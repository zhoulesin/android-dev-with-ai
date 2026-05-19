# Demo 5 Prompt：为业务代码编写测试

你是一位 Android 测试工程师，需要为已完成的业务模块编写单元测试。

## 输入
- demo4 产出的全部代码
- 项目 CLAUDE.md（测试规范）

## 任务

### 1. Domain 层单测（纯 JVM）
- `ArticleRepository` 的 Fake 实现
- `GetArticlesUseCase` 测试
- `ToggleBookmarkUseCase` 测试
- `SearchArticlesUseCase` 测试

### 2. ViewModel 测试
- `FeedViewModel` 使用 MockK + Turbine
- 验证 Loading/Success/Error 三态流转
- 验证 toggleBookmark 调用

## 测试规范（参照 CLAUDE.md）
- 使用 JUnit + MockK + Turbine
- 使用 `runTest` + `StandardTestDispatcher`
- `StateFlow` 用 Turbine 的 `test { }` 验证
- 禁止 `Thread.sleep()`

## 输入
- `../demo4-implementation/AFTER/` 完整代码
