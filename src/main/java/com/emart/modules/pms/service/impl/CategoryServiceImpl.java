package com.emart.modules.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emart.modules.pms.dto.CategoryParam;
import com.emart.modules.pms.mapper.CategoryMapper;
import com.emart.modules.pms.model.Category;
import com.emart.modules.pms.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品分类Service实现类
 */
@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public List<Category> listAllCategories() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);
        return this.list(wrapper);
    }

    @Override
    public List<Category> getCategoryTree() {
        // 简化实现：直接返回所有分类，前端自行组装树形结构
        return listAllCategories();
    }

    @Override
    public boolean createCategory(CategoryParam categoryParam) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryParam, category);

        if (categoryParam.getId() != null && categoryParam.getId() == 0) {
            category.setId(null);
        }

        return this.save(category);
    }

    @Override
    public boolean updateCategory(Long id, CategoryParam categoryParam) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryParam, category);
        category.setId(id);
        return this.updateById(category);
    }

    @Override
    public boolean deleteCategory(Long id) {
        return this.removeById(id);
    }
}
