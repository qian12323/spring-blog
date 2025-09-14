package qian.blog_api.model.vo;

import lombok.Data;

/**
 * 分类VO（用于/api/categories接口）
 */
@Data
public class CategoryVO {
    private Integer id;
    private String name;
    private String description;
    private Integer blogCount; // 该分类下的博文数量（统计字段）
}
    