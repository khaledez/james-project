/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.ImmutableList;

public class IteratorWrapperTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void getEntriesSeenShouldReturnEmptyWhenNotConsumed() {
        ImmutableList<Integer> originalData = ImmutableList.of(1, 2, 3);
        IteratorWrapper<Integer> integerIteratorWrapper = new IteratorWrapper<>(originalData.iterator());

        assertThat(integerIteratorWrapper.getEntriesSeen()).isEmpty();
    }

    @Test
    public void getEntriesSeenShouldReturnViewOfConsumedData() {
        ImmutableList<Integer> originalData = ImmutableList.of(1, 2, 3);
        IteratorWrapper<Integer> integerIteratorWrapper = new IteratorWrapper<>(originalData.iterator());

        consume(integerIteratorWrapper);

        assertThat(integerIteratorWrapper.getEntriesSeen())
            .containsExactlyElementsOf(originalData);
    }

    @Test
    public void getEntriesSeenShouldReturnViewOfConsumedDataWhenPartiallyConsumed() {
        ImmutableList<Integer> originalData = ImmutableList.of(1, 2, 3);
        IteratorWrapper<Integer> integerIteratorWrapper = new IteratorWrapper<>(originalData.iterator());

        integerIteratorWrapper.next();
        integerIteratorWrapper.next();

        assertThat(integerIteratorWrapper.getEntriesSeen())
            .containsOnly(1, 2);
    }

    @Test
    public void getEntriesSeenShouldReturnEmptyWhenSuppliedEmpty() {
        ImmutableList<Integer> originalData = ImmutableList.of();
        IteratorWrapper<Integer> integerIteratorWrapper = new IteratorWrapper<>(originalData.iterator());

        consume(integerIteratorWrapper);

        assertThat(integerIteratorWrapper.getEntriesSeen())
            .containsExactlyElementsOf(originalData);
    }

    @Test
    public void constructorShouldThrowOnNull() {
        expectedException.expect(NullPointerException.class);

        new IteratorWrapper<Integer>(null);
    }

    private void consume(IteratorWrapper<Integer> integerIteratorWrapper) {
        while (integerIteratorWrapper.hasNext()) {
            integerIteratorWrapper.next();
        }
    }

}