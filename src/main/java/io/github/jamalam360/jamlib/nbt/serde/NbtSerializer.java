package io.github.jamalam360.jamlib.nbt.serde;

import net.minecraft.nbt.NbtCompound;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.UUID;

/**
 * @author Jamalam
 */
public class NbtSerializer {
    public static NbtCompound serialize(Object obj) {
        return serialize(obj, new NbtCompound());
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public static NbtCompound serialize(Object obj, NbtCompound compound) {
        BeanInfo info = BeanUtil.getBeanInfo(obj);
        if (info == null) return compound;

        for (PropertyDescriptor descriptor : info.getPropertyDescriptors()) {
            Class<?> type = descriptor.getPropertyType();
            String key = descriptor.getName();
            Object value = null;

            try {
                value = descriptor.getReadMethod().invoke(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (type == String.class) {
                compound.putString(key, (String) value);
            } else if (type == int.class) {
                compound.putInt(key, (int) value);
            } else if (type == int[].class) {
                compound.putIntArray(key, (int[]) value);
            } else if (type.isAssignableFrom(List.class)) {
                List<?> list = (List<?>) value;

                if (list.isEmpty()) continue;

                if (list.get(0).getClass() == int.class) {
                    compound.putIntArray(key, (List<Integer>) value);
                } else if (list.get(0).getClass() == byte.class) {
                    compound.putByteArray(key, (List<Byte>) value);
                } else if (list.get(0).getClass() == long.class) {
                    compound.putLongArray(key, (List<Long>) value);
                }
            } else if (type == boolean.class) {
                compound.putBoolean(key, (boolean) value);
            } else if (type == byte.class) {
                compound.putByte(key, (byte) value);
            } else if (type == byte[].class) {
                compound.putByteArray(key, (byte[]) value);
            } else if (type == double.class) {
                compound.putDouble(key, (double) value);
            } else if (type == float.class) {
                compound.putFloat(key, (float) value);
            } else if (type == long.class) {
                compound.putLong(key, (long) value);
            } else if (type == long[].class) {
                compound.putLongArray(key, (long[]) value);
            } else if (type == short.class) {
                compound.putShort(key, (short) value);
            } else if (type == UUID.class) {
                compound.putUuid(key, (UUID) value);
            }
        }

        compound.putString("_notice", "Written by JamLib");

        return compound;
    }
}
