package com.emart.modules.cms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.emart.modules.cms.model.BrowseLog;

/**
 * 浏览日志Service
 */
public interface BrowseLogService extends IService<BrowseLog> {

    /**
     * 记录浏览日志
     */
    boolean logBrowse(Long userId, String userName, Long productId, String productName, String ip);

    /**
     * 记录浏览行为，包含分类、停留时长和客户端信息
     */
    boolean logBrowse(Long userId, String userName, Long productId, String productName,
                      Long categoryId, String categoryName, String ip,
                      Integer durationSeconds, String userAgent);
}
