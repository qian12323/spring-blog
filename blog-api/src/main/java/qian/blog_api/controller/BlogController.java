package qian.blog_api.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qian.blog_api.model.Result;
import qian.blog_api.model.dto.BlogCreateDTO;
import qian.blog_api.model.dto.BlogQueryDTO;
import qian.blog_api.model.vo.BlogDetailVO;
import qian.blog_api.model.vo.BlogListVO;
import qian.blog_api.model.vo.DraftListVO;
import qian.blog_api.service.BlogService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


/**
 * 博文管理控制器
 * 处理博文的CRUD及查询接口
 */
@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    /**
     * 分页查询博文列表（公开接口）
     */
    @GetMapping
    public Result<Page<BlogListVO>> getBlogPage(@Valid BlogQueryDTO queryDTO) {
        Page<BlogListVO> blogPage = blogService.getBlogPage(
                queryDTO.getPage(),
                queryDTO.getSize(),
                queryDTO.getCategoryId(),
                queryDTO.getSort()
        );
        return Result.success(blogPage);
    }

    /*
     * 根据博文id查询具体某一篇博文
     */
    @GetMapping("/{id}")
    public Result<BlogDetailVO> getBlogDetail(@PathVariable Integer id) {
        BlogDetailVO blogDetail = blogService.getBlogById(id);
        if (blogDetail == null) {
            return Result.notFound("博文不存在或已被删除");
        }
        return Result.success(blogDetail);
    }

    /**
     * 创建博文接口（需登录）
     */
    @PostMapping("/create")
    public Result<Integer> createBlog(@Valid @RequestBody BlogCreateDTO createDTO) {
        // 调用Service创建博文，返回新博文的ID
        Integer blogId = blogService.createBlog(createDTO);
        return Result.success("博文创建成功", blogId);
    }


    // 在 BlogController 中添加
    /**
     * 更新博文接口（需登录）
     */
    @PutMapping("/update/{id}")
    public Result<?> updateBlog(
            @PathVariable Integer id,
            @Valid @RequestBody BlogCreateDTO updateDTO
    ) {
        // 调用 Service 执行更新操作
        boolean success = blogService.updateBlog(id, updateDTO);
        if (success) {
            return Result.success("博文更新成功");
        } else {
            return Result.notFound("博文不存在或已被删除");
        }
    }
    
     /**
     * 删除博文接口（需登录）
     */
    @DeleteMapping("/delete/{id}")
    public Result<?> deleteBlog(@PathVariable Integer id) {
        boolean success = blogService.deleteBlog(id);
        if (success) {
            return Result.success("博文删除成功");
        } else {
            return Result.notFound("博文不存在或已被删除");
        }
    }

    /**
     * 根据分类ID查询该分类下的所有博文（分页）
     * 公开接口，无需登录
     */
    @GetMapping("/category/{categoryId}")
    public Result<Page<BlogListVO>> getBlogsByCategory(
            @PathVariable Integer categoryId,
            @Valid BlogQueryDTO queryDTO
    ) {
        // 调用服务层方法，传入分类ID和分页参数
        Page<BlogListVO> blogPage = blogService.getBlogPage(
                queryDTO.getPage(),
                queryDTO.getSize(),
                categoryId, // 固定传入路径中的分类ID
                queryDTO.getSort()
        );
        return Result.success(blogPage);
    }

    // 草稿箱分页查询（新增分类和排序参数支持）
    @GetMapping("/drafts")
    public Result<Page<DraftListVO>> getDraftPage(@Valid BlogQueryDTO queryDTO) {
        Page<DraftListVO> draftPage = blogService.getDraftPage(
                queryDTO.getPage(),
                queryDTO.getSize(),
                queryDTO.getCategoryId(), // 支持按分类筛选草稿
                queryDTO.getSort()       // 支持排序
        );
        return Result.success(draftPage);
    }

}