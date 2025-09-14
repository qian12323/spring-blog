package qian.blog_api.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类（对应数据库 user 表）
 */
@Data
@TableName("user")
public class User {

    /**
     * 主键 ID，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名（唯一）
     */
    private String username;

    /**
     * 加密后的密码
     */
    private String password;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private String privilege;
}
    