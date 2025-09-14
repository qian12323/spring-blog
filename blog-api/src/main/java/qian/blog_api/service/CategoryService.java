package qian.blog_api.service;

import com.baomidou.mybatisplus.extension.service.IService;

import qian.blog_api.model.dto.CategoryDTO;
import qian.blog_api.model.entity.Category;
import qian.blog_api.model.vo.CategoryVO;

import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService extends IService<Category> {

    // 基础 CRUD 方法已通过 IService 继承，此处定义业务相关方法

    /**
     * 获取所有分类列表（包含博文数量）
     * @return 分类列表（VO 形式）
     */
    List<CategoryVO> getAllCategories();

    /**
     * 根据 ID 获取分类名称
     * @param id 分类ID
     * @return 分类名称
     */
    String getCategoryNameById(Integer id);

    /**
     * 创建分类
     * @param categoryDTO 分类信息
     * @return 创建成功的分类ID
     */
    Integer createCategory(CategoryDTO categoryDTO);

    /**
     * 更新分类
     * @param id 分类ID
     * @param categoryDTO 新的分类信息
     * @return 是否更新成功
     */
    boolean updateCategory(Integer id, CategoryDTO categoryDTO);

    /**
     * 删除分类
     * @param id 分类ID
     * @return 是否删除成功
     */
    boolean deleteCategory(Integer id);
}
    