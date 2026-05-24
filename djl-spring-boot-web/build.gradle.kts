// 导入文件IO与Kotlin编译任务相关类
import java.io.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// 插件配置区块
plugins {
	// SpringBoot项目核心插件
	id("org.springframework.boot")
	// Jib镜像打包插件，用于构建Docker镜像并推送仓库
	id ("com.google.cloud.tools.jib") apply true
	// Kotlin JVM编译插件
	kotlin("jvm")
	// Kotlin Spring框架适配插件
	kotlin("plugin.spring")
}

// 获取项目提交哈希值，用于镜像版本标记
val commitHash = ext.get("commitHash")

// Jib镜像构建参数配置
jib {
	// 基础运行镜像依赖
	from.image = "openjdk:13"
	// 推送目标AWS ECR镜像仓库地址
	to.image = "929819487611.dkr.ecr.us-east-1.amazonaws.com/djl-spring-boot-web"
	// 镜像标签：项目版本号+代码提交哈希
	to.tags = setOf(version.toString().plus("-").plus(commitHash))
}

// 项目依赖管理
dependencies {
	// 应用监控组件
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	// Thymeleaf模板页面渲染
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	// 响应式Web框架
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	// 传统Web框架
	implementation("org.springframework.boot:spring-boot-starter-web")
	// Kotlin序列化JSON工具
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	// 本地公共通用模块依赖
	implementation(project(":djl-spring-boot-common"))
	// AWS S3对象存储SDK
	implementation("com.amazonaws:aws-java-sdk-s3:1.12.281")
	// 本地模型实体模块依赖
	implementation(project(":djl-spring-boot-model"))
	// Kotlin反射库
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	// Kotlin标准库
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	// 单元测试依赖，排除旧版JUnit引擎
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
}

// 统一配置测试任务，使用新版JUnit5测试平台
tasks.withType<Test> {
	useJUnitPlatform()
}

// Kotlin编译全局配置
tasks.withType<KotlinCompile> {
	kotlinOptions {
		// 开启严格空安全校验
		freeCompilerArgs = listOf("-Xjsr305=strict")
		// 编译目标JDK版本
		jvmTarget = "11"
	}
}
