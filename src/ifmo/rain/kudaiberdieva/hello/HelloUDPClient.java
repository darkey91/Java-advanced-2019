package ifmo.rain.kudaiberdieva.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HelloUDPClient implements HelloClient {
    private final int TIMEOUT = 100;
    private final int LONG_TIMEOUT = 500;

    private static final int DEFAULT_WORKERS = 4;
    private static final int DEFAULT_REQUESTS = 4;

    @Override
    public void run(String host, int port, String prefix, int threads, int requests) {
        try {
            InetAddress addr = InetAddress.getByName(host);
            ExecutorService workers = Executors.newFixedThreadPool(threads);

            for (int i = 0; i < threads; ++i) {
                final int threadIndex = i;
                workers.execute(() -> {
                    try (DatagramSocket socket = new DatagramSocket()) {
                        socket.setSoTimeout(TIMEOUT);
                        for (int j = 0; j < requests; ++j) {
                            String requestStr = prefix.concat(Integer.toString(threadIndex)).concat("_").concat(Integer.toString(j));
                            System.out.println(requestStr);

                            while (true) {
                                try {
                                    DatagramPacket request = new DatagramPacket(requestStr.getBytes(StandardCharsets.UTF_8), requestStr.length(), addr, port);
                                    socket.send(request);
                                } catch (IOException e) {
                                    System.err.println(String.format("Error during sending %d request from %d thread", j, threadIndex));
                                    continue;
                                }

                                try {
                                    DatagramPacket response = new DatagramPacket(new byte[socket.getReceiveBufferSize()], socket.getReceiveBufferSize());
                                    socket.receive(response);
                                    String responseStr = new String(response.getData(), 0, response.getLength(), StandardCharsets.UTF_8);
                                    if (responseStr.contains(requestStr)) {
                                        System.out.println(responseStr);
                                        break;
                                    }
                                } catch (IOException e) {
                                    System.err.println(String.format("Error during receiving response for %d request from %d thread", j, threadIndex));
                                }
                            }
                        }
                    } catch (SocketException e) {
                        System.err.println("Can't create socket");
                        e.printStackTrace();
                    }
                });
            }

            workers.shutdown();
            if (!workers.awaitTermination(LONG_TIMEOUT, TimeUnit.SECONDS)) {
                System.err.println("Can't shutdown threads");
            }

        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + host);
        } catch (InterruptedException e) {
            System.err.println("Interrupted while waiting");
        }
    }

    @SuppressWarnings("Dublicates")
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

    public static void main(String[] args) {
        if (args == null || args.length != 5 || args[0] == null || args[1] == null || args[2] == null) {
            System.out.println("Incorrect input. Usage: [host or ip] [port] [prefix] [threads amount] [requests amount per thread]");
            return;
        }
        int threads = get_value(args, 3, DEFAULT_WORKERS);
        int requests = get_value(args, 4, DEFAULT_REQUESTS);

        try {
            int port = Integer.parseInt(args[1]);
            new HelloUDPClient().run(args[0], port, args[2], threads, requests);
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
        }
    }
}
