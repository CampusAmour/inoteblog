package com.campusamour.inoteblog.handle;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundVerifyCodeException extends RuntimeException {
    public NotFoundVerifyCodeException() {
    }

    public NotFoundVerifyCodeException(String message) {
        super(message);
    }

    public NotFoundVerifyCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
