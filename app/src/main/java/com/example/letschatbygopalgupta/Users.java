package com.example.letschatbygopalgupta;

public class Users {
    String profiledp,mail,userName,password,userId,lastMessage,status;

    public Users(String id, String namee, String emaill, String passs, String repasss, String imageUri, String statuss){}

    public String getProfiledp() {
        return profiledp;
    }

    public void setProfiledp(String profiledp) {
        this.profiledp = profiledp;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
