package com.example.moment_app.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;



import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
// An nhung bien co  gi tri la null di
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse <T>{
    /*
    * Class nay de dinh dang quy uoc tra ve cho nguoi dung
    * */

    Integer status;

    public ApiResponse() {
    }

    public ApiResponse(Integer status, String message, T result, List<ErrorResponse> errors, long timestamp, Integer totalPages, Integer totalItems, Integer currentPage) {
        this.status = status;
        this.message = message;
        this.result = result;
        this.errors = errors;
        this.timestamp = timestamp;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.currentPage = currentPage;
    }


    String message;
    T result;
    List<ErrorResponse> errors;

    long timestamp = System.currentTimeMillis();

    // Phan trang
    Integer totalPages;
    Integer totalItems;
    Integer currentPage;


}


