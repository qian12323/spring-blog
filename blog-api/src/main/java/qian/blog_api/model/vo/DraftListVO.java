package qian.blog_api.model.vo;

import lombok.Data;

/**
 * 草稿列表VO（用于/api/blogs/drafts接口）
 */
@Data
public class DraftListVO {
    private Integer id;
    private String title;
    private String updateTime; // 格式化后的时间字符串
    private Integer categoryId; // 分类Id
    private String categoryName;
}
    