package qian.blog_api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qian.blog_api.mapper.UserMapper;
import qian.blog_api.model.dto.LoginDTO;
import qian.blog_api.model.entity.User;
import qian.blog_api.model.vo.LoginVO;
import qian.blog_api.model.vo.TokenVerifyVO;
import qian.blog_api.service.UserService;
import qian.blog_api.util.JwtUtil;
import qian.blog_api.exception.BusinessException;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public LoginVO login(LoginDTO loginDTO) { // 修正：返回类型改为LoginVO
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();

        
        // 1. 查询用户
        User user = getUserByUsername(username);
        if (user == null) {
            throw new BusinessException(1001, "用户不存在"); // 后续可改为自定义异常
        }

        // 2. 验证密码
        if (!password.equals(user.getPassword())) { // 实际项目需加密比对
            throw new BusinessException(1002, "密码错误");
        }

        // 2.5 生成Token
        String token = jwtUtil.generateToken(user.getUsername());

        // 3. 转换为LoginVO（封装返回给前端的数据）
        LoginVO loginVO = new LoginVO();
        loginVO.setUserId(user.getId());
        loginVO.setUsername(user.getUsername());
        loginVO.setToken("Bearer "+token);
        loginVO.setExpiresIn(3600); // 有效期1小时（秒）
        return loginVO;
    }

    @Override
    public TokenVerifyVO verifyToken(String token) {
        TokenVerifyVO verifyVO = new TokenVerifyVO();
        try {
            // 从token中提取用户名
            String username = jwtUtil.extractUsername(token);
            // 验证token有效性
            if (jwtUtil.validateToken(token, username)) {
                verifyVO.setValid(true);
                verifyVO.setUsername(username);
            } else {
                verifyVO.setValid(false);
            }
        } catch (Exception e) {
            // 解析失败或验证失败均视为无效token
            verifyVO.setValid(false);
            // System.err.println(e.getMessage());
        }
        return verifyVO;
    }
}
    