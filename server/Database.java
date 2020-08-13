package server;

import java.io.*;
import java.util.*;

public class Database implements java.io.Serializable {
    private static Map<String, String> filenames = new HashMap<>();

    public Database() {
        try {
            FileInputStream fileIn = new FileInputStream("data.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            filenames = (HashMap<String, String>) in.readObject();
            in.close();
            fileIn.close();
        } catch (Exception i) { }
    }


    private void saveDatabase() throws IOException {
        FileOutputStream fileOut =
                new FileOutputStream("data.ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(filenames);
        out.close();
        fileOut.close();
    }


    public boolean addFile(String name, String content) throws IOException {
        if (filenames.containsKey(name)) {



            return false;
        } else {
            filenames.put(name, content);
            saveDatabase();
            return true;
        }

    }

    public boolean removeFile(String name) throws IOException {
        if (filenames.containsKey(name)) {
            filenames.remove(name);
            saveDatabase();
            String absoluteFilePath = "C:\\Users\\Yuriy Volkovskiy\\Desktop\\File Server\\File Server\\task\\src\\server\\data\\" + name;
            File file = new File(absoluteFilePath);
            try {
                file.delete();
            } catch (Exception e) { }
            return true;
        } else {
            return false;
        }
    }

    public Pair<String, Boolean> getFile(String name) throws IOException {
        if (filenames.containsKey(name)) {
            saveDatabase();
            return new Pair<>(filenames.get(name), true);
        } else {
            return new Pair<>("404", false);
        }

    }

    @Override
    public String toString() {
        return "Database " + filenames.toString();
    }
}