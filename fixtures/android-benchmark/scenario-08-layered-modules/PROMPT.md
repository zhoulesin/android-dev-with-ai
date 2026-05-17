# Prompt（四层模块引用溯源）

```
项目为 app / feature:* / core:domain / core:data 分层。

请将 ProfileRepository.loadProfile() 的返回类型从 Profile 改为 Result<Profile>，
并更新所有调用方，要求：
- domain 接口与 data 实现同步修改
- feature:profile 内 ViewModel / UI 处理 Success/Error/Loading
- feature 模块不得依赖 core:data 实现类

完成后说明应执行哪条 Gradle 任务验证。

【附上 files/ 目录下相关 Kotlin 与 build.gradle.kts】
```
