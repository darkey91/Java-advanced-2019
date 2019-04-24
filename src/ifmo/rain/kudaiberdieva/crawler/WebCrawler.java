package ifmo.rain.kudaiberdieva.crawler;

import info.kgeorgiy.java.advanced.crawler.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class WebCrawler implements Crawler {
    private final ExecutorService downloaders;
    private final ExecutorService extractors;
    private final Downloader downloader;

    private static final int DEFAULT_DEPTH = 5;
    private static final int DEFAULT_DOWNLOADERS = 2;
    private static final int DEFAULT_EXTRACTORS = 4;
    private static final int DEFAULT_PERHOST = 10;

    public WebCrawler(Downloader downloader, int downloaders, int extractors, int perHost) {
        this.downloaders = Executors.newFixedThreadPool(downloaders);
        this.extractors  = Executors.newFixedThreadPool(extractors);
        this.downloader = downloader;
    }

    @Override
    public Result download(String url, int depth) {
        final Set<String> usedUrl =  ConcurrentHashMap.newKeySet();
        final Map<String, IOException> errors = new ConcurrentHashMap<>();

        final Phaser phaser = new Phaser(1);

        usedUrl.add(url);
        crawl(url, depth, usedUrl, errors, phaser);

        phaser.arriveAndAwaitAdvance();
        usedUrl.removeAll(errors.keySet());
        return new Result(new ArrayList<>(usedUrl), errors);
    }

    private void crawl(final String url, final int depth, final Set<String> usedUrl,
                          final Map<String, IOException> errors, final Phaser phaser) {
        if (depth > 0) {
            Runnable downloadTask = () -> {
                try {
                    final Document document = downloader.download(url);
                    Runnable extractTask = () -> {
                        try {
                            for (String link: document.extractLinks()) {
                                if (usedUrl.add(link)) {
                                    crawl(link, depth - 1, usedUrl, errors, phaser);
                                }
                            }
                        } catch (IOException e) {
                            errors.put(url, e);
                        } finally {
                            phaser.arrive();
                        }
                    };

                    if (depth > 1) {
                        phaser.register();
                        synchronized (extractors) {
                            extractors.submit(extractTask);
                        }
                    }

                } catch (IOException e) {
                    errors.put(url, e);
                } finally {
                    phaser.arrive();
                }
            };
            phaser.register();
            synchronized (downloaders) {
                downloaders.submit(downloadTask);
            }
        }
    }

    private static int get_value(String[] args, int index, int default_value) {
        try {
            if (args.length > index && args[index] != null) {
                return Integer.parseInt(args[index]);
            }
        } catch (NumberFormatException e) {
            return default_value;
        }
        return default_value;
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 1 || args[0] == null) {
            System.err.println("Wrong arguments. Usage : WebCrawler url [depth [downloads [extractors [perHost]]]]");
        }

        String url = args[0];
        Downloader downloader = new CachingDownloader();

        int depth = get_value(args, 1, DEFAULT_DEPTH);
        int downloaders = get_value(args, 2, DEFAULT_DOWNLOADERS);
        int extractors = get_value(args, 3, DEFAULT_EXTRACTORS);
        int perhost = get_value(args, 4, DEFAULT_PERHOST);

        WebCrawler crawler = new WebCrawler(downloader, downloaders, extractors, perhost);
        Result  result = crawler.download(url, depth);

        System.out.println("Successfully downloaded " + result.getDownloaded().size() + " pages: ");

        result.getDownloaded().forEach(System.out::println);
        System.out.println("Not downloaded due to error: " + result.getErrors().size());
        result.getErrors().forEach((s, e) -> {
            System.out.println("URL: " + s);
            System.out.println("Error: " + e.getMessage());
        });
    }

    @Override
    public void close() {
        downloaders.shutdownNow();
        extractors.shutdownNow();
    }


}
