package com.example.yourcall;

public class user_data {
    private String email;
    private String pass;
    private String repass;

    public user_data() {
        // Default constructor required for Firebase
}
public user_data(String email,String pass,String repass){
        this.email=email;
        this.pass=pass;
        this.repass=repass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRepass() {
        return repass;
    }

    public void setRepass(String repass) {
        this.repass = repass;
    }
}
