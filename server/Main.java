package server;

import java.io.*;
import java.net.*;

public class Main {

    private static Database database = new Database();

    public static void main(String[] args) {


        try (ServerSocket server = new ServerSocket(23456)) {
           // System.out.println("Server started!");
            int idCounter = 0;
           // server.setSoTimeout(200);
            while (true) {
                try {
                    new Session(server.accept(), ++idCounter).start();
                } catch (SocketTimeoutException e) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class Session extends Thread {

        private final int id;
        private final Socket socket;

        public Session(Socket socket, int id) {
            this.socket = socket;
            this.id = id;
        }

        @Override
        public void run() {
            try (DataInputStream input = new DataInputStream(socket.getInputStream());
                 DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

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
    }
}