package qian.blog_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qian.blog_api.model.Result;
import qian.blog_api.model.dto.CategoryDTO;
import qian.blog_api.model.vo.CategoryVO;
import qian.blog_api.service.CategoryService;

import java.util.List;

/**
 * 分类管理控制器
 * 处理分类的查询等接口
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取所有分类列表（包含每个分类下的博文数量）
     * 公开接口，无需登录即可访问
     */
    @GetMapping
    public Result<List<CategoryVO>> getAllCategories() {
        List<CategoryVO> categories = categoryService.getAllCategories();
        return Result.success(categories);
    }


    /**
     * 创建分类（需要管理员权限）
     */
    @PostMapping("create")
    // @RequireAdmin // 自定义注解，用于校验管理员权限
    public Result<Integer> createCategory(@Validated @RequestBody CategoryDTO categoryDTO) {
        Integer categoryId = categoryService.createCategory(categoryDTO);
        return Result.success("分类创建成功", categoryId);
    }

    /**
     * 更新分类（需要管理员权限）
     */
    @PutMapping("/update/{id}")
    // @RequireAdmin
    public Result<?> updateCategory(
            @PathVariable Integer id,
            @Validated @RequestBody CategoryDTO categoryDTO
    ) {
        boolean success = categoryService.updateCategory(id, categoryDTO);
        if (success) {
            return Result.success("分类更新成功");
        } else {
            return Result.notFound("分类不存在");
        }
    }

    /**
     * 删除分类（需要管理员权限）
     */
    @DeleteMapping("/delete/{id}")
    // @RequireAdmin
    public Result<?> deleteCategory(@PathVariable Integer id) {
        boolean success = categoryService.deleteCategory(id);
        if (success) {
            return Result.success("分类删除成功");
        } else {
            return Result.notFound("分类不存在");
        }
    }
}