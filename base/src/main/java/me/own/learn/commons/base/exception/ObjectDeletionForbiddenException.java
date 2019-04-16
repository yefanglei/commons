package me.own.learn.commons.base.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by CN-LEBOOK-YANGLI on 2017/2/28.
 */
public abstract class ObjectDeletionForbiddenException extends BusinessException {
    public HttpStatus getHttpStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
