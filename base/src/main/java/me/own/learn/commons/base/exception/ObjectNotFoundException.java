package me.own.learn.commons.base.exception;

import org.springframework.http.HttpStatus;

public abstract class ObjectNotFoundException extends BusinessException {
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
