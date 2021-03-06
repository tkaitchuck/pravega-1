/**
 * Copyright (c) 2017 Dell Inc., or its subsidiaries. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package io.pravega.client.stream.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.pravega.client.segment.impl.Segment;
import java.util.Collections;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CheckpointStateTest {

    @Test
    public void testCheckpointNoReaders() {
        CheckpointState state = new CheckpointState();
        state.beginNewCheckpoint("foo", ImmutableSet.of(), Collections.emptyMap());
        assertTrue(state.isCheckpointComplete("foo"));
        assertFalse(state.getPositionsForLatestCompletedCheckpoint().isPresent());
    }
    
    @Test
    public void testCheckpointCompletes() {
        CheckpointState state = new CheckpointState();
        state.beginNewCheckpoint("foo", ImmutableSet.of("a", "b"), Collections.emptyMap());
        assertFalse(state.isCheckpointComplete("foo"));
        state.readerCheckpointed("foo", "a", ImmutableMap.of(getSegment("S1"), 1L));
        assertFalse(state.isCheckpointComplete("foo"));
        assertNull(state.getPositionsForCompletedCheckpoint("foo"));
        state.readerCheckpointed("foo", "b", ImmutableMap.of(getSegment("S2"), 2L));
        assertTrue(state.isCheckpointComplete("foo"));
        Map<Segment, Long> completedCheckpoint = state.getPositionsForCompletedCheckpoint("foo");
        assertNotNull(completedCheckpoint);
        assertEquals(ImmutableMap.of(getSegment("S1"), 1L, getSegment("S2"), 2L), completedCheckpoint);
        state.clearCheckpointsBefore("foo");
        assertEquals(ImmutableMap.of(getSegment("S1"), 1L, getSegment("S2"), 2L),
                state.getPositionsForLatestCompletedCheckpoint().get());
    }

    @Test
    public void testGetCheckpointForReader() {
        CheckpointState state = new CheckpointState();
        state.beginNewCheckpoint("foo", ImmutableSet.of("a", "b"), Collections.emptyMap());
        assertEquals("foo", state.getCheckpointForReader("a"));
        assertEquals("foo", state.getCheckpointForReader("b"));
        assertEquals(null, state.getCheckpointForReader("c"));
        state.readerCheckpointed("foo", "a", Collections.emptyMap());
        assertEquals(null, state.getCheckpointForReader("a"));
        assertEquals("foo", state.getCheckpointForReader("b"));
        state.clearCheckpointsBefore("foo");
        assertEquals(null, state.getCheckpointForReader("a"));
        assertEquals("foo", state.getCheckpointForReader("b"));
        assertFalse(state.getPositionsForLatestCompletedCheckpoint().isPresent());
    }
    
    @Test
    public void testCheckpointsCleared() {
        CheckpointState state = new CheckpointState();
        state.beginNewCheckpoint("1", ImmutableSet.of("a", "b"), Collections.emptyMap());
        state.beginNewCheckpoint("2", ImmutableSet.of("a", "b"), Collections.emptyMap());
        state.beginNewCheckpoint("3", ImmutableSet.of("a", "b"), Collections.emptyMap());
        assertEquals("1", state.getCheckpointForReader("a"));
        assertEquals("1", state.getCheckpointForReader("b"));
        assertEquals(null, state.getCheckpointForReader("c"));
        state.readerCheckpointed("1", "a", Collections.emptyMap());
        assertEquals("2", state.getCheckpointForReader("a"));
        assertEquals("1", state.getCheckpointForReader("b"));
        state.clearCheckpointsBefore("2");
        assertEquals("2", state.getCheckpointForReader("a"));
        assertEquals("2", state.getCheckpointForReader("b"));
        state.clearCheckpointsBefore("3");
        assertEquals("3", state.getCheckpointForReader("a"));
        assertEquals("3", state.getCheckpointForReader("b"));
        assertFalse(state.getPositionsForLatestCompletedCheckpoint().isPresent());
    }

    private Segment getSegment(String name) {
        return new Segment("ExampleScope", name, 0);
    }

}
