package qian.blog_api.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qian.blog_api.model.Result;
import qian.blog_api.model.dto.LoginDTO;
import qian.blog_api.model.vo.LoginVO;
import qian.blog_api.model.vo.TokenVerifyVO;
import qian.blog_api.service.UserService;

/**
 * 用户认证控制器
 * 处理登录、token验证等接口
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录接口
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = userService.login(loginDTO);
        return Result.success("登录成功", loginVO);
    }

    /**
     * 验证token有效性接口
     */
    @GetMapping("/verify")
    public Result<TokenVerifyVO> verifyToken(@RequestHeader("Authorization") String authorization) {
        // 从Authorization头中提取token（格式：Bearer token）
        String token = authorization.replace("Bearer ", "").trim();
        TokenVerifyVO verifyVO = userService.verifyToken(token);

        // 新增：如果token无效，返回错误状态
        if (!verifyVO.getValid()) {
            return Result.error("无效的token或token已过期");
        }
        return Result.success(verifyVO);
    }
}
    