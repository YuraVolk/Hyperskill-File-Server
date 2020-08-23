package server;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Database implements java.io.Serializable {

    public class File implements java.io.Serializable {
        private String name;
        private int hashCode;
        private String content;

        File(String name, String content) {
            this.name = name;
            this.hashCode = Integer.parseInt(Integer.toString(name.length() + 1 * 25).substring(0, 2));
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


    public FileList filenames = new FileList();

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

    private String readLineByLineJava8(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }



    public boolean addFile(String name, String content) throws IOException {

        String filepath = "C:\\Users\\Yuriy Volkovskiy\\Desktop\\File Server\\File Server\\task\\src\\client\\data\\" + name;
        java.io.File f = new java.io.File(filepath);
        if (f.exists() && !f.isDirectory()) {
            filenames.addFile(content, readLineByLineJava8(filepath));

            if (content.length() == 0) {
                content = "noName.txt";
            }

            try (BufferedOutputStream bf = new BufferedOutputStream(new FileOutputStream("C:\\Users\\Yuriy Volkovskiy\\Desktop\\File Server\\File Server\\task\\src\\server\\data\\" + content))) {
                byte[] array = readLineByLineJava8(filepath).getBytes();
                bf.write(array);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

            saveDatabase();
            return true;
        } else {
            return false;
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