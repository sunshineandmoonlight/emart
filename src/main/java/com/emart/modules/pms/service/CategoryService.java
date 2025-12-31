package com.emart.modules.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emart.modules.pms.dto.CategoryParam;
import com.emart.modules.pms.model.Category;

import java.util.List;

/**
 * 商品分类Service
 */
public interface CategoryService extends IService<Category> {

    /**
     * 获取所有分类
     */
    List<Category> listAllCategories();

    /**
     * 获取分类树
     */
    List<Category> getCategoryTree();

    /**
     * 创建分类
     */
    boolean createCategory(CategoryParam categoryParam);

    /**
     * 更新分类
     */
    boolean updateCategory(Long id, CategoryParam categoryParam);

    /**
     * 删除分类
     */
    boolean deleteCategory(Long id);
}
