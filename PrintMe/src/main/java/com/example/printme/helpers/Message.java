package com.example.printme.helpers;

import java.io.Serializable;

public class Message implements Serializable {
    public String text;
    public String userName;

    public Message(String text, String userName) {
        this.text = text;
        this.userName = userName;
    }
}
