package com.example.moment_app.models.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

//    Tra ve thong tin ca nhan

    String id;
    String userName;
    String email;
    String phoneNumber;
    String createdAt;
    String name;
    String birthday;
    String address;
    String urlPhoto;
    int status;

    public UserResponse() {
    }

    public UserResponse(String id, String userName, String email, String phoneNumber, String createdAt, String name, String birthday, String address, String urlPhoto, int status) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.name = name;
        this.birthday = birthday;
        this.address = address;
        this.urlPhoto = urlPhoto;
        this.status = status;
    }
}
