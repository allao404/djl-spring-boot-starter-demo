// 肖晴标注
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
// 表单实体类存放包
package com.aws.samples.djldemoweb.form

// 非空校验注解
import org.jetbrains.annotations.NotNull
// 接收前端上传的文件对象
import org.springframework.web.multipart.MultipartFile

/**
 * 物体检测提交表单实体
 * 用于绑定前端上传的图片文件数据
 */
class ObjectDetectionForm {
    // 上传的图片文件，非空必填项
    @NotNull
    var file: MultipartFile? = null
}
