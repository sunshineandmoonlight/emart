package com.emart.modules.cms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.emart.modules.cms.mapper.BrowseLogMapper;
import com.emart.modules.cms.model.BrowseLog;
import com.emart.modules.cms.service.BrowseLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 浏览日志Service实现类
 */
@Slf4j
@Service
public class BrowseLogServiceImpl extends ServiceImpl<BrowseLogMapper, BrowseLog> implements BrowseLogService {

    @Override
    public boolean logBrowse(Long userId, String userName, Long productId, String productName, String ip) {
        try {
            BrowseLog browseLog = new BrowseLog();
            browseLog.setUserId(userId);
            browseLog.setUserName(userName);
            browseLog.setProductId(productId);
            browseLog.setProductName(productName);
            browseLog.setIp(ip);
            browseLog.setCreateTime(new Date());

            return this.save(browseLog);
        } catch (Exception e) {
            log.error("记录浏览日志失败", e);
            return false;
        }
    }
}
