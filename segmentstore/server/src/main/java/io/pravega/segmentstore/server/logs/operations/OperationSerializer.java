/**
 * Copyright (c) 2017 Dell Inc., or its subsidiaries. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package io.pravega.segmentstore.server.logs.operations;

import io.pravega.common.io.serialization.VersionedSerializer;
import io.pravega.segmentstore.server.logs.Serializer;

/**
 * Operation Serializer.
 */
public class OperationSerializer extends VersionedSerializer.MultiType<Operation> implements Serializer<Operation> {
    public static final OperationSerializer DEFAULT = new OperationSerializer();

    @Override
    protected void declareSerializers(Builder b) {
        // Unused values:
        // - 0: Unsupported Serializer.
        // - 5: TransactionMapOperation (retired).
        b.serializer(StreamSegmentAppendOperation.class, 1, new StreamSegmentAppendOperation.Serializer())
         .serializer(StreamSegmentSealOperation.class, 2, new StreamSegmentSealOperation.Serializer())
         .serializer(MergeSegmentOperation.class, 3, new MergeSegmentOperation.Serializer())
         .serializer(StreamSegmentMapOperation.class, 4, new StreamSegmentMapOperation.Serializer())
         .serializer(UpdateAttributesOperation.class, 6, new UpdateAttributesOperation.Serializer())
         .serializer(StreamSegmentTruncateOperation.class, 7, new StreamSegmentTruncateOperation.Serializer())
         .serializer(MetadataCheckpointOperation.class, 8, new MetadataCheckpointOperation.Serializer())
         .serializer(StorageMetadataCheckpointOperation.class, 9, new StorageMetadataCheckpointOperation.Serializer());
    }
}
