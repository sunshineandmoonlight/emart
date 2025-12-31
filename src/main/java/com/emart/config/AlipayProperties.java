package com.emart.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Alipay配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "alipay")
public class AlipayProperties {

    /**
     * 支付宝网关
     */
    private String gateway;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 应用私钥
     */
    private String privateKey;

    /**
     * 支付宝公钥
     */
    private String alipayPublicKey;

    /**
     * 前端回调地址
     */
    private String returnUrl;

    /**
     * 后台异步通知地址
     */
    private String notifyUrl;

    /**
     * 签名类型
     */
    private String signType;
}
