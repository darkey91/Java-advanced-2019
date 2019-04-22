package ifmo.rain.kudaiberdieva.crawler;

import info.kgeorgiy.java.advanced.crawler.Crawler;
import info.kgeorgiy.java.advanced.crawler.Document;
import info.kgeorgiy.java.advanced.crawler.Downloader;
import info.kgeorgiy.java.advanced.crawler.Result;

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

    @Override
    public void close() {
        downloaders.shutdown();
        extractors.shutdown();
    }


}
