# demo-app 实施清单

> 本文档只列**可执行事项**与验收标准，不包含实现代码。  
> 模块结构与依赖规则见 [`README.md`](./README.md)。  
> 关联教程：仓库根目录 `docs/` 第 4、7、8 章及 `templates/`、`fixtures/`。

**状态图例**：`[ ]` 未开始 · `[~]` 进行中 · `[x]` 已完成

---

## 阶段 0：工程约定（开工前）

| 状态 | 事项 | 验收标准 | 关联 |
|:---:|------|----------|------|
| [x] | 确认 demo-app 位于本仓库 `demo-app/` 子目录，不单独维护第二套 templates | 文档改动以根目录 `templates/` 为准 | `templates/README.md` |
| [x] | 在团队内约定包名、applicationId（如 `com.example.aiworkflow`） | 写入 demo-app README，全模块 namespace 一致 | — |
| [x] | 锁定技术栈版本（AGP、Kotlin、Compose BOM、Hilt、minSdk） | `gradle/libs.versions.toml` 定稿且与 `templates/rules/CLAUDE.md` 描述一致 | 考卷 C |
| [x] | 配置静态检查（至少 ktlint 或 detekt 其一） | `./gradlew ktlintCheck` 或等价任务可跑 | 第 8 章坑五 |
| [x] | 明确模块依赖门禁：`feature` 不得依赖 `core:data` | 用 Gradle `dependencyAnalysis` 或 Code Review 清单约束 | 第 3 章场景二 |

---

## 阶段 1：内容与同步（文档 → App）

| 状态 | 事项 | 验收标准 | 关联 |
|:---:|------|----------|------|
| [x] | 维护 `tooling/sync-content.sh` 同步范围清单 | 脚本覆盖：`templates/rules/CLAUDE.md`、`templates/release-checklist.md`、`fixtures/android-benchmark/**` | — |
| [x] | 定义 assets 目录规范 | 文档写明 `app/src/main/assets/templates/`、`assets/fixtures/android-benchmark/` 结构 | README |
| [x] | 本地执行 sync，确认文件数量与源目录一致 | 执行脚本后 38 文件，spot-check 考卷 01、07、08 存在 | — |
| [x] | 决定 assets 是否入库 Git | 方案 A：gitignore + 开发前 sync，已写入 README | — |
| [~] | （若选 CI sync）在 CI 增加 sync 步骤 | PR 构建前自动 sync，缺失 assets 则失败 | 第 7 章 §7.0 |
| [x] | 编写「内容更新流程」：改 md → 跑 sync → 验 App | 已写入 demo-app README | 第 11 章 |

---

## 阶段 2：core 层（领域与数据）

### 2.1 core:model

| 状态 | 事项 | 验收标准 |
|:---:|------|----------|
| [x] | 定义 `ContentDocument`（id、title、path、category、bodyMarkdown） | 可表示 Rules / Prompt / README |
| [x] | 定义 `BenchmarkScenario`（id、考卷字母、标题、fixture 路径、摘要） | 与 fixtures 目录 scenario-01…08 一一对应 |
| [x] | 定义 `ChecklistSection` / `ChecklistItem`（分组、文案、是否勾选） | 能表达 release-checklist 分组结构 |
| [x] | 定义 `PlaybookTab` 枚举：Rules、Skills、Prompts | playbook UI 使用 |

### 2.2 core:common

| 状态 | 事项 | 验收标准 |
|:---:|------|----------|
| [x] | 定义统一 `Result` / 错误类型（可选） | domain 层不抛裸异常到 UI |
| [x] | 约定主线程 / IO 调度注入方式（为单测预留） | 文档说明 TestDispatcher 策略 |

### 2.3 core:domain（用例，无 Android 依赖）

| 状态 | 事项 | 验收标准 |
|:---:|------|----------|
| [x] | `GetHubDestinationsUseCase` | 返回三入口：配置、考卷、发布 |
| [x] | `ListPlaybookDocumentsUseCase` | 按 Tab 过滤 assets 内文档 |
| [x] | `GetPlaybookDocumentUseCase` | 按 id 取正文 Markdown |
| [x] | `ListBenchmarkScenariosUseCase` | 返回 7 个场景元数据（硬编码表 + assets 校验） |
| [x] | `GetBenchmarkDetailUseCase` | 返回 README + PROMPT 路径与正文 |
| [x] | `GetReleaseChecklistUseCase` | 解析 checklist 结构（通过 flow first） |
| [x] | `ToggleChecklistItemUseCase` | 切换勾选状态 |
| [x] | `ResetReleaseChecklistUseCase` | 恢复默认未勾选 |
| [x] | `ObserveReleaseChecklistUseCase` | 对外暴露 Flow 供 UI 订阅 |

