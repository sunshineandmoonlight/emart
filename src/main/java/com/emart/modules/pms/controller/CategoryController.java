package com.emart.modules.pms.controller;

import com.emart.common.api.CommonResult;
import com.emart.modules.pms.dto.CategoryParam;
import com.emart.modules.pms.model.Category;
import com.emart.modules.pms.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 商品分类Controller
 */
@Slf4j
@RestController
@Tag(name = "商品分类管理", description = "商品分类增删改查")
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "获取所有分类")
    @GetMapping("/listAll")
    public CommonResult<List<Category>> listAllCategories() {
        List<Category> categories = categoryService.listAllCategories();
        return CommonResult.success(categories);
    }

    @Operation(summary = "获取分类树")
    @GetMapping("/tree")
    public CommonResult<List<Category>> getCategoryTree() {
        List<Category> tree = categoryService.getCategoryTree();
        return CommonResult.success(tree);
    }

    @Operation(summary = "添加分类")
    @PostMapping("/create")
    public CommonResult<Boolean> createCategory(@Valid @RequestBody CategoryParam categoryParam) {
        boolean success = categoryService.createCategory(categoryParam);
        if (success) {
            return CommonResult.success(true, "添加成功");
        }
        return CommonResult.failed("添加失败");
    }

    @Operation(summary = "更新分类")
    @PostMapping("/update/{id}")
    public CommonResult<Boolean> updateCategory(@PathVariable Long id,
                                               @Valid @RequestBody CategoryParam categoryParam) {
        boolean success = categoryService.updateCategory(id, categoryParam);
        if (success) {
            return CommonResult.success(true, "更新成功");
        }
        return CommonResult.failed("更新失败");
    }

    @Operation(summary = "删除分类")
    @PostMapping("/delete/{id}")
    public CommonResult<Boolean> deleteCategory(@PathVariable Long id) {
        boolean success = categoryService.deleteCategory(id);
        if (success) {
            return CommonResult.success(true, "删除成功");
        }
        return CommonResult.failed("删除失败");
    }
}
