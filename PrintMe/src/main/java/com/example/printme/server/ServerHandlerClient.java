package com.example.printme.server;

import com.example.printme.helpers.CanvasState;
import com.example.printme.helpers.ChatRoom;
import com.example.printme.helpers.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerHandlerClient implements Runnable{
    private ChatRoom clientRoom;
    private Socket socet;
    private String username = "";
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    public ObjectOutputStream getWriter() {
        return writer;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }

    public void setClientRoom(ChatRoom clientRoom) {
        this.clientRoom = clientRoom;
    }

    public ChatRoom getClientRoom() {
        return clientRoom;
    }
    public ObjectInputStream getReader(){
        return reader;
    }

    public ServerHandlerClient(Socket clientSocket) {
        try {
            this.socet = clientSocket;
            this.writer = new ObjectOutputStream(clientSocket.getOutputStream());
            this.reader = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {
            while (true) {
                Object o = reader.readObject();
                if(o instanceof Message) {
                    Message message = (Message) o;
                    if (message == null || message.equals("/exit")) {
                        continue;
                    } else {
                        clientRoom.broadcastMessage(message);
                    }
                }else{
                    CanvasState canvasState = (CanvasState) o;
                    if(canvasState == null){
                        continue;
                    }else{
                        clientRoom.broadcastCanvasState(canvasState);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendMessage(Message message) throws IOException {
        writer.writeObject(message);
        System.out.println("sending message in room" + clientRoom);
    }
    public void sentRole(String role) throws IOException{
        writer.writeObject(role);
        System.out.println("role is send");
    }

    public void sendCanvasState(CanvasState canvasState) throws IOException{
        writer.writeObject(canvasState);
        System.out.println("sending canvas state in room" + clientRoom);
    }
}