### 2.4 core:data（实现，仅 app 通过 Hilt 装配）

| 状态 | 事项 | 验收标准 |
|:---:|------|----------|
| [x] | `AssetContentRepository`：从 assets 读 Markdown | 能读 CLAUDE.md、各 scenario 的 PROMPT.md / README.md |
| [x] | `BenchmarkCatalog`：场景 id 与目录映射 | 缺目录时返回明确错误，不崩溃 |
| [x] | `ReleaseChecklistRepository`：DataStore 持久化勾选状态 | 杀进程后勾选仍保留 |
| [x] | Hilt Module：绑定 domain 接口 → data 实现 | 仅 `app` 模块依赖 `core:data` |
| [x] | （可选）Room 表记录「最近一次打开的场景」 | V2；MVP 不做 |

### 2.5 core:designsystem

| 状态 | 事项 | 验收标准 |
|:---:|------|----------|
| [x] | `AiWorkflowTheme`（Material3 亮/暗） | 全 App 统一 |
| [x] | `AppScaffold`（TopBar、返回、分享/复制 action 槽位） | feature 复用 |
| [x] | `MarkdownBody` 或「纯文本预览」组件 | playbook / benchmark 详情可读 |
| [x] | `CopyButton` + 复制成功 toast | 考卷 PROMPT 一键复制 |
| [x] | `Loading` / `Error` / `Empty` 三态占位 | 各 feature 一致 |

---

## 阶段 3：feature 模块（UI）

### 3.1 feature:hub

| 状态 | 事项 | 验收标准 |
|:---:|------|----------|
| [x] | `HubScreen`：三张入口卡片（配置、考卷、发布） | 文案与图标清晰 |
| [x] | `HubViewModel`：调用 `GetHubDestinationsUseCase` | 无业务逻辑在 Composable |
| [x] | 导航事件：跳转 playbook / benchmark / release | 类型安全 route |
| [~] | 展示 sync 版本信息（可选）：assets 内 README 最后修改或构建时间 | 便于确认内容是否过期 |

### 3.2 feature:playbook（Rules | Skills | Prompts）

| 状态 | 事项 | 验收标准 |
|:---:|------|----------|
| [x] | 顶栏 Tab 或内部 Navigation 切换三栏 | 默认 Rules |
| [x] | **Rules**：列表 → 详情（CLAUDE.md 全文或分段） | 支持复制全文 / 复制当前段 |
| [x] | **Skills**：说明页 + 目录结构示意（链接 docs 第 4 章） | 首版只读，不接 Cursor API |
| [x] | **Prompts**：列表内置 Prompt 摘要（可从 fixtures 索引生成） | 点击进入详情 + 复制 |
| [x] | `PlaybookViewModel` + UiState（Loading/Success/Error） | 符合 templates Rules 约定 |
| [x] | 空态：sync 未执行时提示运行 `sync-content.sh` | 错误信息可操作 |

### 3.3 feature:benchmark

| 状态 | 事项 | 验收标准 |
|:---:|------|----------|
| [x] | 列表页：考卷 A–E + scenario 07/08 卡片（标题 + 一句话） | 与第 2、3 章命名一致 |
| [x] | 详情页：展示 README 验收标准 + PROMPT 全文 | 两个复制按钮：「复制 Prompt」「复制 README」 |
| [~] | 链接到仓库在线文档（可选）：GitHub 路径说明 | 方便分享 |
| [x] | 详情页底部：关联章节（如考卷 A → docs/02） | 教学闭环 |
| [x] | `BenchmarkViewModel` 处理不存在场景 id | 显示 Error 而非崩溃 |

### 3.4 feature:release

| 状态 | 事项 | 验收标准 |
|:---:|------|----------|
| [x] | 从 `release-checklist` 渲染分组勾选列表 | 与 templates 章节对应 |
| [x] | 勾选状态持久化（DataStore） | 退出重进保持 |
| [x] | 「全部重置」「复制为 PR 描述」两个操作 | 复制块格式与 templates 中 PR 区一致 |
| [x] | 未完成项数量提示（如 3/12） | 发包前一眼可见 |
| [x] | 文案提醒：勾选须人工确认，AI 仅可生成初稿 | 对齐第 7 章 §7.0 |

---

## 阶段 4：app 集成

