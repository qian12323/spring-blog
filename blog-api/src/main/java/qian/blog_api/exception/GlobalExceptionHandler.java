// src/main/java/qian/blog_api/exception/GlobalExceptionHandler.java
package qian.blog_api.exception;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import qian.blog_api.model.Result;

/**
 * 全局异常处理器：统一处理所有异常，返回Result格式
 */
@ControllerAdvice // 拦截所有Controller层的异常
@ResponseBody // 确保返回JSON
public class GlobalExceptionHandler {

    /**
     * 处理自定义业务异常（最常用）
     */
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        // 直接使用异常中携带的错误码和消息
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常（如@NotBlank、@Min等注解验证失败）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidationException(MethodArgumentNotValidException e) {
        // 提取参数校验失败的消息（如“用户名不能为空”）
        BindingResult bindingResult = e.getBindingResult();
        FieldError firstError = bindingResult.getFieldError();
        String errorMsg = firstError != null ? firstError.getDefaultMessage() : "参数校验失败";
        return Result.fail(400, errorMsg); // 400表示参数错误
    }

    /**
     * 处理系统异常（如NullPointerException、SQL异常等）
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleSystemException(Exception e) {
        // 系统异常通常不暴露详细信息，返回通用提示
        return Result.fail(500, "服务器内部错误，请稍后再试");
    }
}