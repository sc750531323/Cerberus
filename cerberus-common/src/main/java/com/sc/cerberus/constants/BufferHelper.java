package com.sc.cerberus.constants;

public class BufferHelper {
    public  static String FLUSHER = "FLUSHER";
    static String MPMC = "MPMC";

    public static boolean isMpmc(String bufferType) {
        return MPMC.equals(bufferType);
    }

    public static boolean isFlusher(String bufferType) {
        return FLUSHER.equals(bufferType);
    }
}
