package server;

import java.io.*;
import java.util.*;

public class Database implements java.io.Serializable {

    private class FileList implements java.io.Serializable {
        private List<File> files = new ArrayList<>();
        private class File implements java.io.Serializable {
            private String name;
            private int hashCode;
            private String content;

            File(String name, String content) {
                this.name = name;
                this.hashCode = name.hashCode();
                this.content = content;
            }

            public String getName() {
                return name;
            }

            public int getHashCode() {
                return hashCode;
            }

            public String getContent() {
                return content;
            }

            @Override
            public String toString() {
                return "File{" +
                        "name='" + name + '\'' +
                        ", hashCode=" + hashCode +
                        ", content='" + content + '\'' +
                        '}';
            }
        }

        public void addFile(String name, String content) {
            files.add(new File(name,content));
        }

        public boolean containsFile(String name) {
            return files.stream().filter(o -> o.getName().equals(name)).findFirst().isPresent();
        }

        public File getFileByName(String name) {
            return files.stream().filter(o -> o.getName().equals(name)).findFirst().get();
        }

        public File getFileByID(int id) {
            return files.stream().filter(o -> o.getHashCode() == id).findFirst().get();
        }

        public void removeFile(String name) {
            files.remove(getFileByName(name));
        }

        public void removeFileByID(int ID) {
            files.remove(getFileByID(ID));
        }

        @Override
        public String toString() {
            return "FileList{" +
                    "files=" + files +
                    '}';
        }
    }


    private FileList filenames = new FileList();

    public Database() {
        try {
            FileInputStream fileIn = new FileInputStream("data.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            filenames = (FileList) in.readObject();
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
        if (filenames.containsFile(name)) {
            return false;
        } else {
            filenames.addFile(name, content);
            saveDatabase();
            return true;
        }

    }

    public boolean removeFile(String name) throws IOException {
        if (filenames.containsFile(name)) {
            filenames.removeFile(name);
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
        if (filenames.containsFile(name)) {
            filenames.removeFile(name);
            saveDatabase();
            return new Pair<>(filenames.getFileByName(name).getContent(), true);
        } else {
            return new Pair<>("404", false);
        }

    }

    @Override
    public String toString() {
        return "Database " + filenames.toString();
    }
}