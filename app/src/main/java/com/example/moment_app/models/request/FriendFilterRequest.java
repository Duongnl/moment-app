package com.example.moment_app.models.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendFilterRequest {
    int pageCurrent;

    String time;

    public FriendFilterRequest(int pageCurrent, String time) {
        this.pageCurrent = pageCurrent;
        this.time = time;
    }
}
