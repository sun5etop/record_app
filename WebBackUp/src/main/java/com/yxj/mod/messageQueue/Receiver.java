package com.yxj.mod.messageQueue;

import org.springframework.stereotype.Component;

@Component
public class Receiver {

    private String lastMessage;

    public void receiveMessage(String message) {
        this.lastMessage = message;
        System.out.println("Received <" + message + ">");
    }

    public String getLastMessage() {
        return lastMessage;
    }
}