package com.emart.modules.pms.controller;

import com.emart.common.api.CommonResult;
import com.emart.modules.pms.model.SkuStock;
import com.emart.modules.pms.service.SkuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SKU库存Controller
 */
@RestController
@Tag(name = "SKU管理", description = "SKU库存管理")
@RequestMapping("/sku")
public class SkuController {

    @Autowired
    private SkuService skuService;

    /**
     * 根据商品ID获取SKU列表
     */
    @Operation(summary = "获取商品的SKU列表")
    @GetMapping("/product/{productId}")
    public CommonResult<List<SkuStock>> getSkusByProductId(@PathVariable Long productId) {
        List<SkuStock> skus = skuService.getSkusByProductId(productId);
        return CommonResult.success(skus);
    }

    /**
     * 根据ID获取SKU
     */
    @Operation(summary = "获取SKU详情")
    @GetMapping("/{id}")
    public CommonResult<SkuStock> getSkuById(@PathVariable Long id) {
        SkuStock sku = skuService.getSkuById(id);
        if (sku == null) {
            return CommonResult.failed("SKU不存在");
        }
        return CommonResult.success(sku);
    }

    /**
     * 创建SKU
     */
    @Operation(summary = "创建SKU")
    @PostMapping("/create")
    public CommonResult<SkuStock> createSku(@RequestBody SkuStock skuStock) {
        try {
            SkuStock sku = skuService.createSku(skuStock);
            return CommonResult.success(sku);
        } catch (Exception e) {
            return CommonResult.failed("创建SKU失败: " + e.getMessage());
        }
    }

    /**
     * 更新SKU
     */
    @Operation(summary = "更新SKU")
    @PutMapping("/update")
    public CommonResult<SkuStock> updateSku(@RequestBody SkuStock skuStock) {
        try {
            SkuStock sku = skuService.updateSku(skuStock);
            return CommonResult.success(sku);
        } catch (Exception e) {
            return CommonResult.failed("更新SKU失败: " + e.getMessage());
        }
    }

    /**
     * 删除SKU
     */
    @Operation(summary = "删除SKU")
    @DeleteMapping("/{id}")
    public CommonResult<Void> deleteSku(@PathVariable Long id) {
        try {
            skuService.deleteSku(id);
            return CommonResult.success();
        } catch (Exception e) {
            return CommonResult.failed("删除SKU失败: " + e.getMessage());
        }
    }

    /**
     * 更新库存
     */
    @Operation(summary = "更新SKU库存")
    @PutMapping("/stock/{skuId}/{quantity}")
    public CommonResult<Void> updateStock(@PathVariable Long skuId, @PathVariable Integer quantity) {
        try {
            skuService.updateStock(skuId, quantity);
            return CommonResult.success();
        } catch (Exception e) {
            return CommonResult.failed("更新库存失败: " + e.getMessage());
        }
    }
}
