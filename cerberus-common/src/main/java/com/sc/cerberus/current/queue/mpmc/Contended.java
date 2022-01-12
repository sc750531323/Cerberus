package com.sc.cerberus.current.queue.mpmc;

public class Contended {
public static final int CACHE_LINE = Integer.getInteger("Intel.CacheLineSize",6);

    public static void main(String[] args) {
        System.out.println(CACHE_LINE);
    }
}
