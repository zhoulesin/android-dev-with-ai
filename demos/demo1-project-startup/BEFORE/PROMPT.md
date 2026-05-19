# Demo 1 Prompt：根据 CLAUDE.md 搭建项目脚手架

你是一位 Android 架构师，现在需要根据项目宪法 `CLAUDE.md` 创建一个多模块 Kotlin Compose 项目。

## 输入文件
- CLAUDE.md（同上目录）

## 任务

1. 创建 Gradle 多模块项目，包含以下模块：
   - `:app`（Application + HiltAndroidApp + NavHost）
   - `:core:domain`（纯 JVM，无 Android 依赖）
   - `:core:data`（Room + 数据源实现）
   - `:core:designsystem`（Compose 主题 + 通用组件）
   - `:feature:feed`（文章列表）
   - `:feature:article`（文章详情）
   - `:feature:bookmark`（收藏管理）
   - `:feature:search`（搜索）

2. Gradle 配置要求：
   - Version Catalog（`gradle/libs.versions.toml`）单一管理版本
   - AGP 8.7.3 · Kotlin 2.0.21 · Compose BOM · Hilt 2.52 · Room 2.6.1
   - ktlint 静态检查
   - minSdk 26 · targetSdk 35
   - KSP 替代 kapt

3. 架构约束（严格按 CLAUDE.md）：
   - `feature:*` → 依赖 `core:domain` + `core:designsystem`，**禁止依赖 `core:data`**
   - `core:domain` 纯 JVM 模块，不依赖 Android/Compose
   - `core:data` 实现 domain 接口，用 @Module 绑定
   - Hilt 在 `:app` 装配依赖图

4. 模块依赖规则：
   - 模块间暴露类型用 `api`
   - 内部实现用 `implementation`

5. 包名：`com.example.bestpractice`

## 产出要求
- 项目可直接 `./gradlew :app:assembleDebug` 编译通过
- 包含一个可展示的界面占位

## ⚠️ 验证要点
- [ ] `feature:feed` 的 `build.gradle.kts` 中不存在 `project(":core:data")`
- [ ] `core:domain/build.gradle.kts` 不存在 `android-library` 插件
- [ ] `libs.versions.toml` 是所有版本号的唯一来源
- [ ] 项目编译通过
