package qian.blog_api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import qian.blog_api.model.entity.User;

/**
 * 用户表 Mapper 接口
 * 继承 BaseMapper 后，自动获得 CRUD 方法
 */
public interface UserMapper extends BaseMapper<User> {
    // 基础操作已包含在 BaseMapper 中，复杂查询可在此扩展
}
    