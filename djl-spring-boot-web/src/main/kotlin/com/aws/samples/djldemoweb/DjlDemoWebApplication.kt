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
// 项目主程序包
package com.aws.samples.djldemoweb

// 导入AWS客户端配置类
import com.aws.samples.djl.spring.common.AmazonClientConfiguration
// 日志工具类
import org.slf4j.Logger
import org.slf4j.LoggerFactory
// 读取配置文件参数注解
import org.springframework.beans.factory.annotation.Value
// SpringBoot启动核心注解
import org.springframework.boot.autoconfigure.SpringBootApplication
// 项目启动运行方法
import org.springframework.boot.runApplication
// 注册Bean组件注解
import org.springframework.context.annotation.Bean
// 导入外部配置类注解
import org.springframework.context.annotation.Import
// HTTP请求头相关类
import org.springframework.http.HttpHeaders
// 数据媒体类型枚举
import org.springframework.http.MediaType
// 响应式HTTP客户端
import org.springframework.web.reactive.function.client.WebClient

/**
 * SpringBoot项目启动主类
 * 负责项目初始化、配置导入、HTTP客户端Bean创建
 */
@SpringBootApplication
// 引入AWS相关客户端配置
@Import(AmazonClientConfiguration::class)
class DjlDemoWebApplication {
    // 日志打印对象
	companion object {
		val LOG: Logger = LoggerFactory.getLogger(DjlDemoWebApplication::class.java)
	}

    // 从配置文件读取后端推理服务接口地址
	@Value("\${djl.app.url}")
	lateinit var apiUrl: String

    /**
     * 注册HTTP请求客户端Bean
     * @param builder WebClient构建器
     * @return 配置完成的请求客户端实例
     */
	@Bean
	fun backendWebClient(builder: WebClient.Builder): WebClient {
		LOG.info("Initializing backend API client with url: {}", apiUrl)
        // 设置基础请求地址与默认请求头格式
		return builder.baseUrl(apiUrl)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}
}

/**
 * 程序入口函数，启动SpringBoot服务
 */
fun main(args: Array<String>) {
	runApplication<DjlDemoWebApplication>(*args)
}
