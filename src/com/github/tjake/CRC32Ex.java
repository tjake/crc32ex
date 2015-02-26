/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.tjake;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;

class CRC32Ex extends CRC32 implements ICRC32
{
    @Override
    public void update(ByteBuffer b, int offset, int length)
    {
        final int oldPosition = b.position();
        final int oldLimit = b.limit();
        try
        {
            b.limit(offset + length);
            b.position(offset);
            update(b);
        }
        finally
        {
            b.position(oldPosition);
            b.limit(oldLimit);
        }
    }

    @Override
    public void updateInt(int v)
    {
        update((v >>> 24) & 0xFF);
        update((v >>> 16) & 0xFF);
        update((v >>> 8) & 0xFF);
        update((v >>> 0) & 0xFF);
    }

    @Override
    public int getCrc()
    {
        return (int)getValue();
    }
}
