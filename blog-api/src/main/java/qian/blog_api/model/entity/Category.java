package qian.blog_api.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 分类实体类（对应数据库 category 表）
 */
@Data
@TableName("category")
public class Category {

    /**
     * 主键 ID，自增
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 分类名称（唯一）
     */
    private String name;

    /**
     * 分类描述
     */
    private String description;

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
}
    