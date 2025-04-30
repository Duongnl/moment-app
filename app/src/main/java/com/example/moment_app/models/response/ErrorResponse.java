package com.example.moment_app.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
// An nhung bien co  gi tri la null di
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    String code;
    String message;

    public ErrorResponse() {
    }

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
