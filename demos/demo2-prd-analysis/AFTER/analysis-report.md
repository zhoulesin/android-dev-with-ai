# PRD 解析报告

> 由 LLM 根据 PRD.md + CLAUDE.md 自动生成 | 解析时间：2025-05-18

## 摘要

PRD 描述了一款「Android 开发者技术文章阅读 App」，共 8 个用户故事（4 P0 + 4 P1），4 个功能模块。技术栈对齐 CLAUDE.md 规范：Clean Architecture + Compose + Hilt + Room。

**核心决策**：本阶段不接真实 API，使用 FakeRemoteDataSource 从 assets/JSON 加载数据，模拟网络延迟 400ms。

## 模块划分

| 模块 | 类型 | 职责 | 对应故事 |
|------|:---:|------|---------|
| `:core:domain` | JVM Lib | Article 模型、Repository 接口、UseCase | 全部 |
| `:core:data` | Android Lib | Room 缓存、FakeDataSource、Repository 实现 | US-01,03,06 |
| `:core:designsystem` | Android Lib | 主题、通用 Composable | 全部 |
| `:feature:feed` | Android Lib | 首页文章列表 | US-01,02,03,04 |
| `:feature:article` | Android Lib | 文章详情 | US-02 |
| `:feature:bookmark` | Android Lib | 收藏管理 | US-04,05,06 |
| `:feature:search` | Android Lib | 搜索 | US-07,08 |
| `:app` | Application | DI 装配、NavHost | 全部 |

## 架构决策记录

### ADR-001：数据源选型

**决策**：使用 FakeRemoteDataSource + Room 离线缓存，而非直接接网络 API。
**理由**：MVP 阶段不引入真实后端依赖，FakeDataSource 模拟网络行为（延迟、错误），后续切换真实 API 只需替换 DataSource 实现。
**影响**：Repository 层设计为 Remote + Local 双数据源，为未来扩展预留接口。

### ADR-002：Feature 不依赖 Data

**决策**：遵循 CLAUDE.md，feature 模块不直接依赖 `core:data`。
**理由**：保持 Clean 分层，ViewModel 通过 UseCase 间接访问数据，便于单元测试和实现替换。
**影响**：需要在 domain 层为所有数据操作定义 UseCase。

### ADR-003：fake 数据格式

**决策**：articles.json 存储在 `app/src/main/assets/`，解析用 Android 内置 org.json。
**理由**：无需额外 JSON 解析库（不使用 Gson），减少依赖。
**影响**：数据变更直接修改 JSON 文件。

## 风险评估

| 风险 | 等级 | 缓解措施 |
|------|:---:|---------|
| 数据模型变更 | 低 | Article 用 data class，编译期安全检查 |
| Room 版本迁移 | 低 | MVP 为首次安装，无可迁移数据 |
| 收藏状态丢失 | 低 | Room 持久化，杀进程不丢 |
| 搜索性能 | 低 | 数据量 < 100 篇，SQLite LIKE 查询足够 |
| FakeDataSource 替换成本 | 中 | Repository 接口已抽象，替换只需新实现 |
