package com.miner.recruiteer.Class;

import java.io.Serializable;

public class LogIn implements Serializable {
    String email;
    String password;

    public LogIn(String email, String password) {
        this.email = email;
        this.password = password;
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
}
