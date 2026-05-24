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
// 网页控制器包路径
package com.aws.samples.djldemoweb.controller

// 引入网页控制器注解
import org.springframework.stereotype.Controller
// 引入请求路径映射注解
import org.springframework.web.bind.annotation.RequestMapping

/**
 * 首页访问控制器
 * 处理网站根路径请求，跳转首页页面
 */
@Controller
class IndexController {

    /**
     * 根路径请求映射
     * @return 首页视图页面名称
     */
    @RequestMapping("/")
    fun index(): String {
        // 返回首页模板视图
        return "index"
    }
}
