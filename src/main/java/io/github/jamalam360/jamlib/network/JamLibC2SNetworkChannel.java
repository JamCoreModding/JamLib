/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022 Jamalam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.jamalam360.jamlib.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Jamalam
 */
public class JamLibC2SNetworkChannel extends JamLibNetworkChannel<ServerPlayNetworking.PlayChannelHandler> {
    public JamLibC2SNetworkChannel(Identifier identifier) {
        super(identifier);
    }

    public void send() {
        PacketByteBuf buf = PacketByteBufs.empty();
        ClientPlayNetworking.send(this.getIdentifier(), buf);
    }

    public void send(Consumer<PacketByteBuf> dataWriter) {
        PacketByteBuf buf = PacketByteBufs.create();
        dataWriter.accept(buf);
        ClientPlayNetworking.send(this.getIdentifier(), buf);
    }

    public void setHandler(ServerPlayNetworking.PlayChannelHandler handler) {
        this.handler = handler;
        List<JamLibNetworkChannel<?>> list = JamLibServerNetworking.SERVER_CHANNELS.getOrDefault(this.getIdentifier().getNamespace(), new ArrayList<>());
        list.add(this);
        JamLibServerNetworking.SERVER_CHANNELS.put(this.getIdentifier().getNamespace(), list);
    }
}
