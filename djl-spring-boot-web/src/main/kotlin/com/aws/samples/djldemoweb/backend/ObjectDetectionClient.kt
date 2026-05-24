# 肖晴标注
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
// 后端服务包名
package com.aws.samples.djldemoweb.backend

// 导入模型响应实体类
import com.aws.samples.djl.spring.model.InferenceResponse
// Spring服务注解
import org.springframework.stereotype.Service
// 响应式HTTP客户端
import org.springframework.web.reactive.function.client.WebClient
// 响应式数据流容器
import reactor.core.publisher.Mono

/**
 * 目标检测远程调用客户端
 * 负责发起HTTP请求，调用推理服务接口获取检测结果
 */
@Service  // 标识为Spring托管业务服务类
class ObjectDetectionClient(private val webClient: WebClient) {

    /**
     * 发起物体检测请求
     * @param file 待检测图片文件路径/标识
     * @param generateOutputImage 是否生成带检测框的结果图
     * @return 异步返回推理结果实体
     */
    fun detect(file: String, generateOutputImage: Boolean?): Mono<InferenceResponse> {
        // 构造GET请求，拼接接口地址与请求参数
        return this.webClient.get().uri{
            builder -> builder.path("/inference")          // 请求接口路径
                .queryParam("file", file)                 // 传入图片文件参数
                .queryParam("generateOutputImage", generateOutputImage)  // 绘图开关参数
                .build()
        }
        // 接收响应并解析为推理响应实体类
        .retrieve()
        .bodyToMono(InferenceResponse::class.java)
    }
}
