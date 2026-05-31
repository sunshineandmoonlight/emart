package com.emart.modules.cms.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 浏览日志表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("cms_browse_log")
public class BrowseLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品分类ID
     */
    private Long categoryId;

    /**
     * 商品分类名称
     */
    private String categoryName;

    /**
     * 访问IP
     */
    private String ip;

    /**
     * 停留时长（秒）
     */
    private Integer durationSeconds;

    /**
     * User-Agent
     */
    private String userAgent;

    /**
     * 离开时间
     */
    private Date leaveTime;

    /**
     * 创建时间
     */
    private Date createTime;
}
