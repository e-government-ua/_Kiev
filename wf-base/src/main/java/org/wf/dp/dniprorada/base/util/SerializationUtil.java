package org.wf.dp.dniprorada.base.util;

import org.jboss.serial.io.JBossObjectInputStream;
import org.jboss.serial.io.JBossObjectOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Utility class to serialize and deserialize document.
 * JBossObjectOutputStream and JBossObjectInputStream used because they are 10 faster then default java serialization
 * and doesn't require from classes to implement serializable interface.
 *
 * @see <a href="http://serialization.jboss.org">serialization.jboss.org</a>
 */
public final class SerializationUtil {
    private static final Logger LOG = LoggerFactory.getLogger(SerializationUtil.class);

    private SerializationUtil() {
        //Utility doesn't need constructor
    }

    /**
     * Converts an object to a serialized byte array.
     *
     * @param obj Object to be converted.
     * @return byte[] Serialized array representing the object.
     */
    public static byte[] getByteArrayFromObject(Object obj) {
        byte[] result = null;

        try (final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                final ObjectOutputStream oos = new JBossObjectOutputStream(baos)) {
            oos.writeObject(obj);
            oos.flush();
            result = baos.toByteArray();
        } catch (IOException ioEx) {
            LOG.error("Error converting object to byteArray", ioEx);
        }

        return result;
    }

    /**
     * Utility method to un-serialize objects from byte arrays.
     *
     * @param bytes The input byte array.
     * @return The output object.
     */
    public static Object getObjectFromByteArray(byte[] bytes) {
        Object result = null;

        try (final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                final ObjectInputStream ois = new JBossObjectInputStream(bais)) {
            result = ois.readObject();
        } catch (IOException ioEx) {
            LOG.error("Unable to deserialize object from byte array.", ioEx);
        } catch (ClassNotFoundException cnfEx) {
            LOG.error("No corresponding class for byte array.", cnfEx);
        }

        return result;
    }

}