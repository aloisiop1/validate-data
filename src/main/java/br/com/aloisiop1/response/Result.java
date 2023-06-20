package br.com.aloisiop1.response;

import jakarta.validation.ConstraintViolation;

import java.util.Set;
import java.util.stream.Collectors;

public class Result {
    private String message;
    private boolean success;

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public Result(String message) {
        this.message = message;
        this.success=true;
    }

    public Result(Set<? extends ConstraintViolation<?>> violations){
        this.success = true;
        this.message = violations.stream()
                .map( cv -> cv.getMessage())
                .collect(Collectors.joining(", "));
    }

}
