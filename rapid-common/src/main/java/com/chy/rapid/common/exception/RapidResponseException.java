
package com.chy.rapid.common.exception;

import com.chy.rapid.common.enums.ResponseCode;

/**
 * 所有的响应异常基础定义
 */
public class RapidResponseException extends RapidBaseException {

    private static final long serialVersionUID = -5658789202509039759L;

    public RapidResponseException() {
        this(ResponseCode.INTERNAL_ERROR);
    }

    public RapidResponseException(ResponseCode code) {
        super(code.getMessage(), code);
    }

    public RapidResponseException(Throwable cause, ResponseCode code) {
        super(code.getMessage(), cause, code);
        this.code = code;
    }

}
