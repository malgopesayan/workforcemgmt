package com.example.workforcemgmt.common.model.enums.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

// All Lombok annotations have been removed and replaced with manual code
// to ensure this class compiles correctly without relying on build tool "magic".

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ResponseStatus {

    private Integer code;
    private String message;

    /**
     * A no-argument constructor.
     * This is required by frameworks like Jackson for converting JSON to Java objects.
     */
    public ResponseStatus() {
    }

    /**
     * A constructor that takes all arguments.
     * This is the specific constructor that your CustomExceptionHandler needs.
     * By writing it here ourselves, we guarantee that the compiler can find it.
     */
    public ResponseStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    // --- Manually written Getters and Setters (replaces @Data) ---

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}