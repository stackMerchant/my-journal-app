package com.svats.journalApp.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ExceptionUtils {

    @FunctionalInterface
    public interface CheckedSupplier<T> {
        T get() throws Exception;
    }

    @FunctionalInterface
    public interface CheckedRunnable {
        void run() throws Exception;
    }

    public static void handleException(CheckedRunnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            log.info("Exception occurred: {}", e.getMessage());
        }
    }

    public static <T> T handleException(CheckedSupplier<T> supplier, T fallback) {
        try {
            return supplier.get();
        } catch (Exception e) {
            log.info("Exception occurred: {}", e.getMessage());
            return fallback;
        }
    }

}
