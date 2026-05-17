# Prompt（考卷 A）

```
你是一个 Android 构建专家。下面是一个 Kotlin DSL 多模块项目的 Gradle 片段。
编译失败：Duplicate class androidx.room...

请：
1. 说明根因（哪个模块引入了哪个版本的 Room）
2. 给出修改后的 build.gradle.kts / libs.versions.toml（完整片段）
3. 说明如何避免复发

约束：
- 保持 :core:database 使用 Room 2.6.1
- 优先使用 Version Catalog，不要只在子模块硬编码版本
- 不要删除 feature:home 对第三方 SDK 的依赖，用 exclude 或 force 处理传递依赖

【附上 files/ 目录下所有文件内容】
```
