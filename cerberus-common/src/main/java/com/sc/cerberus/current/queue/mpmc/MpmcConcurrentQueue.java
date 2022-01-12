package com.sc.cerberus.current.queue.mpmc;

public class MpmcConcurrentQueue<E> implements ConcurrentQueue<E>{

    protected final int size;

    final long mask;

    final Cell<E>[] buffer;


    //头部计数器

    //尾部计数器


    public MpmcConcurrentQueue(final  int capacity) {
        int c = 2;
        while(c < capacity){
            c <<= 1;
        }
        size = c;
        mask = size - 1L;
        buffer = new Cell[size];
        for (int i = 0; i < size; i++) {
            buffer[i] =new Cell<E>(i);
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
        return 0;
    }

    @Override
    public int capacity() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public int remove(E[] e) {
        return 0;
    }

    @Override
    public void clear() {

    }

    //共享对象
    protected static final class Cell<R> {

        //计数器
        R entry;

        Cell(final long s){

        }

    }
}
