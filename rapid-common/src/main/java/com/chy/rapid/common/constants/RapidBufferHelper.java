package com.chy.rapid.common.constants;

/**
 * 网关缓冲区辅助类
 *
 * @author Korben on 2021/12/22
 */
public interface RapidBufferHelper {

    String FLUSHER = "FLUSHER";

    String MPMC = "MPMC";

    static boolean isMpmc(String bufferType) {
        return MPMC.equals(bufferType);
    }

    static boolean isFlusher(String bufferType) {
        return FLUSHER.equals(bufferType);
    }
}
