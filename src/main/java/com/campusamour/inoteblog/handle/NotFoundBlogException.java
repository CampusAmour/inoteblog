package com.campusamour.inoteblog.handle;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;


public class NotFoundBlogException extends RuntimeException {
    public NotFoundBlogException() {
    }

    public NotFoundBlogException(String message) {
        super(message);
    }

    public NotFoundBlogException(String message, Throwable cause) {
        super(message, cause);
    }
}


/*
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundBlogException extends RuntimeException {
    public NotFoundBlogException() {
    }

    public NotFoundBlogException(String message) {
        super(message);
    }

    public NotFoundBlogException(String message, Throwable cause) {
        super(message, cause);
    }
}
*/
