package com.emart.modules.pms.controller;

import com.emart.common.api.CommonResult;
import com.emart.modules.log.service.OperationLogService;
import com.emart.modules.pms.dto.CategoryParam;
import com.emart.modules.pms.model.Category;
import com.emart.modules.pms.service.CategoryService;
import com.emart.modules.ums.model.User;
import com.emart.modules.ums.service.UserService;
import com.emart.security.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

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
    public CommonResult<Boolean> createCategory(@Valid @RequestBody CategoryParam categoryParam,
                                                HttpServletRequest request) {
        boolean success = categoryService.createCategory(categoryParam);
        if (success) {
            logOperation("CREATE_CATEGORY", "添加商品分类：" + categoryParam.getName(), request);
            return CommonResult.success(true, "添加成功");
        }
        return CommonResult.failed("添加失败");
    }

    @Operation(summary = "更新分类")
    @PostMapping("/update/{id}")
    public CommonResult<Boolean> updateCategory(@PathVariable Long id,
                                               @Valid @RequestBody CategoryParam categoryParam,
                                               HttpServletRequest request) {
        boolean success = categoryService.updateCategory(id, categoryParam);
        if (success) {
            logOperation("UPDATE_CATEGORY", "更新商品分类：" + categoryParam.getName(), request);
            return CommonResult.success(true, "更新成功");
        }
        return CommonResult.failed("更新失败");
    }

    @Operation(summary = "删除分类")
    @PostMapping("/delete/{id}")
    public CommonResult<Boolean> deleteCategory(@PathVariable Long id,
                                                HttpServletRequest request) {
        Category category = categoryService.getById(id);
        boolean success = categoryService.deleteCategory(id);
        if (success) {
            logOperation("DELETE_CATEGORY", "删除商品分类：" + (category == null ? id : category.getName()), request);
            return CommonResult.success(true, "删除成功");
        }
        return CommonResult.failed("删除失败");
    }

    private void logOperation(String type, String content, HttpServletRequest request) {
        User operator = getOperator(request);
        if (operator != null) {
            operationLogService.record(operator.getId(), operator.getUsername(), operator.getRole(), type, content, request);
        }
    }

    private User getOperator(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        Long userId = jwtTokenUtil.getUserIdFromToken(authHeader.substring(7));
        return userId == null ? null : userService.getById(userId);
    }
}
