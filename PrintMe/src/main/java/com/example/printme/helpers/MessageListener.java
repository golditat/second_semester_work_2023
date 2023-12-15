package com.example.printme.helpers;

import java.io.IOException;

public interface MessageListener {
    void onMessageAdded(String sender, String message) throws IOException;
    void onMessageGetting(Message message) throws IOException;
    void onCanvasStateAdded(CanvasState canvasState) throws IOException;
    void onCanvasStateGetting(CanvasState canvasState) throws IOException;
}
