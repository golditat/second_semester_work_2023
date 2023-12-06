package com.example.printme.server;

import com.example.printme.helpers.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Server {

    /* Setting up variables */
    private static final int PORT = 9001;
    private static final HashMap<String, String> names = new HashMap<>();
    private static HashSet<ObjectOutputStream> writers = new HashSet<>();
    private static ArrayList<String> users = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            listener.close();
        }
    }


    private static class Handler extends Thread {
        private String name;
        private Socket socket;
        private String user;
        private ObjectInputStream input;
        private OutputStream os;
        private ObjectOutputStream output;
        private InputStream is;

        public Handler(Socket socket) throws IOException {
            this.socket = socket;
        }

        public void run() {
            System.out.println("Attempting to connect a user...");
            try {
                is = socket.getInputStream();
                input = new ObjectInputStream(is);
                os = socket.getOutputStream();
                output = new ObjectOutputStream(os);

                Message firstMessage = (Message) input.readObject();
                checkDuplicateUsername(firstMessage);
                writers.add(output);
                sendNotification(firstMessage);
                addToList();

                while (socket.isConnected()) {
                    Message inputmsg = (Message) input.readObject();
                    if (inputmsg != null) {
                        System.out.println(inputmsg.userName + " - " + inputmsg.text);
                    }
                }
            } catch (SocketException socketException) {
                System.out.println("Socket Exception for user " + name);
            }catch (Exception e){
                System.out.println("Exception in run() method for user: " + name);
            } finally {
                closeConnections();
            }
        }

        private synchronized void checkDuplicateUsername(Message firstMessage) {
            System.out.println(firstMessage.userName + " is trying to connect");
            if (!names.containsKey(firstMessage.userName)) {
                users.add(user);
                System.out.println(name + " has been added to the list");
            } else {
                System.out.println(firstMessage.userName + " is already connected");
            }
        }

        private Message sendNotification(Message firstMessage) throws IOException {
            Message msg = new Message();
            msg.text = "has joined the chat.";
            msg.userName=firstMessage.userName;
            write(msg);
            return msg;
        }


        private Message removeFromList() throws IOException {
            System.out.println("removeFromList() method Enter");
            Message msg = new Message();
            msg.text="has left the chat.";
            msg.userName = "SERVER";
            //msg.setUserlist(names);
            write(msg);
            System.out.println("removeFromList() method Exit");
            return msg;
        }

        /*
         * For displaying that a user has joined the server
         */
        private Message addToList() throws IOException {
            Message msg = new Message();
            msg.text="Welcome, You have now joined the server! Enjoy chatting!";
            msg.userName ="SERVER";
            write(msg);
            return msg;
        }

        /*
         * Creates and sends a Message type to the listeners.
         */
        private void write(Message msg) throws IOException {
            for (ObjectOutputStream writer : writers) {
                //msg.setUserlist(names);
                //msg.setUsers(users);
                //msg.setOnlineCount(names.size());
                writer.writeObject(msg);
                writer.reset();
            }
        }

        /*
         * Once a user has been disconnected, we close the open connections and remove the writers
         */
        private synchronized void closeConnections()  {
            System.out.println("closeConnections() method Enter");
            System.out.println("HashMap names:" + names.size() + " writers:" + writers.size() + " usersList size:" + users.size());
            if (name != null) {
                names.remove(name);
                System.out.println("User: " + name + " has been removed!");
            }
            if (user != null){
                users.remove(user);
                System.out.println("User object: " + user + " has been removed!");
            }
            if (output != null){
                writers.remove(output);
                System.out.println("Writer object: " + user + " has been removed!");
            }
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                removeFromList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("HashMap names:" + names.size() + " writers:" + writers.size() + " usersList size:" + users.size());
            System.out.println("closeConnections() method Exit");
        }
    }
}
