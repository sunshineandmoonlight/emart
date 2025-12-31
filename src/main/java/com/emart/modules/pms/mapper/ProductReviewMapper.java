package com.emart.modules.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emart.modules.pms.model.ProductReview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * 商品评价Mapper
 */
@Mapper
public interface ProductReviewMapper extends BaseMapper<ProductReview> {

    /**
     * 获取商品平均评分
     */
    @Select("SELECT AVG(rating) FROM product_review WHERE product_id = #{productId}")
    BigDecimal getAverageRating(@Param("productId") Long productId);

    /**
     * 获取商品评价总数
     */
    @Select("SELECT COUNT(*) FROM product_review WHERE product_id = #{productId}")
    Integer getReviewCount(@Param("productId") Long productId);
}
