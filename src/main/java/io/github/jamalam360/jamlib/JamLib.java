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

package io.github.jamalam360.jamlib;

import io.github.jamalam360.jamlib.nbt.serde.NbtSerializer;
import io.github.jamalam360.jamlib.tick.TickScheduling;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class JamLib implements ModInitializer {
    public static Logger getLogger(String subName) {
        return LogManager.getLogger("JamLib/" + subName);
    }

    @Override
    public void onInitialize() {
        ServerTickEvents.END_WORLD_TICK.register(TickScheduling::onEndTickServer);

        getLogger("JamLibInit").info("JamLib has been initialized");

        Registry.register(Registry.ITEM, new Identifier("e", "e"), new Item(new FabricItemSettings().group(ItemGroup.TOOLS)) {
            @Override
            public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
                user.getStackInHand(hand).getOrCreateSubNbt("TestBean").put("TestBeanSub", NbtSerializer.serialize(new TestBean()));
                return TypedActionResult.success(user.getStackInHand(hand));
            }
        });
    }

    public static class TestBean {
        private String foo;
        private boolean shouldBeTrue;
        private UUID randomUuid;

        public TestBean() {
            this.foo = "bar";
            this.shouldBeTrue = true;
            this.randomUuid = UUID.randomUUID();
        }

        public String getFoo() {
            return foo;
        }

        public void setFoo(String foo) {
            this.foo = foo;
        }

        public boolean isShouldBeTrue() {
            return shouldBeTrue;
        }

        public void setShouldBeTrue(boolean shouldBeTrue) {
            this.shouldBeTrue = shouldBeTrue;
        }

        public UUID getRandomUuid() {
            return randomUuid;
        }

        public void setRandomUuid(UUID randomUuid) {
            this.randomUuid = randomUuid;
        }
    }
}
