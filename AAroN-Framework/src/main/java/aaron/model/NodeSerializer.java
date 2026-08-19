package aaron.model;

import java.io.*;

public final class NodeSerializer {

    public static byte[] serialize(AAroNNode obj) {
        if (obj == null) return null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            oos.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            throw new UncheckedIOException("Fehler beim Serialisieren von AAroNNode", e);
        }
    }

    public static AAroNNode deserialize(byte[] bytes) {
        if (bytes == null) return null;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            Object o = ois.readObject();
            return (AAroNNode) o;
        } catch (IOException e) {
            throw new UncheckedIOException("Fehler beim Deserialisieren von AAroNNode", e);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Klasse beim Deserialisieren nicht gefunden", e);
        }
    }
}
