package qian.blog_api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qian.blog_api.exception.BusinessException;
import qian.blog_api.mapper.BlogMapper;
import qian.blog_api.mapper.CategoryMapper;
import qian.blog_api.model.entity.Category;
import qian.blog_api.model.vo.CategoryVO;
import qian.blog_api.service.CategoryService;
import qian.blog_api.model.dto.CategoryDTO;
import qian.blog_api.model.entity.Blog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类服务实现类
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private BlogMapper blogMapper;

    @Override
    public List<CategoryVO> getAllCategories() {
        // 1. 查询所有分类
        List<Category> categories = baseMapper.selectList(null);

        // 2. 转换为 VO 并计算每个分类的博文数量
        return categories.stream().map(category -> {
            CategoryVO vo = new CategoryVO();
            BeanUtils.copyProperties(category, vo);
            // 查询该分类下的博文数量（只统计已发布的）
            QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("category_id", category.getId());
            queryWrapper.eq("is_draft", false);
            long blogCount = blogMapper.selectCount(queryWrapper);
            vo.setBlogCount((int) blogCount);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public String getCategoryNameById(Integer id) {
        if (id == null) {
            return "";
        }
        Category category = baseMapper.selectById(id);
        return category != null ? category.getName() : "";
    }

    @Override
    public Integer createCategory(CategoryDTO categoryDTO) {
        // 1. 校验分类名称是否已存在
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", categoryDTO.getName());
        if (baseMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException(2002, "分类名称已存在");
        }

        // 2. 转换DTO为实体并设置默认值
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());

        // 3. 保存到数据库
        baseMapper.insert(category);
        return category.getId();
    }

    @Override
    public boolean updateCategory(Integer id, CategoryDTO categoryDTO) {
        // 1. 校验分类是否存在
        Category existingCategory = baseMapper.selectById(id);
        if (existingCategory == null) {
            return false;
        }

        // 2. 校验分类名称是否已被其他分类使用
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", categoryDTO.getName())
                   .ne("id", id); // 排除当前分类
        if (baseMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException(2002, "分类名称已存在");
        }

        // 3. 更新分类信息
        BeanUtils.copyProperties(categoryDTO, existingCategory);
        existingCategory.setUpdateTime(LocalDateTime.now());
        int rows = baseMapper.updateById(existingCategory);
        return rows > 0;
    }

    @Override
    public boolean deleteCategory(Integer id) {
        // 1. 校验分类是否存在
        Category existingCategory = baseMapper.selectById(id);
        if (existingCategory == null) {
            return false;
        }

        // 2. 校验该分类下是否有博文（有则不允许删除）
        QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<>();
        blogQueryWrapper.eq("category_id", id);
        if (blogMapper.selectCount(blogQueryWrapper) > 0) {
            throw new BusinessException(2003, "该分类下存在博文，无法删除");
        }

        // 3. 执行删除
        int rows = baseMapper.deleteById(id);
        return rows > 0;
    }

}
    