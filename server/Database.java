package server;

import java.io.*;
import java.util.*;

public class Database implements java.io.Serializable {

    public class File implements java.io.Serializable {
        private String name;
        private int hashCode;
        private String content;

        File(String name, String content) {
            this.name = name;
            this.hashCode = Integer.parseInt(Integer.toString(name.hashCode()).substring(0, 2));
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


    public class FileList implements java.io.Serializable {
        public List<File> files = new ArrayList<>();

        public void addFile(String name, String content) {
            files.add(new File(name,content));
        }

        public boolean containsFile(String name) {
            return files.stream().filter(o -> o.getName().equals(name)).findFirst().isPresent();
        }

        public boolean containsFileByID(int ID) {
            return files.stream().filter(o -> o.getHashCode() == ID).findFirst().isPresent();
        }

        public File getFileByName(String name) {
            for(File file : files) {
                if(file.getName().equals(name)) {
                    System.out.println(file);
                    return file;
                }
            }

            return null;
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

    public boolean removeFile(String name, String choice) throws IOException {
        boolean containsfile;

        if (choice.equals("2")) {
            containsfile = filenames.containsFileByID(Integer.parseInt(name, 10));
        } else {
            containsfile = filenames.containsFile(name);
        }

        if (containsfile) {
            File filenameTrue;

            if (choice.equals("2")) {
                filenameTrue = filenames.getFileByID(Integer.parseInt(name, 10));
                filenames.removeFileByID(Integer.parseInt(name, 10));
            } else {
                filenameTrue = filenames.getFileByName(name);
                filenames.removeFile(name);
            }

            saveDatabase();
            String absoluteFilePath = "C:\\Users\\Yuriy Volkovskiy\\Desktop\\File Server\\File Server\\task\\src\\server\\data\\" + filenameTrue.getName();
            java.io.File file = new java.io.File(absoluteFilePath);
            try {
                file.delete();
            } catch (Exception e) { }
            return true;
        } else {
            return false;
        }
    }

    public Pair<File, Boolean> getFile(String name, String choice) throws IOException {
        boolean containsfile;

        if (choice.equals("2")) {
            containsfile = filenames.containsFileByID(Integer.parseInt(name, 10));
        } else {
            containsfile = filenames.containsFile(name);
        }

        if (containsfile) {
            saveDatabase();
            System.out.println(choice);
            if (choice.equals("2")) {

                return new Pair<>(filenames.getFileByID(Integer.parseInt(name, 10)), true);
            } else {
                return new Pair<>(filenames.getFileByName(name), true);
            }

        } else {

            return new Pair<>(new File("trrer", "trtrrte"), false);
        }

    }

    @Override
    public String toString() {
        return "Database " + filenames.toString();
    }
}