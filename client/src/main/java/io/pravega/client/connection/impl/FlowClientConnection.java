/**
 * Copyright (c) Dell Inc., or its subsidiaries. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package io.pravega.client.connection.impl;

import io.pravega.shared.protocol.netty.Append;
import io.pravega.shared.protocol.netty.ConnectionFailedException;
import io.pravega.shared.protocol.netty.WireCommand;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FlowClientConnection implements ClientConnection {

    @Getter
    private final String connectionName;
    private final ClientConnection channel;
    @Getter
    private final int flowId;
    private final FlowHandler handler;

    @Override
    public void send(WireCommand cmd) throws ConnectionFailedException {
        channel.send(cmd);
    }

    @Override
    public void send(Append append) throws ConnectionFailedException {
        channel.send(append);
    }

    @Override
    public void sendAsync(List<Append> appends, CompletedCallback callback) {
        channel.sendAsync(appends, callback);
    }

    @Override
    public void close() {
        handler.closeFlow(this);
    }

}
