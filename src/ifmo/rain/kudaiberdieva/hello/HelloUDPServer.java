package ifmo.rain.kudaiberdieva.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloUDPServer implements HelloServer {
    private final int LONG_TIMEOUT = 100;

    private ExecutorService workers;
    private DatagramSocket socket;

    /**
     * Starts a new Hello server.
     *
     * @param port    server port.
     * @param threads number of working threads.
     */
    @Override
    public void start(int port, int threads) {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            System.err.println("Can't create socket");
            e.printStackTrace();
            return;
        }

        workers = Executors.newFixedThreadPool(threads);

        for (int i = 0; i < threads; ++i) {
            workers.execute(() -> {
                    try {
                        while (!socket.isClosed()) {
                            DatagramPacket request = new DatagramPacket(new byte[socket.getReceiveBufferSize()],  socket.getReceiveBufferSize());
                            socket.receive(request);
                            String receivedData = new String(request.getData(), request.getOffset(), request.getLength(), StandardCharsets.UTF_8);
                            byte[] responseData = ("Hello, ".concat(receivedData)).getBytes(StandardCharsets.UTF_8);
                            DatagramPacket response = new DatagramPacket(responseData,  responseData.length, request.getAddress(), request.getPort());
                            socket.send(response);                        }
                    } catch (IOException e) {
                        if (!socket.isClosed()) {
                            System.err.println("Errors during receivingsss");
                        }
                    }

            });
        }
    }

    /**
     * Stops server and deallocates all resources.
     */
    @Override
    public void close() {
        socket.close();
        workers.shutdownNow();
    }
}
