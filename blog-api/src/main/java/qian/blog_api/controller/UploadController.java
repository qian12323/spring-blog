package qian.blog_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import qian.blog_api.model.Result;
import qian.blog_api.model.dto.ImageUploadDTO;
import qian.blog_api.service.UploadService;

import java.io.IOException;

/**
 * 图片上传控制器
 * 处理图片上传相关接口
 */
@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    /**
     * 图片上传接口（需登录）
     * 前端通过form-data形式提交，字段名为"image"
     */
    @PostMapping("/image")
    public Result<String> uploadImage(ImageUploadDTO imageUploadDTO) {
        try {
            MultipartFile image = imageUploadDTO.getImage();
            // 调用服务层处理上传逻辑，返回图片访问URL
            String imageUrl = uploadService.uploadImage(image);
            return Result.success("图片上传成功", imageUrl);
        } catch (IOException e) {
            return Result.error("图片上传失败：" + e.getMessage());
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        }
    }
}