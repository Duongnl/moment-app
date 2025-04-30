package com.example.moment_app.models.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
    /*
    * Class nay tra ve thong tin luc login
    * Muc dich de nguoi dung biet co dang nhap thanh cong hay khong
    * */

    String token;
    boolean authenticated;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(String token, boolean authenticated) {
        this.token = token;
        this.authenticated = authenticated;
    }
}

