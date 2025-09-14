package qian.blog_api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import qian.blog_api.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

     // 注入上传路径配置
     @Value("${upload.image.path}")
     private String uploadImagePath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                // 需要拦截的接口（例如所有需要登录的接口）
                .addPathPatterns(
                    "/api/blogs/update/{id}",
                    "/api/blogs/create",
                    "/api/blogs/delete/{id}",
                    "/api/categories/update/{id}",
                    "/api/categories/create",
                    "/api/categories/delete/{id}",
                    "/api/upload/image"
                )
                // 放行的接口（登录、注册、公开接口等）
                .excludePathPatterns(
                        "/api/auth/login",    // 登录接口
                        "/api/auth/verify",   // Token 验证接口（可选）
                        "/api/blogs" ,    //所有的博文列表
                        "/api/blogs/{id}",
                        "/api/categories",
                        "api/blogs/category/{categoryid}",
                        "/images/**"
                );
    }

    // 添加静态资源映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将 /images/** 路径映射到实际的图片存储目录
        // 开发环境：使用项目相对路径
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + uploadImagePath + "/");
    }
}