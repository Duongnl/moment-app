package com.example.moment_app.models.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {

    String name;

    String birthday;

    String sex;

    String userName;

    String password;

    public RegisterRequest() {
    }

    public RegisterRequest(String name, String birthday, String sex, String userName, String password) {
        this.name = name;
        this.birthday = birthday;
        this.sex = sex;
        this.userName = userName;
        this.password = password;
    }
}
