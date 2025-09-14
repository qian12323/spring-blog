package qian.blog_api.model;

import lombok.Data;

/**
 * 统一响应结果类
 * 所有接口返回数据都通过此类封装
 */
@Data
public class Result<T> {

    /**
     * 状态码：0-成功，非0-失败
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    // 成功响应（无数据）
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        return result;
    }

    // 成功响应（带数据）
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    // 成功响应（带消息和数据）
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    // 失败响应（带错误码和消息)
    public static <T> Result<T> fail(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    // 新增：简化的失败响应（默认错误码1，只传消息）
    public static <T> Result<T> error(String message) {
        return fail(401, message);
    }
    // 新增资源不存在方法
    public static <T> Result<T> notFound(String message) {
        return fail(404, message);
    }

}
    