package server;

import java.util.*;

public class Database implements java.io.Serializable {
    private static Map<String, String> filenames = new HashMap<>();

    public Database() {

    }


    private void saveDatabase() {

    }

    public boolean addFile(String name, String content) {
        if (filenames.containsKey(name)) {
            return false;
        } else {
            filenames.put(name, content);
            return true;
        }

    }

    public boolean removeFile(String name) {
        if (filenames.containsKey(name)) {
            filenames.remove(name);
            return true;
        } else {
            return false;
        }
    }

    public Pair<String, Boolean> getFile(String name) {
        if (filenames.containsKey(name)) {
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