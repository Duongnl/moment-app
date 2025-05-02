package com.example.moment_app.models.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    private String oldPassword; // Mật khẩu cũ
    private String newPassword; // Mật khẩu mới

    public ChangePasswordRequest() {
    }
}
