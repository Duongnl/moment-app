package com.example.moment_app.models.response;

import java.time.LocalDateTime;

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
public class PhotoResponse {
    private Integer id;
    private String urlPhoto;
    private String caption;
    private LocalDateTime createdAt;
    private String slug;
    private String name;
    private String urlAvt;
    private String userName;

    public PhotoResponse() {
    }

    public PhotoResponse(Integer id, String urlPhoto, String caption, LocalDateTime createdAt, String slug, String name, String urlAvt, String userName) {
        this.id = id;
        this.urlPhoto = urlPhoto;
        this.caption = caption;
        this.createdAt = createdAt;
        this.slug = slug;
        this.name = name;
        this.urlAvt = urlAvt;
        this.userName = userName;
    }
}
