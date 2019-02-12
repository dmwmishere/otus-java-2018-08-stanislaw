package ru.otus.shw16.app;

import ru.otus.shw16.channel.Blocks;

import java.io.Closeable;

/**
 * Created by tully.
 */
public interface MsgWorker<T> extends Closeable {
    void send(T msg);

    @Blocks
    T take() throws InterruptedException;

    void close();
}
