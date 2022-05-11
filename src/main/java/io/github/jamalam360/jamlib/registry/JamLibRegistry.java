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

package io.github.jamalam360.jamlib.registry;

import io.github.jamalam360.jamlib.JamLib;
import io.github.jamalam360.jamlib.registry.entry.BlockEntry;
import io.github.jamalam360.jamlib.registry.entry.BlockWithEntityEntry;
import io.github.jamalam360.jamlib.registry.entry.EnchantmentEntry;
import io.github.jamalam360.jamlib.registry.entry.ItemEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("unused")
public class JamLibRegistry {
    private static final Logger LOGGER = JamLib.getLogger("Registry");

    public void register(String modId, List<Object> registries) {
        for (Object registry : registries) {
            register(modId, registry);
        }
    }

    public void register(String modId, Object registry) {
        for (Field field : registry.getClass().getDeclaredFields()) {
            try {
                if (field.getType().isAssignableFrom(BlockEntry.class)) {
                    BlockEntry entry = (BlockEntry) field.get(registry);

                    Identifier identifier;

                    if (entry.getId() != null) {
                        identifier = entry.getId();
                    } else {
                        String fieldName = field.getName();
                        identifier = new Identifier(modId, fieldName.toLowerCase(Locale.ROOT));
                    }

                    Registry.register(
                            Registry.BLOCK,
                            identifier,
                            entry.getBlock()
                    );

                    if (entry.getItem() != null) {
                        Registry.register(
                                Registry.ITEM,
                                identifier,
                                entry.getItem()
                        );
                    }

                    if (entry instanceof BlockWithEntityEntry blockEntityEntry) {
                        Registry.register(
                                Registry.BLOCK_ENTITY_TYPE,
                                identifier,
                                blockEntityEntry.getBlockEntityType()
                        );
                    }
                } else if (field.getType().isAssignableFrom(ItemEntry.class)) {
                    ItemEntry entry = (ItemEntry) field.get(registry);

                    Identifier identifier;

                    if (entry.getId() != null) {
                        identifier = entry.getId();
                    } else {
                        String fieldName = field.getName();
                        identifier = new Identifier(modId, fieldName.toLowerCase(Locale.ROOT));
                    }

                    Registry.register(
                            Registry.ITEM,
                            identifier,
                            entry.getItem()
                    );
                } else if (field.getType().isAssignableFrom(EnchantmentEntry.class)) {
                    EnchantmentEntry entry = (EnchantmentEntry) field.get(registry);

                    Identifier identifier;

                    if (entry.getId() != null) {
                        identifier = entry.getId();
                    } else {
                        String fieldName = field.getName();
                        identifier = new Identifier(modId, fieldName.toLowerCase(Locale.ROOT));
                    }

                    Registry.register(
                            Registry.ENCHANTMENT,
                            identifier,
                            entry.getEnchantment()
                    );
                }
            } catch (IllegalAccessException e) {
                LOGGER.warn("Failed to access field: " + e.getMessage());
            }
        }
    }
}
