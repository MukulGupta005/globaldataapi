package com.app.countrydata.Exceptions;

import com.app.countrydata.ApiResponse.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<String> handleHttpClientErrorException(HttpClientErrorException ex) {
        return new ResponseEntity<>("Enter the correct country name: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<List<ApiResponse>> handleGeneralException(Exception ex) {
        List<ApiResponse> errorResponses = new ArrayList<>();
        ApiResponse errorResponse = new ApiResponse("An error occurred: kindly check your URL " + ex.getMessage(), false, "This is the base URL http://localhost:8080/api/");
        errorResponses.add(errorResponse);
        return new ResponseEntity<>(errorResponses, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse> handleResponseStatusException(ResponseStatusException ex) {
        ApiResponse errorResponse = new ApiResponse("An error occurred: " + ex.getReason(), false, "This is the base URL http://localhost:8080/api/");
        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiResponse errorResponse = new ApiResponse("Invalid argument: " + ex.getMessage(), false, "This is the base URL http://localhost:8080/api/");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse> handleNullPointerException(NullPointerException ex) {
        ApiResponse errorResponse = new ApiResponse("Null pointer exception: " + ex.getMessage(), false, "This is the base URL http://localhost:8080/api/");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
