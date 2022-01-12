package com.sc.cerberus.current.queue.mpmc;

public interface ConcurrentQueue<E> {
    Boolean offer(E e);

    E poll();

    E peek();

    int size();

    int capacity();

    boolean isEmpty();

    boolean contains(Object o);

    int remove(E[] e);


    void clear();
}
