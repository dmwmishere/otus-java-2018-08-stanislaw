package ru.otus.shw16.server;

import lombok.extern.slf4j.Slf4j;
import ru.otus.shw15.messageSystem.Address;
import ru.otus.shw16.messageSystem.MessageSystem;
import ru.otus.shw16.app.MsgWorker;
import ru.otus.shw16.channel.Blocks;
import ru.otus.shw16.channel.SocketMessageWorker;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tully.
 */
@Slf4j
public class ProcessingMessageSocketServer implements SocketServerMXBean {

    private static final int THREADS_NUMBER = 1;
    private static final int PORT = 5050;

    private final ExecutorService executor;
    private final Map<String, MsgWorker> workers;

    private final MessageSystem ms;

    public ProcessingMessageSocketServer(MessageSystem ms) {
        executor = Executors.newFixedThreadPool(THREADS_NUMBER);
        workers = new HashMap<>();
        this.ms = ms;
    }

    @Blocks
    public void start() throws Exception {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            log.info("Server started on port: {}", serverSocket.getLocalPort());
            while (!executor.isShutdown()) {
                Socket socket = serverSocket.accept(); //blocks
                SocketMessageWorker worker = new SocketMessageWorker(socket);
                String nodeId = worker.initServer();
                ms.addAdressee(new Address(nodeId), worker.getInput(), worker.getOutput());
                ms.start(new Address(nodeId));
                workers.put(nodeId, worker);
            }
        }
    }

    @Override
    public boolean getRunning() {
        return true;
    }

    @Override
    public void setRunning(boolean running) {
        if (!running) {
            executor.shutdown();
            log.info("Bye.");
        }
    }
}
