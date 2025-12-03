package org.allbluehu.mse.auth.dto;

import lombok.Data;

@Data
public class ApiResult<T> {
    private boolean success;
    private String message;
    private T data;
    private Long timestamp;
    
    public ApiResult() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public static <T> ApiResult<T> success(String message) {
        ApiResult<T> result = new ApiResult<>();
        result.setSuccess(true);
        result.setMessage(message);
        return result;
    }
    
    public static <T> ApiResult<T> success(String message, T data) {
        ApiResult<T> result = new ApiResult<>();
        result.setSuccess(true);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
    
    public static <T> ApiResult<T> failure(String message) {
        ApiResult<T> result = new ApiResult<>();
        result.setSuccess(false);
        result.setMessage(message);
        return result;
    }
}