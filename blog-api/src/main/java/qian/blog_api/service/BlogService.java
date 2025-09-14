package qian.blog_api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import qian.blog_api.model.dto.BlogCreateDTO;
import qian.blog_api.model.entity.Blog;
import qian.blog_api.model.vo.BlogListVO;
import qian.blog_api.model.vo.DraftListVO;
import qian.blog_api.model.vo.BlogDetailVO;

/**
 * 博文服务接口
 */
public interface BlogService extends IService<Blog> {

    // 基础 CRUD 方法已通过 IService 继承，此处定义业务相关方法

    /**
     * 分页查询博文列表
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param categoryId 分类ID（可选，用于筛选）
     * @param sort 排序方式（create_time_asc 或 create_time_desc）
     * @return 分页后的博文列表（VO 形式）
     */
    Page<BlogListVO> getBlogPage(Integer pageNum, Integer pageSize, Integer categoryId, String sort);

    /**
     * 根据 ID 获取博文详情
     * @param id 博文ID
     * @return 博文详情（VO 形式）
     */
    BlogDetailVO getBlogById(Integer id);

    /**
     * 分页查询草稿列表
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 分页后的草稿列表（VO 形式）
     */
    Page<BlogListVO> getDraftPage(Integer pageNum, Integer pageSize);

    /**
     * 创建博文
     * @param createDTO 创建博文的参数
     * @return 新创建博文的ID
     */
    Integer createBlog(BlogCreateDTO createDTO);

    /**
     * 更新博文
     * @param id 博文ID
     * @param updateDTO 更新参数
     * @return 是否更新成功（true：成功，false：博文不存在）
     */
    boolean updateBlog(Integer id, BlogCreateDTO updateDTO);

    /**
     * 根据ID删除博文
     * @param id 博文ID
     * @return 是否删除成功（true：成功，false：博文不存在）
     */
    boolean deleteBlog(Integer id);

    /**
     * 分页查询草稿列表
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param categoryId 分类ID（可选，支持按分类筛选草稿）
     * @param sort 排序方式（可选）
     * @return 分页后的草稿列表
     */
    Page<DraftListVO> getDraftPage(Integer pageNum, Integer pageSize, Integer categoryId, String sort);

}
    