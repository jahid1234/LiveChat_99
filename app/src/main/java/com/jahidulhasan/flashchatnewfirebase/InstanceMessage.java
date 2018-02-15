package com.jahidulhasan.flashchatnewfirebase;

/**
 * Created by Jahid on 1/10/2018.
 */

public class InstanceMessage  {

    private String message;
    private String sender;

    public InstanceMessage() {
    }

    public InstanceMessage(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }
}
