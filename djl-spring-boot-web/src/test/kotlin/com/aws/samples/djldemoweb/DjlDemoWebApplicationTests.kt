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
// 项目测试类所在包
package com.aws.samples.djldemoweb

// JUnit5单元测试注解
import org.junit.jupiter.api.Test
// SpringBoot项目集成测试注解
import org.springframework.boot.test.context.SpringBootTest

/**
 * 项目启动上下文测试类
 * 用于校验Spring容器能否正常加载初始化
 */
@SpringBootTest
class DjlDemoWebApplicationTests {

    /**
     * 容器加载测试方法
     * 无业务逻辑，仅验证项目上下文正常启动
     */
	@Test
	fun contextLoads() {
	}

}
