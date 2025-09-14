package qian.blog_api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qian.blog_api.exception.BusinessException;
import qian.blog_api.mapper.BlogMapper;
import qian.blog_api.model.dto.BlogCreateDTO;
import qian.blog_api.model.entity.Blog;
import qian.blog_api.model.vo.BlogDetailVO;
import qian.blog_api.model.vo.BlogListVO;
import qian.blog_api.model.vo.DraftListVO;
import qian.blog_api.service.BlogService;
import qian.blog_api.service.CategoryService;
import qian.blog_api.util.DateUtil;
import java.util.List;

/**
 * 博文服务实现类
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

    @Autowired
    private CategoryService categoryService;

    @Override
    public Page<BlogListVO> getBlogPage(Integer pageNum, Integer pageSize, Integer categoryId, String sort) {
        // 1. 构建分页对象
        Page<Blog> page = new Page<>(pageNum, pageSize);

        // 2. 构建查询条件（只查询已发布的博文）
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_draft", false); // 非草稿
        if (categoryId != null) {
            queryWrapper.eq("category_id", categoryId); // 按分类筛选
        }

        // 3. 处理排序
        if ("create_time_asc".equals(sort)) {
            queryWrapper.orderByAsc("create_time");
        } else {
            queryWrapper.orderByDesc("create_time"); // 默认降序
        }

        // 4. 执行分页查询
        Page<Blog> blogPage = baseMapper.selectPage(page, queryWrapper);

        // 5. 手动转换分页数据（关键修正）
        // 5.1 转换列表数据
        List<BlogListVO> records = blogPage.getRecords().stream()
                .map(blog -> {
                    BlogListVO vo = new BlogListVO();
                    BeanUtils.copyProperties(blog, vo);
                    //时间格式不匹配，手动转换
                    vo.setCreateTime(DateUtil.format(blog.getCreateTime()));
                    vo.setCategoryName(categoryService.getCategoryNameById(blog.getCategoryId()));
                    return vo;
                })
                .collect(Collectors.toList());

        // 5.2 构建目标分页对象并设置属性
        Page<BlogListVO> resultPage = new Page<>();
        resultPage.setRecords(records);          // 分页数据列表
        resultPage.setTotal(blogPage.getTotal());// 总条数
        resultPage.setSize(blogPage.getSize());  // 每页条数
        resultPage.setCurrent(blogPage.getCurrent());// 当前页码
        resultPage.setPages(blogPage.getPages());// 总页数

        return resultPage;
    }

    @Override
    public BlogDetailVO getBlogById(Integer id) {
        // 1. 查询博文实体
        Blog blog = baseMapper.selectById(id);
        if (blog == null) {
            return null; // 后续可通过异常处理优化
        }

        // 2. 转换为详情 VO
        BlogDetailVO vo = new BlogDetailVO();
        BeanUtils.copyProperties(blog, vo);
        // 手动时间转换以及补充分类名称
        vo.setCreateTime(DateUtil.format(blog.getCreateTime()));
        vo.setUpdateTime(DateUtil.format(blog.getUpdateTime()));
        vo.setCategoryName(categoryService.getCategoryNameById(blog.getCategoryId()));
        return vo;
    }

    @Override
    public Page<BlogListVO> getDraftPage(Integer pageNum, Integer pageSize) {
        // 1. 构建分页对象
        Page<Blog> page = new Page<>(pageNum, pageSize);

        // 2. 构建查询条件（只查询草稿）
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_draft", true); // 是草稿
        queryWrapper.orderByDesc("update_time"); // 按更新时间降序

        // 3. 执行分页查询
        Page<Blog> blogPage = baseMapper.selectPage(page, queryWrapper);

        // 4. 转换为 VO 对象
        // 同样使用手动转换方式处理分页
        List<BlogListVO> records = blogPage.getRecords().stream()
                .map(blog -> {
                    BlogListVO vo = new BlogListVO();
                    BeanUtils.copyProperties(blog, vo);
                    vo.setCategoryName(categoryService.getCategoryNameById(blog.getCategoryId()));
                    return vo;
                })
                .collect(Collectors.toList());

        Page<BlogListVO> resultPage = new Page<>();
        resultPage.setRecords(records);
        resultPage.setTotal(blogPage.getTotal());
        resultPage.setSize(blogPage.getSize());
        resultPage.setCurrent(blogPage.getCurrent());
        resultPage.setPages(blogPage.getPages());

        return resultPage;
    }

    @Override
    public Integer createBlog(BlogCreateDTO createDTO) {
        // 1. 校验分类是否存在（避免无效分类ID）
        String categoryName = categoryService.getCategoryNameById(createDTO.getCategoryId());
        if (categoryName == null || categoryName.isEmpty()) {
            throw new BusinessException(2001, "分类不存在");
        }

        // 2. 转换DTO为实体类
        Blog blog = new Blog();
        BeanUtils.copyProperties(createDTO, blog);
        // 补充默认值（若DTO中未传递）
        if (blog.getIsDraft() == null) {
            blog.setIsDraft(false); // 默认直接发布
        }

        // 3. 保存到数据库（创建时间和更新时间由MyMetaObjectHandler自动填充）
        baseMapper.insert(blog);

        // 4. 返回新创建的博文ID
        return blog.getId();
    }

    // 在 BlogServiceImpl 中添加
    @Override
    public boolean updateBlog(Integer id, BlogCreateDTO updateDTO) {
        // 1. 校验博文是否存在
        Blog existingBlog = baseMapper.selectById(id);
        if (existingBlog == null) {
            return false; // 博文不存在
        }

        // 2. 校验分类是否存在（与创建逻辑一致）
        String categoryName = categoryService.getCategoryNameById(updateDTO.getCategoryId());
        if (categoryName == null || categoryName.isEmpty()) {
            throw new BusinessException(2001, "分类不存在");
        }

        // 3. 拷贝更新参数到实体（仅更新DTO中允许修改的字段）
        BeanUtils.copyProperties(updateDTO, existingBlog, "id");
        existingBlog.setUpdateTime(null);

        // 4. 执行更新（更新时间由 MyMetaObjectHandler 自动填充）
        int rows = baseMapper.updateById(existingBlog);
        return rows > 0;
    }

    @Override
    public boolean deleteBlog(Integer id) {
        // 1. 校验博文是否存在
        Blog existingBlog = baseMapper.selectById(id);
        if (existingBlog == null) {
            return false; // 博文不存在，返回删除失败
        }

        // 2. 执行删除操作（物理删除，若需逻辑删除可改为更新状态）
        int rows = baseMapper.deleteById(id);
        return rows > 0; // 返回删除是否成功
    }

        // 草稿查询（调用公共方法，传入isDraft=true）
        @Override
        public Page<DraftListVO> getDraftPage(Integer pageNum, Integer pageSize, Integer categoryId, String sort) {
            // 1. 构建分页对象
            Page<Blog> page = new Page<>(pageNum, pageSize);

            // 2. 构建查询条件（只查询已发布的博文）
            QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("is_draft", true); // 草稿

            // 3. 处理排序
            if ("create_time_asc".equals(sort)) {
                queryWrapper.orderByAsc("create_time");
            } else {
                queryWrapper.orderByDesc("create_time"); // 默认降序
            }

            // 4. 执行分页查询
            Page<Blog> blogPage = baseMapper.selectPage(page, queryWrapper);

            // 5. 手动转换分页数据（关键修正）
            // 5.1 转换列表数据
            List<DraftListVO> records = blogPage.getRecords().stream()
                    .map(blog -> {
                        DraftListVO vo = new DraftListVO();
                        BeanUtils.copyProperties(blog, vo);
                        //时间格式不匹配，手动转换
                        vo.setUpdateTime(DateUtil.format(blog.getUpdateTime()));
                        // vo.setCategoryName(categoryService.getCategoryNameById(blog.getCategoryId()));
                        return vo;
                    })
                    .collect(Collectors.toList());

            // 5.2 构建目标分页对象并设置属性
            Page<DraftListVO> resultPage = new Page<>();
            resultPage.setRecords(records);          // 分页数据列表
            resultPage.setTotal(blogPage.getTotal());// 总条数
            resultPage.setSize(blogPage.getSize());  // 每页条数
            resultPage.setCurrent(blogPage.getCurrent());// 当前页码
            resultPage.setPages(blogPage.getPages());// 总页数

            return resultPage;
        }


}
    