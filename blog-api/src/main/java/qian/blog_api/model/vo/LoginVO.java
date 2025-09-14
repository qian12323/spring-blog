package qian.blog_api.model.vo;

import lombok.Data;

/**
 * 登录响应VO
 */
@Data
public class LoginVO {
    private Integer userId; // 用户ID
    private String username; // 用户名
    private String token; // 认证token
    private Integer expiresIn; // token有效期（秒）
}
    