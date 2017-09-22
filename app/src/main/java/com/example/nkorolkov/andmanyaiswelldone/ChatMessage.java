package com.example.nkorolkov.andmanyaiswelldone;

public class ChatMessage {

    private String text;
    private String name;
    private String photoUrl;
    private String description;

    public ChatMessage() {
    }

    public ChatMessage(String text, String description, String name, String photoUrl) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.description = description;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
}
