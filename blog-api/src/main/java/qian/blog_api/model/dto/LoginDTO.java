package qian.blog_api.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 登录请求参数DTO
 * 对应接口：POST /api/auth/login
 */
@Data
public class LoginDTO {

    /**
     * 用户名
     * 非空校验：不能为空
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     * 非空校验：不能为空
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 是否记住我
     * 默认值：false
     */
    private Boolean rememberMe = false;
}
    