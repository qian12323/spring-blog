// src/main/java/qian/blog_api/exception/BusinessException.java
package qian.blog_api.exception;

import lombok.Data;

/**
 * 自定义业务异常（用于业务逻辑错误）
 */
@Data
public class BusinessException extends RuntimeException {
    // 错误码（如1001：用户名不存在）
    private Integer code;
    // 错误消息
    private String message;

    // 构造方法：传入错误码和消息
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}