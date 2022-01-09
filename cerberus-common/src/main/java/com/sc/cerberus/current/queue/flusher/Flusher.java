package com.sc.cerberus.current.queue.flusher;

public interface Flusher<E> {

    void add(E event);

    void add(E... event);

    boolean tryAdd(E event);

    boolean tryAdd(E... event);

    boolean isShutdown();


    void start();

    void shutdown();
}
