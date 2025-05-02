package com.example.moment_app.models.request;

import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
public class AccountInfoRequest {
    private String name;
    private String userName;
    private String birthday;
    private String sex;

    public AccountInfoRequest() {
    }
}
