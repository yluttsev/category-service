package ru.example.categoryservice.controller;

import jakarta.persistence.EntityExistsException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.example.categoryservice.exception.NotFoundException;
import ru.example.categoryservice.model.payload.MessageResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public MessageResponse notFound(NotFoundException e) {
        return new MessageResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public MessageResponse validationError(ConstraintViolationException e) {
        String validationErrorMessage = e.getConstraintViolations().iterator().next().getMessage();
        return new MessageResponse(validationErrorMessage);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(EntityExistsException.class)
    public MessageResponse categoryAlreadyExists(EntityExistsException e) {
        return new MessageResponse(e.getMessage());
    }
}
