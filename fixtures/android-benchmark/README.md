# Android 模型能力考卷（Fixtures）

用于第 2 章「大模型选型」的**可复现评测素材**。不是完整可运行的 Android App（避免本仓库绑定 SDK/AGP 版本），而是**多模块 Gradle 片段 + Kotlin 考题 + 标准 Prompt**。

## 怎么用

1. 选一个场景目录，阅读其中的 `README.md`（验收标准）。
2. 将 `PROMPT.md` 全文复制到你的 AI 工具（Cursor / Claude Code / ChatGPT 等）。
3. 把同目录下的 `files/` 内容作为上下文附上，或拷入临时多模块工程。
4. 每个模型**只允许一次回答**，不追加 Prompt。
5. 按 README 里的检查清单打分（编译 / 最佳实践 / 边界）。

## 场景一览

| 目录 | 考什么 | 对应第 2 章 |
|------|--------|-------------|
| [`scenario-01-gradle-duplicate-class`](./scenario-01-gradle-duplicate-class/) | Room 传递依赖版本冲突、`exclude` / `force` / catalog | Gradle 多模块冲突 |
| [`scenario-02-api-vs-implementation`](./scenario-02-api-vs-implementation/) | 模块暴露类型与 `api`/`implementation` 声明 | 新增考卷 B |
| [`scenario-03-version-catalog`](./scenario-03-version-catalog/) | 只改 `libs.versions.toml` 引发连锁 | 新增考卷 C |
| [`scenario-04-sealed-when`](./scenario-04-sealed-when/) | `sealed` 事件 + `when` 穷尽 | 新增考卷 D |
| [`scenario-05-compose-chat-list`](./scenario-05-compose-chat-list/) | Compose 聊天列表（需求 + 验收，无预置代码） | Compose 聊天列表 |
| [`scenario-07-manifest-merge`](./scenario-07-manifest-merge/) | Manifest 合并冲突（debug exported / SDK 节点） | 第 8 章坑十一（不做工具打分） |
| [`scenario-08-layered-modules`](./scenario-08-layered-modules/) | app/feature/domain/data 引用溯源 | 第 3 章场景二扩展 |

第 3 章工具对比复用 **考卷 C** 时，请用 [`scenario-03-version-catalog/PROMPT-tools.md`](./scenario-03-version-catalog/PROMPT-tools.md)。

## 推荐评测顺序

**先跑 01 → 02 → 03 → 04**（Android 构建与 Kotlin 日常），再跑 05（UI 广度）。团队选型可只跑前四项，节省成本。

## 记录结果

建议用表格记录（模型 × 场景 × 是否通过验收），每季度复测一次。第 2 章文中的 2026-05 数据为示例，请以你本地复测为准。
