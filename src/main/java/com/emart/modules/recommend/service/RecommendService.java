package com.emart.modules.recommend.service;

import com.emart.modules.pms.model.Product;

import java.util.List;

public interface RecommendService {

    List<Product> alsoBuy(Long productId, Integer limit);

    List<Product> recommendForUser(Long userId, Integer limit);
}
