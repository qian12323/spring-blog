package qian.blog_api.model.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 博文列表查询参数DTO
 * 对应接口：GET /api/blogs
 */
@Data
public class BlogQueryDTO {

    /**
     * 分类ID（可选）
     * 用于筛选特定分类下的博文
     */
    private Integer categoryId;

    /**
     * 页码
     * 最小值：1，默认值：1
     */
    @Min(value = 1, message = "页码不能小于1")
    private Integer page = 1;

    /**
     * 每页条数
     * 最小值：1，默认值：10
     */
    @Min(value = 1, message = "每页条数不能小于1")
    private Integer size = 10;

    /**
     * 排序方式
     * 可选值：create_time_asc（按创建时间升序）、create_time_desc（按创建时间降序）
     * 默认值：create_time_desc
     */
    private String sort = "create_time_desc";
}
    