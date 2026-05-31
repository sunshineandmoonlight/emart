package com.emart.modules.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.emart.modules.log.model.OperationLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {
}
