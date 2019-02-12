package ru.otus.shw16.client;

import ru.otus.shw16.channel.SocketMessageWorker;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by tully.
 */
class ClientSocketMessageWorker extends SocketMessageWorker {

    private final Socket socket;

    ClientSocketMessageWorker(String host, int port) throws IOException {
        this(new Socket(host, port));
    }

    private ClientSocketMessageWorker(Socket socket) {
        super(socket);
        this.socket = socket;
    }

    public void close() {
        try {
            super.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
