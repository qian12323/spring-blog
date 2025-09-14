package qian.blog_api.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 创建博文请求参数DTO
 * 对应接口：POST /api/blogs/create
 */
@Data
public class BlogCreateDTO {

    /**
     * 博文标题（必填）
     */
    @NotNull(message = "标题不能为空")
    @Size(max = 200, message = "标题长度不能超过200个字符")
    private String title;

    /**
     * 博文内容
     */
    private String content;

    /**
     * 博文摘要
     */
    @Size(max = 500, message = "摘要长度不能超过500个字符")
    private String summary;

    /**
     * 分类ID（必填）
     */
    @NotNull(message = "分类ID不能为空")
    private Integer categoryId;

    /**
     * 是否为草稿（默认false：直接发布）
     */
    private Boolean isDraft = false;
}