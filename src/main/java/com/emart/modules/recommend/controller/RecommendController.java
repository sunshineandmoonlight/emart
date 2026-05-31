package com.emart.modules.recommend.controller;

import com.emart.common.api.CommonResult;
import com.emart.modules.pms.model.Product;
import com.emart.modules.recommend.service.RecommendService;
import com.emart.security.util.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Tag(name = "推荐系统", description = "基于浏览和购买行为的商品推荐")
@RequestMapping("/recommend")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Operation(summary = "浏览过此商品的人也买了")
    @GetMapping("/also-buy/{productId}")
    public CommonResult<List<Product>> alsoBuy(@PathVariable Long productId,
                                               @RequestParam(defaultValue = "4") Integer limit) {
        return CommonResult.success(recommendService.alsoBuy(productId, limit));
    }

    @Operation(summary = "当前用户个性化推荐")
    @GetMapping("/user")
    public CommonResult<List<Product>> recommendForUser(@RequestParam(defaultValue = "4") Integer limit,
                                                        HttpServletRequest request) {
        return CommonResult.success(recommendService.recommendForUser(getUserId(request), limit));
    }

    private Long getUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return jwtTokenUtil.getUserIdFromToken(authHeader.substring(7));
    }
}
