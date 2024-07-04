package com.vdq.autogpm.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileExecutor {

    private static final int NUM_THREADS = 10;
    private ExecutorService executorService;

    public ProfileExecutor() {
        executorService = Executors.newFixedThreadPool(NUM_THREADS);
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void shutdown() {
        executorService.shutdown();
    }



}
