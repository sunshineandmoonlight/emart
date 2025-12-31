package com.emart.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Alipay配置类
 */
@Slf4j
@Configuration
public class AlipayConfig {

    @Autowired
    private AlipayProperties alipayProperties;

    /**
     * 清理密钥格式，移除BEGIN/END标记和换行符
     */
    private String cleanKey(String key) {
        if (key == null) {
            return null;
        }
        // 移除BEGIN/END标记
        String cleaned = key.replaceAll("-----BEGIN.*-----", "")
                            .replaceAll("-----END.*-----", "")
                            .replaceAll("\\r", "")
                            .replaceAll("\\n", "")
                            .trim();
        log.info("密钥已清理，原始长度: {}, 清理后长度: {}", key.length(), cleaned.length());
        return cleaned;
    }

    @Bean
    public AlipayClient alipayClient() {
        log.info("初始化AlipayClient，网关：{}", alipayProperties.getGateway());
        String cleanedPrivateKey = cleanKey(alipayProperties.getPrivateKey());
        String cleanedAlipayPublicKey = cleanKey(alipayProperties.getAlipayPublicKey());

        return new DefaultAlipayClient(
                alipayProperties.getGateway(),
                alipayProperties.getAppId(),
                cleanedPrivateKey,
                "json",
                "UTF-8",
                cleanedAlipayPublicKey,
                alipayProperties.getSignType()
        );
    }
}
