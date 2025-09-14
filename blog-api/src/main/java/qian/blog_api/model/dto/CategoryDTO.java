package qian.blog_api.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 分类创建/更新参数DTO
 * 对应接口：POST /api/categories、PUT /api/categories/{id}
 */
@Data
public class CategoryDTO {

    /**
     * 分类名称
     * 非空校验：不能为空
     * 长度限制：最大100字符
     * 唯一性：数据库层面保证
     */
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 100, message = "分类名称长度不能超过100个字符")
    private String name;

    /**
     * 分类描述
     * 长度限制：最大500字符
     */
    @Size(max = 500, message = "分类描述长度不能超过500个字符")
    private String description;
}
    