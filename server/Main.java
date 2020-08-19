package server;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public class Main {

    private static Database database = new Database();

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(23456)) {
            while (true) {
                try (Socket socket = server.accept(); // accepting a new client
                     DataInputStream input = new DataInputStream(socket.getInputStream());
                     DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
                    String msg = input.readUTF(); // reading a message
                    if ("exit".equals(msg)) {
                        break;
                    } else {
                        System.out.println(msg);
                        if (msg.startsWith("PUT")) {
                            String[] request = msg.split("~");

                            if (request.length == 2) {
                                String name = request[1];
                                request = new String[3];
                                request[1] = name;
                                request[2] = "";
                            }

                            System.out.println(Arrays.toString(request));


                            if (database.addFile(request[1], request[2])) {
                                try (BufferedOutputStream bf = new BufferedOutputStream(new FileOutputStream("C:\\Users\\Yuriy Volkovskiy\\Desktop\\File Server\\File Server\\task\\src\\server\\data\\" + request[1]))) {
                                    byte[] array = request[2].getBytes();
                                    bf.write(array);
                                } catch (IOException e) {
                                    System.err.println(e.getMessage());
                                }

                                Database.File fileContent = database.getFile(request[1], "1").getKey();

                                output.writeUTF("Response says that file is saved! ID = " + fileContent.getHashCode());
                            } else {
                                output.writeUTF("Ok, the response says that create file was forbidden!");
                            }

                            System.out.println(database);
                        } else if (msg.startsWith("GET")) {
                            String[] request = msg.split(" ");



                            if (!database.getFile(request[1], request[2]).getValue() == false) {
                                Database.File fileContent = database.getFile(request[1], request[2]).getKey();
                                try (BufferedOutputStream bf = new BufferedOutputStream(new FileOutputStream("C:\\Users\\Yuriy Volkovskiy\\Desktop\\File Server\\File Server\\task\\src\\client\\data\\" + fileContent.getName()))) {
                                    byte[] array = fileContent.getContent().getBytes();
                                    bf.write(array);
                                } catch (IOException e) {
                                    System.err.println(e.getMessage());
                                }
                                output.writeUTF("Ok, the content of the file is: " + fileContent.getContent());
                            } else {
                                output.writeUTF("The response says that this file is not found!");
                            }
                        } else if (msg.startsWith("DELETE")) {
                            String[] request = msg.split(" ");

                            if (database.removeFile(request[1], request[2])) {
                                output.writeUTF("Ok, the response says that the file was successfully deleted!");
                            } else {
                                output.writeUTF("Ok, the response says that the file was not found!");
                            }
                        }
                    }
                    //  output.writeUTF(handler(msg));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*private static class Session extends Thread {

        private final int id;
        private final Socket socket;

        public Session(Socket socket, int id) {
            this.socket = socket;
            this.id = id;
        }

        @Override
        public void run() {
            try () {

                loop:
                while (!this.isInterrupted()) {
                    try {
                        String msg = input.readUTF();
                        if ("exit".equals(msg)) {
                            break loop;
                        } else {
                         //   System.out.printf("Client %d sent: %s\n", id, msg);
                           // int wordsCount = msg.split("\\s+").length;
                         //   output.writeUTF("Count is " + wordsCount);

                            System.out.println(msg);

                            if (msg.startsWith("PUT")) {

                                String[] request = msg.split("-");


                                if (database.addFile(request[1], request[2])) {

                                    output.writeUTF("Ok, the response says that the file was created!");
                                } else {
                                    output.writeUTF("Ok, the response says that creating the file was forbidden!");
                                }

                                System.out.println(database);
                            } else if (msg.startsWith("GET")) {
                                String[] request = msg.split(" ");


                                if (!database.getFile(request[1]).getValue() == false) {
                                    output.writeUTF("Ok, the content of the file is: " + database.getFile(request[1]).getKey());
                                } else {
                                    output.writeUTF("Ok, the response says that the file was not found!");
                                }
                            } else if (msg.startsWith("DELETE")) {
                                String[] request = msg.split(" ");


                                if (database.removeFile(request[1])) {
                                    output.writeUTF("Ok, the response says that the file was successfully deleted!");
                                } else {
                                    output.writeUTF("Ok, the response says that the file was not found!");
                                }
                            } else {


                            }

                            System.out.println(database);
                         //   System.out.printf("Sent to client %d: Count is %d\n", id, wordsCount);
                        }
                    } catch (IOException e) {
                        break;
                    }
                }
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
}