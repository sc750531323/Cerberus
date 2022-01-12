package com.sc.cerberus.current.queue.mpmc;


public class MpmcConcurrentQueue<E> implements ConcurrentQueue<E> {

    protected final int size;

    final long mask;

    final Cell<E>[] buffer;


    //头部计数器
    ContendedAtomicLong head = new ContendedAtomicLong(0L);
    //尾部计数器
    ContendedAtomicLong tail = new ContendedAtomicLong(0L);

    public MpmcConcurrentQueue(final int capacity) {
        int c = 2;
        while (c < capacity) {
            c <<= 1;
        }
        size = c;
        mask = size - 1L;
        buffer = new Cell[size];
        for (int i = 0; i < size; i++) {
            buffer[i] = new Cell<E>(i);
        }
    }

    @Override
    public Boolean offer(E e) {
        return null;
    }

    @Override
    public E poll() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public int size() {
        return (int)Math.max((tail.get() - head.get()),0);
    }

    @Override
    public int capacity() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return head.get() == tail.get();
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size(); i++) {
            int slot = (int) ((head.get() + i) & mask);
            if(buffer[slot].entry != null && buffer[slot].entry.equals(o)){
                return true;
            }
        }
        return false;
    }


    @Override
    public int remove(E[] e) {
        return 0;
    }

    @Override
    public void clear() {
        while (!isEmpty()){
            poll();
        }
    }

    //共享对象
    protected static final class Cell<R> {

        //计数器
        final ContendedAtomicLong seq = new ContendedAtomicLong(0L);

        R entry;

        Cell(final long s) {
            seq.set(s);
            entry = null;
        }

    }
}
