package com.emart.modules.pms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emart.common.api.CommonResult;
import com.emart.modules.log.service.OperationLogService;
import com.emart.modules.pms.dto.ProductQueryParam;
import com.emart.modules.pms.dto.ProductSaveParam;
import com.emart.modules.pms.model.Product;
import com.emart.modules.pms.service.ProductService;
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

/**
 * 商品管理Controller
 */
@Slf4j
@RestController
@Tag(name = "商品管理", description = "商品增删改查")
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Operation(summary = "分页查询商品")
    @PostMapping("/list")
    public CommonResult<Page<Product>> listProducts(@RequestBody ProductQueryParam queryParam) {
        Page<Product> result = productService.listProducts(queryParam);
        return CommonResult.success(result);
    }

    @Operation(summary = "获取轮播图推荐商品")
    @GetMapping("/featured")
    public CommonResult<Page<Product>> getFeaturedProducts(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        ProductQueryParam queryParam = new ProductQueryParam();
        queryParam.setPageNum(pageNum);
        queryParam.setPageSize(pageSize);
        queryParam.setStatus(1); // 只获取上架商品
        Page<Product> result = productService.listProducts(queryParam);
        return CommonResult.success(result);
    }

    @Operation(summary = "获取商品详情")
    @GetMapping("/{id}")
    public CommonResult<Product> getProduct(@PathVariable Long id) {
        Product product = productService.getProductDetail(id);
        if (product == null) {
            return CommonResult.failed("商品不存在");
        }
        return CommonResult.success(product);
    }

    @Operation(summary = "添加商品")
    @PostMapping("/create")
    public CommonResult<Boolean> createProduct(@Valid @RequestBody ProductSaveParam productParam,
                                               HttpServletRequest request) {
        boolean success = productService.createProduct(productParam);
        if (success) {
            logOperation("CREATE_PRODUCT", "添加商品：" + productParam.getName(), request);
            return CommonResult.success(true, "添加成功");
        }
        return CommonResult.failed("添加失败");
    }

    @Operation(summary = "更新商品")
    @PostMapping("/update/{id}")
    public CommonResult<Boolean> updateProduct(@PathVariable Long id,
                                               @Valid @RequestBody ProductSaveParam productParam,
                                               HttpServletRequest request) {
        boolean success = productService.updateProduct(id, productParam);
        if (success) {
            logOperation("UPDATE_PRODUCT", "更新商品：" + productParam.getName(), request);
            return CommonResult.success(true, "更新成功");
        }
        return CommonResult.failed("更新失败");
    }

    @Operation(summary = "删除商品")
    @PostMapping("/delete/{id}")
    public CommonResult<Boolean> deleteProduct(@PathVariable Long id,
                                               HttpServletRequest request) {
        Product product = productService.getById(id);
        boolean success = productService.deleteProduct(id);
        if (success) {
            logOperation("DELETE_PRODUCT", "删除商品：" + (product == null ? id : product.getName()), request);
            return CommonResult.success(true, "删除成功");
        }
        return CommonResult.failed("删除失败");
    }

    @Operation(summary = "更新库存")
    @PostMapping("/stock/{id}")
    public CommonResult<Boolean> updateStock(@PathVariable Long id,
                                            @RequestParam Integer quantity,
                                            HttpServletRequest request) {
        boolean success = productService.updateStock(id, quantity);
        if (success) {
            logOperation("UPDATE_STOCK", "扣减商品库存，商品ID：" + id + "，数量：" + quantity, request);
            return CommonResult.success(true, "库存更新成功");
        }
        return CommonResult.failed("库存不足或商品不存在");
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
