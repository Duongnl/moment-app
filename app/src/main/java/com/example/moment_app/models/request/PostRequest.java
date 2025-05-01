package com.example.moment_app.models.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostRequest {

    private String url;

    private String caption;
}
