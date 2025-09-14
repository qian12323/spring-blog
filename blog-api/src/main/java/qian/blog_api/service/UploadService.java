package qian.blog_api.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * 上传服务接口
 */
public interface UploadService {

    /**
     * 上传图片文件
     * @param file 图片文件
     * @return 图片访问URL
     * @throws IOException  IO异常
     * @throws IllegalArgumentException 非法参数异常
     */
    String uploadImage(MultipartFile file) throws IOException, IllegalArgumentException;
}