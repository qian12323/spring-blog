package qian.blog_api.model.vo;

import lombok.Data;

/**
 * Token验证VO（用于/api/auth/verify接口）
 */
@Data
public class TokenVerifyVO {
    private Boolean valid; // token是否有效
    private String username; // 用户名（token有效时返回）
}
    