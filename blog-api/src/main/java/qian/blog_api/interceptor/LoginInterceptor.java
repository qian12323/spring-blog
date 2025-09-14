package qian.blog_api.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import qian.blog_api.model.Result;
// import qian.blog_api.service.UserService;
import qian.blog_api.util.JwtUtil;
import com.alibaba.fastjson.JSONObject;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    // @Autowired
    // private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取请求头中的 Authorization
        String authorization = request.getHeader("Authorization");

        // 2. 检查 Token 是否存在
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            returnJson(response, Result.error("未登录，请先登录"));
            return false;
        }

        // 3. 提取 Token（去掉 "Bearer " 前缀）
        String token = authorization.replace("Bearer ", "").trim();

        // 4. 验证 Token 有效性
        try {
            // 从 Token 中解析用户名
            String username = jwtUtil.extractUsername(token);
            // 验证 Token 是否有效（与用户信息匹配且未过期）
            if (!jwtUtil.validateToken(token, username)) {
                returnJson(response, Result.error("登录已过期，请重新登录"));
                return false;
            }
            // 5. Token 有效，放行
            return true;
        } catch (Exception e) {
            // Token 解析失败（无效 Token）
            returnJson(response, Result.error("无效的登录信息，请重新登录"));
            return false;
        }
    }

    // 向客户端返回 JSON 格式的错误信息
    private void returnJson(HttpServletResponse response, Result<?> result) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(JSONObject.toJSONString(result));
    }
}