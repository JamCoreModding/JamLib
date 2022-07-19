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

package io.github.jamalam360.jamlib.keybind;

import com.mojang.blaze3d.platform.InputUtil;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBind;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Jamalam
 */
public class JamLibKeybinds {
    private static final List<JamLibKeybindImpl> KEY_BINDS = new ArrayList<>();

    public static void register(JamLibKeybind keyBind) {
        KeyBind keyBindBacker = new KeyBind(
                "key." + keyBind.modId() + "." + keyBind.name(),
                InputUtil.Type.KEYSYM,
                keyBind.keyCode(),
                "key.category." + keyBind.modId()
        );

        KEY_BINDS.add(new JamLibKeybindImpl(
                keyBindBacker,
                keyBind.wasPressedConsumer()
        ));

        KeyBindingHelper.registerKeyBinding(keyBindBacker);
    }

    @ApiStatus.Internal
    public static void onEndTick(MinecraftClient client) {
        KEY_BINDS.forEach(keyBind -> {
            while (keyBind.keyBind().wasPressed()) {
                keyBind.wasPressedConsumer().accept(client);
            }
        });
    }

    public record JamLibKeybind(String modId, String name, int keyCode, Consumer<MinecraftClient> wasPressedConsumer) {
    }

    private record JamLibKeybindImpl(KeyBind keyBind, Consumer<MinecraftClient> wasPressedConsumer) {
    }
}
