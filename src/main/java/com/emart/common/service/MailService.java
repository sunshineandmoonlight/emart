package com.emart.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.util.Map;

/**
 * 邮件服务类
 */
@Slf4j
@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送简单文本邮件
     */
    public void sendSimpleMail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            log.info("简单邮件发送成功：收件人={}, 主题={}", to, subject);
        } catch (Exception e) {
            log.error("发送简单邮件失败：收件人={}, 主题={}", to, subject, e);
            throw new RuntimeException("邮件发送失败：" + e.getMessage());
        }
    }

    /**
     * 发送HTML邮件
     */
    public void sendHtmlMail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
            log.info("HTML邮件发送成功：收件人={}, 主题={}", to, subject);
        } catch (Exception e) {
            log.error("发送HTML邮件失败：收件人={}, 主题={}", to, subject, e);
            throw new RuntimeException("邮件发送失败：" + e.getMessage());
        }
    }

    /**
     * 发送模板邮件（使用Thymeleaf模板）
     */
    public void sendTemplateMail(String to, String subject, String templateName, Map<String, Object> variables) {
        try {
            // 创建Thymeleaf上下文
            Context context = new Context();
            context.setVariables(variables);

            // 渲染模板
            String content = templateEngine.process(templateName, context);

            // 发送HTML邮件
            sendHtmlMail(to, subject, content);
            log.info("模板邮件发送成功：收件人={}, 主题={}, 模板={}", to, subject, templateName);
        } catch (Exception e) {
            log.error("发送模板邮件失败：收件人={}, 主题={}, 模板={}", to, subject, templateName, e);
            throw new RuntimeException("邮件发送失败：" + e.getMessage());
        }
    }
}
