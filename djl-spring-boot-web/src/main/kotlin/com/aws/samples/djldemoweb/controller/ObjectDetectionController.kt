/*
 * Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
// 物体检测前端请求控制器包名
package com.aws.samples.djldemoweb.controller

// 导入S3图片下载工具类
import com.aws.samples.djl.spring.common.S3ImageDownloader
// 导入S3图片上传工具类
import com.aws.samples.djl.spring.common.S3ImageUploader
// 导入检测接口调用客户端
import com.aws.samples.djldemoweb.backend.ObjectDetectionClient
// 导入表单数据实体类
import com.aws.samples.djldemoweb.form.ObjectDetectionForm
// JSON序列化工具
import com.fasterxml.jackson.databind.ObjectMapper
// 日志相关依赖
import org.slf4j.Logger
import org.slf4j.LoggerFactory
// 输入流资源封装类
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
// Spring控制器注解
import org.springframework.stereotype.Controller
// 页面模型数据封装
import org.springframework.ui.Model
// 请求映射、参数绑定相关注解
import org.springframework.web.bind.annotation.*
// 超时时间控制
import java.time.Duration

/**
 * 物体检测业务控制器
 * 处理图片上传、检测请求、图片预览、结果展示等页面接口逻辑
 */
@Controller
class ObjectDetectionController(
    private val uploader: S3ImageUploader,          // S3图片上传工具实例
    private val downloader: S3ImageDownloader,       // S3图片下载工具实例
    private val apiClient: ObjectDetectionClient    // 检测服务调用客户端实例
) {
    // 日志对象
    companion object {
        val LOG: Logger = LoggerFactory.getLogger(ObjectDetectionController::class.java)
    }

    /**
     * 查询待检测文件夹图片列表
     * @param model 页面数据模型
     * @return 图片列表页面视图名
     */
    @RequestMapping("/object-detection/inbox")
    fun listFiles(model: Model): String {
        // 查询inbox目录下所有图片文件并传入页面
        model.addAttribute("files", downloader.listFolder("inbox"))
        return "object-detection-files"
    }

    /**
     * 跳转新建物体检测表单页面
     * @param model 页面数据模型
     * @return 检测表单页面视图名
     */
    @RequestMapping("/object-detection/inbox/new-object-detection")
    fun newObjectDetection(model: Model): String  {
        // 初始化空表单对象传入页面
        model.addAttribute("objectDetectionForm", ObjectDetectionForm())
        return "new-object-detection"
    }

    /**
     * 提交图片并执行物体检测
     * @param form 前端上传表单数据
     * @param model 页面数据模型
     * @return 结果列表页面视图名
     */
    @RequestMapping("/object-detection/inbox", method = [RequestMethod.POST])
    fun detectObjects(@ModelAttribute form: ObjectDetectionForm, model: Model): String {
        // 未上传图片直接返回页面
        if(form.file?.originalFilename == null) {
            return "object-detection-inbox"
        }
        // 获取上传文件名称
        val fileName = form.file?.originalFilename ?: ""
        // 将图片文件上传至云端存储
        uploader.upload(form.file?.inputStream, form.file?.size ?:0, fileName)
        // 调用检测接口，设置30秒请求超时
        val results = apiClient.detect(fileName, true).block(Duration.ofSeconds(30))
        // 将检测结果格式化为美化JSON字符串
        val jsonResults = ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(results)
        // 打印检测日志
        LOG.info("Object detection results: {} ", jsonResults)
        // 封装数据回传给页面
        model.addAttribute("files", downloader.listFolder("inbox"))
        model.addAttribute("results", jsonResults)
        model.addAttribute("originalFile", form.file?.originalFilename)
        model.addAttribute("resultFile", form.file?.originalFilename.plus(".png"))
        return "object-detection-files"
    }

    /**
     * 读取待检测原图资源
     * @param fileName 图片文件名
     * @return 图片流资源
     */
    @RequestMapping("/object-detection/images/inbox/{file-name}")
    @ResponseBody
    fun getInboxImage(@PathVariable("file-name") fileName: String) : Resource {
        // 下载指定路径图片并封装为流资源返回
        return InputStreamResource(downloader.downloadStream("inbox/".plus(fileName)))
    }

    /**
     * 读取检测完成后的结果图片资源
     * @param fileName 结果图片文件名
     * @return 图片流资源
     */
    @RequestMapping("/object-detection/images/outbox/{file-name}")
    @ResponseBody
    fun getOutboxImage(@PathVariable("file-name") fileName: String) : Resource {
        return InputStreamResource(downloader.downloadStream("outbox/".plus(fileName)))
    }
}
