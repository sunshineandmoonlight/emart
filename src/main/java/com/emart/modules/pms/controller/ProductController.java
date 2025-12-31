package com.emart.modules.pms.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.emart.common.api.CommonResult;
import com.emart.modules.pms.dto.ProductQueryParam;
import com.emart.modules.pms.dto.ProductSaveParam;
import com.emart.modules.pms.model.Product;
import com.emart.modules.pms.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public CommonResult<Boolean> createProduct(@Valid @RequestBody ProductSaveParam productParam) {
        boolean success = productService.createProduct(productParam);
        if (success) {
            return CommonResult.success(true, "添加成功");
        }
        return CommonResult.failed("添加失败");
    }

    @Operation(summary = "更新商品")
    @PostMapping("/update/{id}")
    public CommonResult<Boolean> updateProduct(@PathVariable Long id,
                                               @Valid @RequestBody ProductSaveParam productParam) {
        boolean success = productService.updateProduct(id, productParam);
        if (success) {
            return CommonResult.success(true, "更新成功");
        }
        return CommonResult.failed("更新失败");
    }

    @Operation(summary = "删除商品")
    @PostMapping("/delete/{id}")
    public CommonResult<Boolean> deleteProduct(@PathVariable Long id) {
        boolean success = productService.deleteProduct(id);
        if (success) {
            return CommonResult.success(true, "删除成功");
        }
        return CommonResult.failed("删除失败");
    }

    @Operation(summary = "更新库存")
    @PostMapping("/stock/{id}")
    public CommonResult<Boolean> updateStock(@PathVariable Long id,
                                            @RequestParam Integer quantity) {
        boolean success = productService.updateStock(id, quantity);
        if (success) {
            return CommonResult.success(true, "库存更新成功");
        }
        return CommonResult.failed("库存不足或商品不存在");
    }
}
