package qian.blog_api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import qian.blog_api.model.entity.User;
import qian.blog_api.model.dto.LoginDTO;
import qian.blog_api.model.vo.LoginVO;
import qian.blog_api.model.vo.TokenVerifyVO;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户实体
     */
    User getUserByUsername(String username);

    /**
     * 用户登录验证（返回LoginVO）
     * @param loginDTO 登录请求参数（包含用户名和密码）
     * @return 登录结果VO（包含token等信息）
     */
    LoginVO login(LoginDTO loginDTO); // 修正：返回类型从User改为LoginVO

    /**
     * 验证token有效性（新增方法）
     * @param token 前端传递的token
     * @return 验证结果VO
     */
    TokenVerifyVO verifyToken(String token);
}
    