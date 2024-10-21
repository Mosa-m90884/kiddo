package com.example.kiddo.Model;


public class UserInAdminPanel {
    private String id;
    private String username;
    private String email;

    public UserInAdminPanel() {
        // مطلوب لفايرستور
    }

    public UserInAdminPanel(String id, String name, String email) {
        this.id = id;
        this.username = name;
        this.email = email;
    }

    // getters and setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}