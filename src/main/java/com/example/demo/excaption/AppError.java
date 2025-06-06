package com.example.demo.excaption;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppError {
    private int statusCode;
    private String message;

    public AppError() {}

    public AppError(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
