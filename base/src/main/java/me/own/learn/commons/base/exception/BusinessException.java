package me.own.learn.commons.base.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务型异常基类，通常由于参数传递错误、配置不得当导致的异常
 * 子类需要自行设置自己的错误提示
 * @author Christopher.Wang
 */
public abstract class BusinessException extends RuntimeException {
    /**
     * 错误代码
     * @return
     */
    public abstract String getErrorCode();

    /**
     * 错误信息
     * @return
     */
    public abstract String getErrorMsg();

    /**
     * 如果需要返回至api层，所对应的http状态码
     * @return
     */
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    /**
     * 附件信息
     * @return
     */
    public Object getAttachment () {
        return null;
    }

    public final ResponseEntity getResponseEntity() {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error_code", this.getErrorCode());
        responseBody.put("error_msg", this.getErrorMsg());
        if (null != this.getAttachment()) {
            responseBody.put("attachment", this.getAttachment());
        }
        return new ResponseEntity(responseBody, this.getHttpStatus());
    }
}
