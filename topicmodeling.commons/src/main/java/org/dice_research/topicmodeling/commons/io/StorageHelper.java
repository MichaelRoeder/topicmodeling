package org.dice_research.topicmodeling.commons.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class StorageHelper {

    /**
     * Reads and deserializes the object from the file with the given name. Note that only the first object will be read
     * from file.
     * 
     * @param filename
     * @return The read object.
     * @throws IOException
     *             if an IO error occurs
     * @throws ClassNotFoundException
     *             if the class inside the file can't be found
     */
    public static <T extends Serializable> T readFromFile(String filename) throws IOException, ClassNotFoundException {
        T object = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filename);
            object = readFromStream(fis);
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
                // nothing to do
            }
        }
        return object;
    }

    /**
     * Reads and deserializes the object from the given stream. Note that only the first object will be read
     * from file.
     * 
     * @param stream
     * @return The read object.
     * @throws IOException
     *             if an IO error occurs
     * @throws ClassNotFoundException
     *             if the class inside the file can't be found
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T readFromStream(InputStream stream) throws IOException,
            ClassNotFoundException {
        T object = null;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(stream);
            object = (T) ois.readObject();
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
                // nothing to do
            }
        }
        return object;
    }

    /**
     * Reads and deserializes the object from the file with the given name. Note that only the first object will be read
     * from file.
     * 
     * @param filename
     * @return The read object or null if an error occurred.
     */
    public static <T extends Serializable> T readFromFileSavely(String filename) {
        T object = null;
        try {
            object = readFromFile(filename);
        } catch (Exception e) {
            // LOGGER.error("Couldn't load object from file (\"" + filename + "\").", e);
        }
        return object;
    }

    /**
     * Reads and deserializes the object from the given stream. Note that only the first object will be read
     * from file.
     * 
     * @param stream
     * @return The read object.
     * @throws IOException
     *             if an IO error occurs
     * @throws ClassNotFoundException
     *             if the class inside the file can't be found
     */
    public static <T extends Serializable> T readFromStreamSavely(InputStream stream) {
        T object = null;
        try {
            object = readFromStream(stream);
        } catch (Exception e) {
            // LOGGER.error("Couldn't load object from file (\"" + filename + "\").", e);
        }
        return object;
    }

    /**
     * Serializes and stores the given object in the file with the given filename.
     * 
     * @param object
     * @param filename
     * @throws IOException
     *             if an IO error occurs
     */
    public static <T extends Serializable> void storeToFile(T object, String filename) throws IOException {
        File file = new File(filename);
        if ((file.getParentFile() != null) && (!file.getParentFile().exists())) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(filename);
            storeToStream(object, fout);
        } finally {
            try {
                fout.close();
            } catch (Exception e) {
                // nothing to do
            }
        }
    }

    /**
     * Serializes and stores the given object in the given stream.
     * 
     * @param object
     * @param out
     * @throws IOException
     *             if an IO error occurs
     */
    public static <T extends Serializable> void storeToStream(T object, OutputStream out) throws IOException {
        ObjectOutputStream oout = null;
        try {
            oout = new ObjectOutputStream(out);
            oout.writeObject(object);
        } finally {
            try {
                oout.close();
            } catch (Exception e) {
                // nothing to do
            }
        }
    }

    /**
     * Serializes and stores the given object in the file with the given filename.
     * 
     * @param object
     * @param filename
     * @return false if an error occurred, else true
     */
    public static <T extends Serializable> boolean storeToFileSavely(T object, String filename) {
        if (object == null) {
            // LOGGER.error("Can't serialize null.");
            return false;
        }
        try {
            storeToFile(object, filename);
            return true;
        } catch (Exception e) {
            // LOGGER.error("Couldn't store " + object.getClass().getSimpleName() + " object to file.", e);
            return false;
        }
    }

    /**
     * Serializes and stores the given object in the given stream.
     * 
     * @param object
     * @param out
     * @return false if an error occurred, else true
     */
    public static <T extends Serializable> boolean storeToStreamSavely(T object, OutputStream out) {
        if (object == null) {
            // LOGGER.error("Can't serialize null.");
            return false;
        }
        try {
            storeToStream(object, out);
            return true;
        } catch (Exception e) {
            // LOGGER.error("Couldn't store " + object.getClass().getSimpleName() + " object to file.", e);
            return false;
        }
    }
}
