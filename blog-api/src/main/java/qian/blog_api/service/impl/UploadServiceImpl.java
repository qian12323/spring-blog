package qian.blog_api.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import qian.blog_api.service.UploadService;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 上传服务实现类
 */
@Service
public class UploadServiceImpl implements UploadService {

    // 从配置文件读取上传路径
    @Value("${upload.image.path}")
    private String uploadPath;

    // 图片访问基础URL
    @Value("${upload.image.base-url}")
    private String imageBaseUrl;

    @Override
    public String uploadImage(MultipartFile file) throws IOException, IllegalArgumentException {
        // 校验文件是否为空
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        // 校验文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("请上传图片类型文件");
        }

        // 确保上传目录存在
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            // 递归创建目录（包括父目录）
            boolean isCreated = uploadDir.mkdirs();
            if (!isCreated) {
                throw new IOException("无法创建上传目录，请检查权限");
            }
        }

        // 生成唯一文件名（避免重复）
        String originalFilename = file.getOriginalFilename();
        String fileExt = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
        String fileName = UUID.randomUUID().toString() + fileExt;

        // 保存文件
        File destFile = new File(uploadPath + File.separator + fileName);
        file.transferTo(destFile);

        // 返回图片访问URL
        return imageBaseUrl + "/" + fileName;
    }
}