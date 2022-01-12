package com.sc.cerberus.current.queue.mpmc;

import java.util.concurrent.atomic.AtomicLongArray;
//缓冲行填充技术
public class ContendedAtomicLong extends Contended{
    //一个缓冲行需要多少Long元素填充
    private static final int CACHE_LINE_LONGS = CACHE_LINE/Long.BYTES;
    private final AtomicLongArray contendedArray;

    public ContendedAtomicLong(long init) {
        this.contendedArray = new AtomicLongArray(2 * CACHE_LINE_LONGS);
        set(init);
    }

    void set(long l){
        contendedArray.set(CACHE_LINE_LONGS,1);
    }

    long get(){
        return contendedArray.get(CACHE_LINE_LONGS);
    }

    @Override
    public String toString() {
        return Long.toString(get());
    }

    public boolean compareAndSet(final long expect,final long l){
        return contendedArray.compareAndSet(CACHE_LINE_LONGS,expect,l);
    }
}
