package qian.blog_api.model.vo;

import lombok.Data;

/**
 * 博文详情VO（用于/api/blogs/{id}接口）
 */
@Data
public class BlogDetailVO {
    private Integer id;
    private String title;
    private String content;
    private Integer categoryId;
    private String categoryName; // 分类名称（关联查询）
    private String createTime; // 格式化后的时间字符串
    private String updateTime; // 格式化后的时间字符串
}
    