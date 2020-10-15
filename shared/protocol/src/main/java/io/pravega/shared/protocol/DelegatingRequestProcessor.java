/**
 * Copyright (c) Dell Inc., or its subsidiaries. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package io.pravega.shared.protocol;

import io.pravega.shared.protocol.WireCommands.CreateSegment;
import io.pravega.shared.protocol.WireCommands.DeleteSegment;
import io.pravega.shared.protocol.WireCommands.GetSegmentAttribute;
import io.pravega.shared.protocol.WireCommands.GetStreamSegmentInfo;
import io.pravega.shared.protocol.WireCommands.KeepAlive;
import io.pravega.shared.protocol.WireCommands.MergeSegments;
import io.pravega.shared.protocol.WireCommands.ReadSegment;
import io.pravega.shared.protocol.WireCommands.SealSegment;
import io.pravega.shared.protocol.WireCommands.SetupAppend;
import io.pravega.shared.protocol.WireCommands.TruncateSegment;
import io.pravega.shared.protocol.WireCommands.UpdateSegmentAttribute;
import io.pravega.shared.protocol.WireCommands.UpdateSegmentPolicy;

/**
 * A RequestProcessor that hands off all implementation to another RequestProcessor.
 * This is useful for creating subclasses that only handle a subset of Commands.
 */
public abstract class DelegatingRequestProcessor implements RequestProcessor {

    public abstract RequestProcessor getNextRequestProcessor();

    @Override
    public void setupAppend(SetupAppend setupAppend) {
        getNextRequestProcessor().setupAppend(setupAppend);
    }

    @Override
    public void append(Append append) {
        getNextRequestProcessor().append(append);
    }

    @Override
    public void readSegment(ReadSegment readSegment) {
        getNextRequestProcessor().readSegment(readSegment);
    }

    @Override
    public void updateSegmentAttribute(UpdateSegmentAttribute updateSegmentAttribute) {
        getNextRequestProcessor().updateSegmentAttribute(updateSegmentAttribute);
    }
    
    @Override
    public void getSegmentAttribute(GetSegmentAttribute getSegmentAttribute) {
        getNextRequestProcessor().getSegmentAttribute(getSegmentAttribute);
    }
    
    @Override
    public void getStreamSegmentInfo(GetStreamSegmentInfo getStreamInfo) {
        getNextRequestProcessor().getStreamSegmentInfo(getStreamInfo);
    }

    @Override
    public void createSegment(CreateSegment createStreamsSegment) {
        getNextRequestProcessor().createSegment(createStreamsSegment);
    }

    @Override
    public void updateSegmentPolicy(UpdateSegmentPolicy updateSegmentPolicy) {
        getNextRequestProcessor().updateSegmentPolicy(updateSegmentPolicy);
    }

    @Override
    public void mergeSegments(MergeSegments mergeSegments) {
        getNextRequestProcessor().mergeSegments(mergeSegments);
    }

    @Override
    public void sealSegment(SealSegment sealSegment) {
        getNextRequestProcessor().sealSegment(sealSegment);
    }

    @Override
    public void truncateSegment(TruncateSegment truncateSegment) {
        getNextRequestProcessor().truncateSegment(truncateSegment);
    }

    @Override
    public void deleteSegment(DeleteSegment deleteSegment) {
        getNextRequestProcessor().deleteSegment(deleteSegment);
    }

    @Override
    public void keepAlive(KeepAlive keepAlive) {
        getNextRequestProcessor().keepAlive(keepAlive);
    }

    @Override
    public void mergeTableSegments(WireCommands.MergeTableSegments mergeSegments) {
       getNextRequestProcessor().mergeTableSegments(mergeSegments);
    }

    @Override
    public void sealTableSegment(WireCommands.SealTableSegment sealTableSegment) {
        getNextRequestProcessor().sealTableSegment(sealTableSegment);
    }

    @Override
    public void createTableSegment(WireCommands.CreateTableSegment createTableSegment) {
        getNextRequestProcessor().createTableSegment(createTableSegment);
    }

    @Override
    public void deleteTableSegment(WireCommands.DeleteTableSegment deleteSegment) {
        getNextRequestProcessor().deleteTableSegment(deleteSegment);
    }

    @Override
    public void updateTableEntries(WireCommands.UpdateTableEntries tableEntries) {
        getNextRequestProcessor().updateTableEntries(tableEntries);
    }

    @Override
    public void removeTableKeys(WireCommands.RemoveTableKeys tableKeys) {
        getNextRequestProcessor().removeTableKeys(tableKeys);
    }

    @Override
    public void readTable(WireCommands.ReadTable readTable) {
        getNextRequestProcessor().readTable(readTable);
    }

    @Override
    public void readTableKeys(WireCommands.ReadTableKeys readTableKeys) {
        getNextRequestProcessor().readTableKeys(readTableKeys);
    }

    @Override
    public void readTableEntries(WireCommands.ReadTableEntries readTableEntries) {
        getNextRequestProcessor().readTableEntries(readTableEntries);
    }

    @Override
    public void readTableEntriesDelta(WireCommands.ReadTableEntriesDelta readTableEntriesDelta) {
        getNextRequestProcessor().readTableEntriesDelta(readTableEntriesDelta);
    }
}