| 状态 | 事项 | 验收标准 |
|:---:|------|----------|
| [x] | `Application` + `@HiltAndroidApp` | 编译通过 |
| [x] | `MainActivity` + 根 `NavHost` | hub 为 startDestination |
| [x] | 注册 feature 路由：playbook、benchmark、release | 类型安全 route |
| [x] | 依赖聚合：`app` 依赖全部 feature + `core:data` | 依赖图符合 README |
| [~] | 应用名、图标、启动屏（可选） | 可对外演示 |
| [~] | ProGuard / R8 规则（若开启 minify） | release 构建不丢 assets |

---

## 阶段 5：测试与质量

| 状态 | 事项 | 验收标准 |
|:---:|------|----------|
| [x] | `ListBenchmarkScenariosUseCase` 单元测试 | 通过：2 条且 id 唯一（Fake repo） |
| [x] | `ToggleChecklistItemUseCase` + Fake Repository 测试 | 通过：切换/重置/未知id 不崩溃 |
| [~] | `AssetContentRepository` 仪器测试或 Robolectric 读 assets | 需设备/模拟器运行 |
| [~] | 模块依赖测试（可选）：`feature:playbook` 不依赖 `:core:data` | Gradle 断言或架构测试 |
| [x] | 手动探索测试清单（见下「发布前手测」） | APK 构建成功，待设备验证 |

### 发布前手测（Manual QA）

| 状态 | 步骤 | 预期 |
|:---:|------|------|
| [ ] | 未 sync 安装运行 | 空态提示运行 sync 脚本 |
| [ ] | sync 后打开 Rules | 内容与 `templates/rules/CLAUDE.md` 一致 |
| [ ] | 打开考卷 A 详情 → 复制 Prompt | 剪贴板与 PROMPT.md 一致 |
| [ ] | release 勾选 3 项 → 杀进程 → 重开 | 仍为 3 项勾选 |
| [ ] | 「复制为 PR 描述」 | Markdown 含勾选框语法 |
| [ ] | 旋转屏 / 暗色模式 | 无状态丢失（release 勾选除外，应保留） |

---

## 阶段 6：文档与仓库联动

| 状态 | 事项 | 验收标准 |
|:---:|------|----------|
| [x] | 根目录 `README.md` 增加 demo-app 入口说明 | 已存在则复核链接 |
| [x] | `docs/07-接入项目全流程.md` 增加「配套 App 演示」一句 + 链接 | 读者知道有实操 |
| [x] | `docs/缺失项清单.md` 增加 demo-app 章节跟踪 | 与本文档阶段同步 |
| [~] | 第 2 章 / 第 3 章文末增加「在 demo-app 中打开考卷」链接（可选） | 闭环 |
| [~] | 录制 30–60 秒 GIF 或截图放入 `docs/images/`（可选） | 缺失项清单可勾选 |

---

## 阶段 7：V2  backlog（明确不做进 MVP）

| 事项 | 说明 |
|------|------|
| 真实 AI Token / 费用统计 | 需定义数据源（IDE 插件日志、手填、企业 API） |
| Skills 在线市场浏览 | 依赖外部 API 与鉴权 |
| App 内编辑并写回 `templates/` | 权限与 Git 流程复杂 |
| 对接 Cursor / Claude Code CLI | 非通用 MVP |
| `feature:stats` 独立模块 | 等有稳定数据源再拆 |
| 多语言界面 | 内容已是中文为主，UI 可先单语言 |

---

## 建议排期（参考）

| 周次 | 目标 | 对应阶段 |
|------|------|----------|
| 第 1 周 | 工程可编译 + sync + core:data 读 assets | 0、1、2.4 部分 |
| 第 2 周 | playbook（Rules）+ benchmark 列表/详情 | 2.1–2.3、3.2、3.3 |
| 第 3 周 | release 勾选 + hub + NavHost 集成 | 3.1、3.4、4 |
| 第 4 周 | 测试、文档联动、手测收尾 | 5、6 |

---

## 完成定义（Definition of Done · MVP）

满足以下全部条件即视为 MVP 完成：

1. 执行 `sync-content.sh` 后，App 可离线浏览 Rules、8 个 benchmark 场景、release 检查清单。  
2. 至少 3 处「一键复制」可用（Rules 全文、考卷 Prompt、PR 检查块）。  
3. release 勾选可持久化，并可复制为 PR Markdown。  
4. 模块依赖符合 README 禁止越界表。  
5. 根目录文档已链到 demo-app，实施清单阶段 0–5 核心项均为 `[x]`。

---

## 变更记录

| 日期 | 说明 |
|------|------|
| 2026-05 | 初版：基于 hub / playbook / benchmark / release 方案拆分 |
