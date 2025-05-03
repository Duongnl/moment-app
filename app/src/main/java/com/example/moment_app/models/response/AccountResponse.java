package com.example.moment_app.models.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter

@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse {
    String name;

    String urlPhoto;

    String urlProfile;

    String friendStatus;

    String requestedAt;

    String id;
}
