package com.emart.common.controller;

import com.emart.common.api.CommonResult;
import org.springframework.web.bind.annotation.*;

/**
 * 文件上传Controller
 */
@RestController
@RequestMapping("/file")
public class FileUploadController {

    /**
     * 上传商品图片
     */
    @PostMapping("/upload")
    public Object upload(@RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        if (file.isEmpty()) {
            return CommonResult.failed("请选择文件");
        }

        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));

        // 生成新的文件名
        String newFileName = System.currentTimeMillis() + suffix;

        try {
            // 保存文件到uploads目录
            String uploadDir = "D:\\test\\project\\emart\\uploads\\";
            java.io.File dest = new java.io.File(uploadDir + newFileName);

            // 确保目录存在
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }

            file.transferTo(dest);

            // 返回文件访问路径
            String url = "/uploads/" + newFileName;
            return CommonResult.success(url, "上传成功");

        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed("上传失败: " + e.getMessage());
        }
    }
}
