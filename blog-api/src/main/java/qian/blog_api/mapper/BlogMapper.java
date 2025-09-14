package qian.blog_api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import qian.blog_api.model.entity.Blog;

/**
 * 博文表 Mapper 接口
 */

public interface BlogMapper extends BaseMapper<Blog> {
    // 后续可添加分页查询、按分类筛选等复杂方法
}
    