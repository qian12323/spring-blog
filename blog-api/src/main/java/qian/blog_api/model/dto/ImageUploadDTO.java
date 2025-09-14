package qian.blog_api.model.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片上传参数DTO
 * 对应接口：POST /api/upload/image
 */
@Data
public class ImageUploadDTO {

    /**
     * 图片文件
     * 前端通过form-data传递，字段名为"image"
     */
    private MultipartFile image;
}
    