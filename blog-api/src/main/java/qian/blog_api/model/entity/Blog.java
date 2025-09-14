package qian.blog_api.model.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldFill;

import lombok.Data;

/**
 * 博文实体类（对应数据库 blog 表）
 */
@Data
@TableName("blog") // 指定对应的数据库表名
public class Blog {

    /**
     * 主键 ID，自增
     */
    @TableId(type = IdType.AUTO) // 主键策略：自增
    private Integer id;

    /**
     * 博文标题
     */
    private String title;

    /**
     * 博文内容
     */
    private String content;

    /**
     * 博文摘要
     */
    private String summary;

    /**
     * 关联分类表的主键（分类 ID）
     */
    @TableField("category_id") // 对应数据库字段名（驼峰转下划线可省略，但显式声明更清晰）
    private Integer categoryId;

    /**
     * 是否为草稿（true：草稿，false：已发布）
     * 数据库表中需新增 is_draft 字段（tinyint 类型，默认 0）
     */
    @TableField("is_draft")
    private Boolean isDraft;

    /**
     * 创建时间
     * 插入时自动填充当前时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     * 插入和更新时自动填充当前时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}