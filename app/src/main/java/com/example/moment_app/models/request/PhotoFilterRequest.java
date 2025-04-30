package com.example.moment_app.models.request;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhotoFilterRequest {
    int pageCurrent;

    String time;

    public PhotoFilterRequest() {
    }

    public PhotoFilterRequest(int pageCurrent, String time) {
        this.pageCurrent = pageCurrent;
        this.time = time;
    }
}
