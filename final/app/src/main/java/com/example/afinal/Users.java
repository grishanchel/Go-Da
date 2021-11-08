package com.example.afinal;

import com.google.firebase.firestore.auth.User;

public class Users {

    private String docId, uid, email, password, avatar;

    public Users (String docId, String uid, String email, String password, String avatar) {
        this.docId = docId;
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
    }

    public void setUser (Users user) {
        this.docId = user.docId;
        this.uid = user.uid;
        this.email = user.email;
        this.password = user.password;
        this.avatar = user.avatar;

    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}