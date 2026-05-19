# Demo 4：编码实现 —— 从 spec 到完整业务代码

> 对应文章：[第 7 章 §7.3 开发阶段](../docs/07-接入项目全流程.md)

## 演示目标

展示如何将 PRD 拆解产物（demo2）+ 设计稿还原（demo3）交给 AI，完成全部业务代码实现。

## 输入（BEFORE）

| 文件 | 说明 |
|------|------|
| `task-breakdown-ref.md` | 引用 demo2 的任务拆解 spec |
| `PROMPT.md` | Prompt：按 spec 逐项实现 |

## 产出（AFTER）

20 个源文件，覆盖 4 层架构：

| 层 | 文件 | 说明 |
|----|------|------|
| domain | `Article.kt`, `ArticleRepository.kt`, 6 UseCase | 模型 + 接口 |
| data | `ArticleEntity.kt`, `ArticleDao.kt`, `ArticleDatabase.kt`, `FakeRemoteDataSource.kt`, `ArticleRepositoryImpl.kt`, `DataModule.kt` | Room + 数据源 |
| feature | `FeedViewModel.kt`, `ArticleViewModel.kt`, `BookmarkViewModel.kt`, `SearchViewModel.kt` | ViewModel |
| app | `MainActivity.kt` (NavHost), `AndroidManifest.xml` | 集成 |

## 验证要点

- [x] domain 模块无 `android.*` import
- [x] feature 模块不直接依赖 `core:data`
- [x] `fakeRemoteDataSource` 模拟网络延迟 400ms
- [x] Room 首次启动自动缓存文章
- [x] 收藏状态通过 Room 持久化

## 关键实践

1. **Spec 驱动编码**：AI 按 demo2 的任务清单逐项实现，不漏项
2. **CLADUE.md 持续约束**：feature 不依赖 data、domain 不引用 Android 等规则自动生效
3. **Fake 数据源降低依赖**：不接真实 API 也能完整验证数据流
4. **Hilt 自动装配**：AI 编写 @Module 后在 app 层自动组装依赖图
