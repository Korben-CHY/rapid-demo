package com.chy.rapid.common.exception;

import com.chy.rapid.common.enums.ResponseCode;

/**
 * 网关最基础的异常定义类
 */
public class RapidBaseException extends RuntimeException {

    private static final long serialVersionUID = -5658789202563433456L;
    
    public RapidBaseException() {
    }

    protected ResponseCode code;

    public RapidBaseException(String message, ResponseCode code) {
        super(message);
        this.code = code;
    }

    public RapidBaseException(String message, Throwable cause, ResponseCode code) {
        super(message, cause);
        this.code = code;
    }

    public RapidBaseException(ResponseCode code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public RapidBaseException(String message, Throwable cause, 
    		boolean enableSuppression, boolean writableStackTrace, ResponseCode code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }
    
    public ResponseCode getCode() {
        return code;
    }

}
