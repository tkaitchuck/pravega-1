/**
 * Copyright (c) Dell Inc., or its subsidiaries. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package io.pravega.client.nonetty.impl;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import io.pravega.client.ClientConfig;
import io.pravega.common.concurrent.ExecutorServiceHelpers;
import io.pravega.shared.protocol.netty.PravegaNodeUri;
import io.pravega.shared.protocol.netty.ReplyProcessor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class SocketConnectionFactoryImpl implements ConnectionFactory {

    private static final AtomicInteger POOLCOUNT = new AtomicInteger();

    private final ClientConfig clientConfig;
    private final ScheduledExecutorService executor;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    public SocketConnectionFactoryImpl(ClientConfig clientConfig) {
        this(clientConfig, (Integer) null);
    }

    @VisibleForTesting
    public SocketConnectionFactoryImpl(ClientConfig clientConfig, Integer numThreadsInPool) {
        this.clientConfig = Preconditions.checkNotNull(clientConfig, "clientConfig");
        this.executor = ExecutorServiceHelpers.newScheduledThreadPool(getThreadPoolSize(numThreadsInPool),
                "clientInternal-" + POOLCOUNT.incrementAndGet());
    }

    @VisibleForTesting
    public SocketConnectionFactoryImpl(ClientConfig clientConfig, ScheduledExecutorService executor) {
        this.clientConfig = Preconditions.checkNotNull(clientConfig, "clientConfig");
        this.executor = executor;
    }


    @Override
    public CompletableFuture<ClientConnection> establishConnection(PravegaNodeUri endpoint, ReplyProcessor rp) {
        return CompletableFuture.completedFuture(new TcpClientConnection(endpoint.getEndpoint(), endpoint.getPort(),
                this.clientConfig, rp));
    }


    private int getThreadPoolSize(Integer threadCount) {
        if (threadCount != null) {
            return threadCount;
        }
        String configuredThreads = System.getProperty("pravega.client.internal.threadpool.size", null);
        if (configuredThreads != null) {
            return Integer.parseInt(configuredThreads);
        }
        return Runtime.getRuntime().availableProcessors();
    }

    @Override
    public void close() {
        log.info("Shutting down connection factory");
        if (closed.compareAndSet(false, true)) {
            ExecutorServiceHelpers.shutdown(executor);
        }
    }
}