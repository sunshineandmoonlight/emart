package com.emart.modules.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emart.modules.oms.model.OrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项Mapper
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
}
