package com.example.healthkit;

public class chat_message_obj {
    public String message,username;
    public chat_message_obj(){

    }
    public chat_message_obj(String message, String username){
        this.message = message;
        this.username=username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
