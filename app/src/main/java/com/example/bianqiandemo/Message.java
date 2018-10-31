package com.example.bianqiandemo;

public class Message {
    private String text;
    private String time;

    public Message(String text,String time) {
        this.text=text;
        this.time=time;
    }

    public String getText() {

        return text;
    }

    public String getTime() {

        return time;
    }
}
