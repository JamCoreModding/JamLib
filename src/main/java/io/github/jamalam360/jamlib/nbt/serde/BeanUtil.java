package io.github.jamalam360.jamlib.nbt.serde;

import io.github.jamalam360.jamlib.JamLib;
import org.apache.logging.log4j.Logger;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;

/**
 * @author Jamalam
 */
public class BeanUtil {
    private static final Logger LOGGER = JamLib.getLogger("BeanUtil");

    public static BeanInfo getBeanInfo(Object obj) {
        try {
            return Introspector.getBeanInfo(obj.getClass());
        } catch (IntrospectionException e) {
            LOGGER.error("Failed to serialize object: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
