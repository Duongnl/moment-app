package com.example.moment_app.models.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileResponse {

    int id;
    String idAccount;
    String userName;
    String name;
    List<PhotoResponse> listPhotoResponseProfile;
    String urlAvt;
    int quantityFriend;
    private String friendStatus;

    public ProfileResponse() {
    }

    public ProfileResponse(int id, String idAccount, String userName, String name, List<PhotoResponse> listPhotoResponseProfile, String urlAvt, int quantityFriend, String friendStatus) {
        this.id = id;
        this.idAccount = idAccount;
        this.userName = userName;
        this.name = name;
        this.listPhotoResponseProfile = listPhotoResponseProfile;
        this.urlAvt = urlAvt;
        this.quantityFriend = quantityFriend;
        this.friendStatus = friendStatus;
    }
}
