package qian.blog_api.model.vo;

import lombok.Data;

/**
 * 博文列表项VO（用于/api/blogs接口）
 */
@Data
public class BlogListVO {
    private Integer id;
    private String title;
    private String summary;
    private Integer categoryId;
    private String categoryName; // 分类名称（关联查询）
    private String createTime; // 格式化后的时间字符串
}
    