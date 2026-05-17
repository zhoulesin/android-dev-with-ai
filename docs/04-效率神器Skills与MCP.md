# 第 4 章 · 效率神器：Skills 与 MCP

> **TL;DR** Skills 是给 AI 注入专业知识的最小单元，MCP 是 AI 连接外部工具的通信协议，Rules 文件是 AI 理解你项目的第一入口。三者配合，让 AI 从"通用助手"变成"你的专属 Android 架构师"。

---

## 4.1 Skills 机制深入

### 本节目标

理解 Skills 系统的内部工作原理，掌握从目录结构、SKILL.md 编写到触发机制和优先级管理的一整套方法。

### 前置知识

- 至少使用过一种 AI Coding 工具（Claude Code / OpenCode / Cursor / Copilot CLI）
- 了解 Markdown 基本语法（Front Matter、代码块）
- 有 Android 项目的基本目录结构认知

---

### 4.1.1 Skills 是什么

**Skills 是给 AI 注入专业领域知识的模块化插件。** 一句话概括：它是运行在 AI 模型"之上"的专家指令集。不同于单纯的一段 prompt，Skill 具有以下特性：

| 特性 | 普通 Prompt | Skill |
|------|------------|-------|
| 持久化 | 粘贴即忘，下次重写 | 文件持久化，自动加载 |
| 可复用 | 手动复制粘贴 | 触发词自动匹配 |
| 可组合 | 难，需手动拼装 | 多个 Skill 自动编排 |
| 版本管理 | 无法管理 | Git 管理、团队共享 |
| 领域知识 | 依赖当前上下文 | 内置专业指令 |

**工作原理：** 当用户输入匹配了 Skill 的触发条件时，AI 工具会把该 Skill 的完整指令注入到系统提示中。这相当于在每次相关对话开始前，自动给 AI 塞了一份"操作手册"。

```
┌──────────────────────────────────────────────────┐
│                   用户输入                         │
│           "帮我检查 Compose 代码"                  │
└──────────────────┬───────────────────────────────┘
                   │ 匹配触发词
                   ▼
┌──────────────────────────────────────────────────┐
│              Skill 引擎                            │
│  1. 扫描 .claude/skills/ 下所有 SKILL.md         │
│  2. 匹配 triggers 字段与用户输入                  │
│  3. 按优先级选择 Skill                           │
│  4. 将 Skill 内容注入 System Prompt              │
└──────────────────┬───────────────────────────────┘
                   │
                   ▼
┌──────────────────────────────────────────────────┐
│              增强后的 AI 对话                      │
│  System: "你是 Android 架构师。当检查 Compose     │
│  代码时，请关注：1. State 提升 2. 重组优化        │
│  3. Modifier 顺序..."                             │
│  User: "帮我检查 Compose 代码"                    │
│  AI: [按 Skill 指令执行专业审查]                  │
└──────────────────────────────────────────────────┘
```

<!-- TODO: 补充截图：Skill 触发匹配过程的调试日志 -->

---

### 4.1.2 Skills 的目录结构

一个规范组织的 Skills 目录如下：

```
.claude/skills/
├── android-compose-review/
│   ├── SKILL.md              # Skill 核心定义（必需）
│   └── references/            # 参考文档（可选）
│       ├── compose-best-practices.md
│       └── modifier-order-guide.md
├── systematic-debugging/
│   ├── SKILL.md
│   └── scripts/               # 可执行脚本（可选）
│       └── collect-logs.sh
└── gradle-helper/
    ├── SKILL.md
    └── references/
        └── common-gradle-errors.md
```

**层级说明：**

- `SKILL.md` 是入口文件，包含名称、描述、触发词、指令内容。工具只识别这个文件。
- `references/` 目录下的文件不会被自动注入，而是在 SKILL.md 中通过指令引用："当需要查看最新 Compose 最佳实践时，读取 references/compose-best-practices.md"
- `scripts/` 存放 Skill 可能引用的 shell/python 脚本，用于自动化操作

**关键设计原则：** Skill 目录名用 kebab-case 命名，引用文件用叙述性命名。一个好的目录名能帮助 Git 工作流中快速识别 Skill 用途。

---

### 4.1.3 SKILL.md 完整写法

一份规范的 SKILL.md 由 **Front Matter（元信息）** 和 **正文（指令体）** 两部分组成：

````markdown
---
name: android-compose-review
description: Review Jetpack Compose code for best practices and performance
triggers:
  - "review compose"
  - "检查 Compose 代码"
  - "compose code review"
  - "review compose code"
  - "帮我看看这个Composable"
  - "检查UI组件"
---

# Android Compose Review Skill

When reviewing Compose code, follow this checklist strictly:

## 1. State Management
- Check state hoisting: stateless composables should accept state as parameters
- Verify `remember` usage: expensive calculations must be wrapped
- Check `derivedStateOf` for derived values to prevent unnecessary recomposition
- Ensure `mutableStateOf` is not used inside ViewModel (use `MutableStateFlow` instead)

## 2. Recomposition Optimization
- No side effects in composable function body (use `LaunchedEffect`, `SideEffect`, `DisposableEffect`)
- Stable parameters: use `@Stable` or `@Immutable` annotation for custom classes
- Inline lambdas with `remember` to avoid recomposition of parent
- Avoid reading `LocalContext.current` or `LocalDensity.current` directly in top-level composable params

## 3. Modifier Order
- Modifiers are applied left-to-right: `Modifier.padding().clickable()` ≠ `Modifier.clickable().padding()`
- `Modifier.clickable` + `Modifier.background` order matters for ripple effect
- `Modifier.size` should precede `Modifier.padding` for correct layout

## 4. API Level Awareness
- Prefer stable APIs over `@Experimental` annotations
- Check minSdk compatibility — Material3 `PullToRefreshContainer` requires API 21+
- `rememberLauncherForActivityResult` requires `ComponentActivity`

## 5. Accessibility
- `contentDescription` for Image/Icons with non-decorative purpose
- `Modifier.semantics` for custom components
- `clickable` should have `role` parameter set (Button/Switch/Tab)

## Output Format
For each composable function reviewed, output:
- **Function**: `FunctionName`
- **Issues Found**: [count]
- **Severity**: 🔴 Critical / 🟡 Warning / 🟢 Suggestion
- **Details**: Line-by-line breakdown
- **Fix**: Code snippet of the corrected version
````

**Front Matter 字段详解：**

| 字段 | 必填 | 说明 | 示例 |
|------|------|------|------|
| `name` | 是 | 唯一标识符，kebab-case | `android-compose-review` |
| `description` | 是 | 一句话描述 Skill 功能 | `Review Compose code for best practices` |
| `triggers` | 是 | 触发词列表，用户输入包含任意一个即触发 | `["review compose", "检查代码"]` |

**正文编写原则：**

1. **用英语写指令**（AI 对英文指令理解更精准），触发词用中英文双覆盖
2. **使用祈使句**（"Check...", "Verify...", "Ensure..."），避免模糊表达
3. **分检查清单**（Checklist 格式优于段落文本），结构清晰，AI 不会遗漏
4. **指定输出格式**，让 AI 每次都按统一结构输出，方便后续自动化处理

<!-- TODO: 补充截图：一个真实的 SKILL.md 在 Claude Code 中触发后的 AI 输出效果 -->

---

### 4.1.4 Skills 的触发方式与优先级

**触发方式分两种：**

1. **自动匹配触发：** 用户输入中包含 triggers 列表中任一触发词时，Skill 自动加载。这是最常用的方式。
2. **手动指定触发：** 用户明确说"用 android-compose-review skill"，即使不包含触发词也会加载。适合需要精确控制 Skill 选择的场景。

**多个 Skills 的优先级处理：**

当多个 Skill 同时对用户输入命中触发词时，按以下规则处理：

```
1. 用户明确指定（"用 xxx skill"） > 自动匹配
2. triggers 完全匹配 > triggers 部分匹配
3. 触发词更长的 Skill > 更短的（更精确的匹配优先）
```

**冲突时的最佳实践：**

- 如果两个 Skill 指令有冲突（如一个要求用 Hilt，另一个要求用 Koin），AI 会提示用户澄清
- 建议在 SKILL.md 正文中声明依赖或互斥关系：

```markdown
## Dependency
- Requires: android-project-context (provides DI and architecture context)
- Conflicts: koin-di-skill (this skill assumes Hilt)
```

---

## 4.2 MCP 协议入门

### 本节目标

理解 MCP 协议的核心概念，掌握配置 MCP Server 的方法，体验 AI 直接操作外部工具的能力边界。

---

### 4.2.1 MCP 是什么

**MCP（Model Context Protocol）** 是 Anthropic 推出的 AI 与外部工具通信的开放标准协议。类比理解：

| 概念 | 类比 |
|------|------|
| AI 模型 | 大脑 |
| Prompt | 想说的话 |
| MCP | 神经系统（连接大脑与四肢） |
| MCP Server | 一只可以独立运作的手（GitHub API / Figma / 数据库） |
| MCP Tool | 手指（具体操作：createIssue、readFile、queryDB） |

MCP 解决的核心问题：**AI 不能直接操作外部世界**。以前你只能复制粘贴代码让 AI 分析；有了 MCP，AI 可以直接读 Figma 设计稿→生成代码→提交 PR→创建 Issue，全程不需要你手动中转。

```
┌──────────┐     MCP Protocol     ┌──────────────────┐
│          │◄────────────────────►│   MCP Server     │
│   AI     │   JSON-RPC over       │   (npx/node)     │
│  Client  │   stdio / SSE /       │                  │
│          │   streamable HTTP     │  ┌────────────┐  │
│          │                      │  │ GitHub API │  │
│          │                      │  │ Figma API  │  │
│          │                      │  │ SQLite DB  │  │
│          │                      │  └────────────┘  │
└──────────┘                      └──────────────────┘
```

---

### 4.2.2 核心概念：Server、Tool、Resource、Prompt

MCP 协议定义了四个核心抽象：

| 概念 | 说明 | AI 视角的类比 | Android 开发者类比 |
|------|------|-------------|-----------------|
| **Server** | MCP 服务的运行实例，提供一组 Tool/Resource/Prompt | 一个可调用的 API 网关 | 一个 Retrofit Service 接口 |
| **Tool** | Server 提供的一个可执行操作 | AI 可以调用的函数 | 一个 suspend 函数 |
| **Resource** | Server 暴露的结构化数据源 | AI 可以读取的文件/数据 | Room DAO 查询 |
| **Prompt** | 预定义的 Prompt 模板 | AI 可以引用的对话模板 | sealed class 定义的消息类型 |

**Tool 调用流程（以 GitHub MCP 创建 Issue 为例）：**

```
1. 用户："帮我在 android-app 仓库创建一个 Bug Issue，标题是 '登录崩溃'"
2. AI 分析意图 → 需要 create_issue 操作
3. AI 通过 MCP → GitHub Server 暴露的 create_issue Tool
4. Server 调用 GitHub REST API: POST /repos/owner/android-app/issues
5. 返回 Issue URL → AI 展示给用户
```

---

### 4.2.3 常见 MCP Server 一览

以下 MCP Server 与 Android 开发日常最相关：

| MCP Server | 能力 | Android 场景 | 配置难度 |
|------------|------|-------------|---------|
| **GitHub MCP** | 操作 Issues/PR/代码/Release | AI 读完代码直接提 PR，附带变更摘要 | ★☆☆ 低 |
| **Figma MCP** | 读取设计稿节点、样式、组件 | Figma 设计稿→AI 生成 Compose 代码 | ★★☆ 中 |
| **Jira/Linear MCP** | 查询/创建/更新任务 | Sprint 规划→AI 拆分子任务→自动分配 | ★★☆ 中 |
| **Filesystem MCP** | 沙盒化文件读写 | 批量化重命名、代码迁移脚本 | ★☆☆ 低 |
| **SQLite MCP** | 数据库查询、Schema 分析 | 调试 Room 数据库、分析数据异常 | ★☆☆ 低 |
| **PostgreSQL MCP** | 完整 SQL 操作 | 后端数据库联调、数据分析 | ★★☆ 中 |
| **Firebase MCP** | Firebase 项目管理 | Crashlytics 数据分析、Remote Config 更新 | ★★★ 高 |

---

### 4.2.4 MCP Server 配置实战

**步骤一：全局配置文件**

不同工具的配置文件位置不同：

| 工具 | 配置文件 | 示例路径 |
|------|---------|---------|
| Claude Code | claude_desktop_config.json | `~/.claude/claude_desktop_config.json` |
| OpenCode | opencode.json | 项目根目录 `opencode.json` |
| Cursor | .cursor/mcp.json | 项目根目录 `.cursor/mcp.json` |
| Continue | config.json | `~/.continue/config.json` |

**步骤二：配置 GitHub MCP Server**

```json
{
  "mcpServers": {
    "github": {
      "command": "npx",
      "args": [
        "-y",
        "@modelcontextprotocol/server-github"
      ],
      "env": {
        "GITHUB_PERSONAL_ACCESS_TOKEN": "<your-github-token>"
      }
    }
  }
}
```

**Token 获取：**
1. 打开 GitHub → Settings → Developer settings → Personal access tokens → Fine-grained tokens
2. 权限勾选：`Issues: Read and write`、`Pull requests: Read and write`、`Contents: Read`
3. 生成 token 后填入配置文件的 `<your-github-token>` 位置

**不要将含 token 的配置文件提交到 Git！** 建议将 token 放在环境变量中引用：

```json
{
  "mcpServers": {
    "github": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-github"],
      "env": {
        "GITHUB_PERSONAL_ACCESS_TOKEN": "${GITHUB_TOKEN}"
      }
    }
  }
}
```

**步骤三：添加 Figma MCP Server**

```json
{
  "mcpServers": {
    "github": { "command": "npx", "args": ["-y", "@modelcontextprotocol/server-github"] },
    "figma": {
      "command": "npx",
      "args": ["-y", "@anthropic/mcp-server-figma"],
      "env": {
        "FIGMA_PERSONAL_ACCESS_TOKEN": "${FIGMA_TOKEN}"
      }
    }
  }
}
```

**步骤四：验证配置**

重启 AI 工具后，输入以下命令验证 MCP 是否正常工作：

```
请列出你可用的 MCP Server 和 Tools
```

AI 应该返回类似：

```
可用的 MCP Server：
- github: 20 tools（create_issue, search_repositories, create_pull_request...）
- figma: 3 tools（get_file, get_component, get_styles...）
```

---

### 4.2.5 MCP 的实际使用场景

**场景：AI 分析 Crashlytics 崩溃并提 PR 修复**

```
1. "读取项目中 Crashlytics 最近 24 小时的 top crash"
   → AI 通过 MCP 调 Firebase Crashlytics API 获取数据

2. "分析 crash 堆栈，定位根因文件并读取相关代码"
   → AI 读取本地文件，分析崩溃原因

3. "生成修复代码，创建修复分支并提交 PR"
   → AI 通过 GitHub MCP：创建分支 → 写入文件 → 提交 → 创建 PR
   → PR 描述中自动附带崩溃分析和修复说明
```

<!-- TODO: 补充截图：AI 通过 MCP 自动提交 PR 的完整过程 -->

---

## 4.3 Rules 文件设计

### 本节目标

学会编写高质量的 CLAUDE.md / AGENTS.md，让 AI 在每次会话开始时自动理解你的项目上下文，避免重复解释。

---

### 4.3.1 Rules 文件 vs Skills 文件

这两个概念经常被混淆，结合上一小节（Skills 机制）做一个清晰对比：

| 维度 | CLAUDE.md / AGENTS.md | SKILL.md |
|------|----------------------|----------|
| **本质** | 项目宪法（静态背景） | 工作流程（动态指令） |
| **加载时机** | 每次会话自动加载 | 触发词匹配后加载 |
| **内容** | 项目是什么、怎么构成的 | 遇到某类任务该怎么做 |
| **触发条件** | 无条件，始终生效 | 条件触发（含触发词时才加载） |
| **Token 影响** | 全量注入，每次消耗 | 按需注入，省 Token |
| **更新频率** | 随项目演进半年一更 | 随流程优化随时更新 |
| **典型内容** | 技术栈、架构约定、命名规范 | 代码审查清单、调试流程、测试模板 |

**配合使用的最佳实践：**

Rules 文件负责"是什么"，Skill 负责"怎么做"。AI 读取 Rules 文件知道项目用 Compose + Hilt，然后当你说"帮我写个登录页"，Composite-review Skill 被触发，指导 AI 如何正确地写一个符合项目约定的 Compose 页面。

---

### 4.3.2 CLAUDE.md 核心要素

一份高质量的 Android 项目 CLAUDE.md 应覆盖以下六个模块：

```markdown
# Project: Cozyla Android

## 1. 技术栈（Technology Stack）
- Language: Kotlin 2.0 + KSP
- UI: Jetpack Compose 1.7 + Material3
- Architecture: MVVM + Clean Architecture (3-layer)
- DI: Hilt (禁止手动注入)
- Build: Gradle KTS 8.x + Version Catalog (libs.versions.toml)
- Network: Retrofit 2.9 + Moshi (禁止 Gson)
- Database: Room 2.7 + Flow 返回
- Async: Kotlin Coroutines 1.9 + Flow
- Testing: JUnit5 + MockK 1.13 + Turbine 1.1 + Compose Testing
- Min SDK: 26, Target SDK: 35
- Image: Coil 3.0 (禁止 Glide)

## 2. 架构约定（Architecture Rules）
- ViewModel 暴露 StateFlow<UiState>，不暴露 LiveData
- Repository 返回 Flow<T>，不返回 suspend fun（除非单次查询）
- UseCase 是单一职责的无状态类，用 operator fun invoke()
- 数据层不可持有 UI 层引用（禁止 Context 传入 Repository）
- 网络模型与 UI 模型分离：Dto（data transfer object）→ Domain Model → UiState

## 3. 模块结构（Module Structure）
```
app/                  # DI 装配、Navigation、Application
├── feature/          # 功能模块（独立 Gradle Module）
│   ├── login/
│   ├── home/
│   └── profile/
├── core/             # 核心模块
│   ├── network/      # Retrofit + OkHttp 配置
│   ├── database/     # Room Database
│   ├── model/        # Domain Model
│   ├── ui/           # Design System、通用 Composable
│   └── common/       # 工具类、扩展函数
└── data/             # Repository 实现层
```

## 4. 命名规范（Naming Conventions）
- Composable 函数：PascalCase，无 `@Composable` 后缀，文件对应函数名
- ViewModel：`[Feature]ViewModel`（如 `LoginViewModel`）
- Repository：`[Feature]Repository` 接口 + `[Feature]RepositoryImpl` 实现
- UseCase：`[Action][Entity]UseCase`（如 `LoginUserUseCase`、`GetArticlesUseCase`）
- State：`[Feature]UiState` data class 或 sealed interface（如 `LoginUiState`）
- Event/Action：`[Feature]Action` sealed interface（如 `LoginAction`）
- 资源命名：`feature_type_name`（如 `login_btn_submit`、`home_ic_notification`）

## 5. Coding 规则（Coding Rules）
- 集合用 `Immutable` / `Persistent` 前缀类名（或 `@Immutable` 注解的稳定类）
- 日志用 `Timber`，不用 `Log.*`
- 字符串用 `R.string`，不硬编码
- `remember` 包裹的高开销计算用 `key` 参数指定依赖
- ViewModel 用 `viewModelScope.launch`，避免 `GlobalScope`
- Flow.collect 在 `LaunchedEffect` 或 `repeatOnLifecycle` 中进行
- 禁止在 Composable 中执行网络请求或数据库操作
- 错误处理统一用 sealed class `Result<T>`（Success/Error/Loading）

## 6. 测试规则（Testing Rules）
- ViewModel 测试：MockK + Turbine + StandardTestDispatcher
- Repository 测试：InMemory Room + Fake Retrofit
- Compose UI 测试：ComposeTestRule + semantics 匹配
- JUnit5 为默认测试框架（不使用 JUnit4）
- 测试文件命名：`[ClassUnderTest]Test.kt`，放在 `test/` 对应的包下
- 一个 test 只测一个行为
```

<!-- TODO: 补充截图：CLAUDE.md 在 Claude Code 启动时的加载日志 -->

---

### 4.3.3 AGENTS.md 写法

AGENTS.md 是给 Subagent 系统（如 OpenCode 的 Subagent、Superpowers 的 Task 系统）用的规则文件。它告诉 Subagent 如何使用 Skills 和工作流：

```markdown
# AGENTS.md - Subagent 调度规则

## Skill 调用约定
- 编码任务前必须先调用 `systematic-debugging` → `writing-plans`，不可跳过
- 所有 Pull Request 前调用 `android-compose-review`
- 跨文件修改调用 `dispatching-parallel-agents`，提取独立子任务分发

## 项目特性清单
- 所有网络请求均为 Retrofit + Moshi，禁止使用 HttpURLConnection
- 所有 Compose 导航通过 `NavHost`，禁止 Fragment 导航
- StateFlow 默认搭配 `WhileSubscribed()` 策略

## Subagent 安全边界
- 不允许修改 `build.gradle.kts` 文件（依赖变更需人工 Review）
- 不允许执行 `git push --force`
- 不允许提交 `.env`、`local.properties`、`google-services.json`
```

---

### 4.3.4 Rules 文件的层级与作用域

Rules 文件支持 Git 作用域机制，不同目录下的 CLAUDE.md 只对同级及子目录生效：

```
project-android/
├── CLAUDE.md                    ← 全局规则（所有对话均加载）
├── app/
│   └── CLAUDE.md                ← app 模块规则（覆盖全局规则中的 UI 规范）
├── core/network/
│   └── CLAUDE.md                ← 网络层规则（覆盖全局规则中的网络规范）
└── feature/login/
    └── CLAUDE.md                ← 登录模块规则（覆盖全局规则中的业务逻辑）
```

**作用域覆盖规则：**
- 子目录 CLAUDE.md 的配置会覆盖父目录的同名字段
- 如果修改的是 `app/core/` 目录下的文件，优先读取 `app/CLAUDE.md`，再回退到根目录 `CLAUDE.md`

**TL;DR：** 全局规则写通用约束，模块级规则写领域约束。不要把所有内容塞进根目录 CLAUDE.md，否则 Token 浪费。

---

## 动手实践

### 实践 1：为你的项目创建 CLAUDE.md

1. 在项目根目录创建 `CLAUDE.md`
2. 参考 4.3.2 的六个模块框架，填写以下关键信息：
   - 技术栈（Kotlin 版本、Compose 版本、DI 框架、网络层、数据库）
   - 命名规范（Composable、ViewModel、Repository 的命名风格）
   - 架构约束（ViewModel 如何暴露状态、Repository 返回类型）
3. 用一段英文描述项目的核心业务，2-3 句话即可
4. 将文件加入 Git，团队成员拉取后即可生效

**验证方式：** 新开一个 AI 会话，问"这个项目使用什么 DI 框架？"——AI 应该不假思索地答出 Hilt/Koin 等。

---

### 实践 2：写一个自定义 Skill —— 检查 API 调用线程安全

在 `.claude/skills/android-thread-check/` 下创建 `SKILL.md`：

```markdown
---
name: android-thread-check
description: Verify Android API calls are on correct threads
triggers:
  - "check thread"
  - "检查线程"
  - "thread safety"
  - "线程安全"
---

# Android Thread Check Skill

When checking thread safety of Android code, verify:

## Rules
1. Network calls (Retrofit) must be on Dispatchers.IO
2. Room database writes must be on Dispatchers.IO
3. Room database reads returning Flow are safe on any dispatcher
4. Room suspend queries must be on Dispatchers.IO
5. UI updates must be on Dispatchers.Main
6. SharedPreferences/DataStore writes must be on Dispatchers.IO
7. ViewModelScope.launch defaults to Dispatchers.Main — wrap IO calls with `withContext`

## Common Anti-patterns
- `viewModelScope.launch { api.fetchUser() }` → Missing `withContext(IO)`
- `suspend fun fetch() = withContext(Main) { ... }` → Wrong context
- `Flow.collect` in Repository without `flowOn(IO)` → Slow stream on caller thread

## Output
For each violation found:
- **File**: path + line number
- **Violation**: description
- **Fix**: corrected code snippet
```

**验证方式：** 在 AI 对话中输入"检查 UserRepository.kt 的线程安全"，AI 应按以上规则进行审查。

---

### 实践 3：配置 GitHub MCP Server

1. 在 GitHub 生成 Personal Access Token（Fine-grained token，勾选 Issues + Pull requests 权限）
2. 按 4.2.4 的 JSON 格式配置到 AI 工具的 MCP 配置文件中
3. 重启 AI 工具
4. 测试命令：

```
请在 [你的仓库名] 创建一个 Issue，标题是 "验证 MCP 集成"，内容为：
"这是一个测试 Issue，用于验证 AI 通过 MCP 操作 GitHub 的能力。"
```

**预期：** AI 应执行 `create_issue` Tool 调用，返回一个 GitHub Issue 链接。

<!-- TODO: 补充截图：MCP 配置验证的完整对话截图 -->

---

## 踩坑记录

### 坑 1：SKILL.md 触发词太短导致误触发

**现象：** Skill 的 triggers 设置了 `["code"]`，导致几乎每次对话都被触发，AI 行为完全跑偏。

**根因：** 触发词是部分匹配，`"code"` 会匹配到所有包含 "code" 的输入（code review、codebase、encode 等）。

**解决：** 触发词至少 3 个英文单词或 4 个中文字，如 `"check compose code"`、`"检查 Compose 代码"`。避免单字触发词。

---

### 坑 2：MCP Server 启动失败但不报错

**现象：** 配置了 MCP Server，但执行相关操作时 AI 说"我没有可用的工具"，或直接走兜底逻辑。

**排查步骤：**
1. 检查 JSON 配置是否有效（缺失逗号、引号不匹配），用 jsonlint.com 验证
2. 确认 `npx -y @modelcontextprotocol/server-github` 在终端中能正常运行（`npx -y` 确保自动安装）
3. 检查环境变量名是否正确（GitHub Server 需要 `GITHUB_PERSONAL_ACCESS_TOKEN`，不是 `GITHUB_TOKEN`）
4. 如果使用 `${GITHUB_TOKEN}` 引用环境变量，确认 shell 中该变量已 export：`echo $GITHUB_TOKEN`
5. 查看 AI 工具的控制台日志（如 Claude Code 的 Developer Mode），搜索 MCP 相关报错

**解决：** 大部分情况下是 token 缺失或 npx 安装被防火墙拦截。先在终端中运行对应 npx 命令确认网络可达。

---

### 坑 3：MCP 操作权限过大

**现象：** AI 通过 GitHub MCP 误删了一个远程分支，或创建了大量垃圾 Issue。

**根因：** Personal Access Token 权限开太大（admin 权限），且没有给 AI 足够的约束指令。

**解决：** 安全三原则：

1. **Token 最小权限：** 只给当前需要的权限（如只读 Contents、读写 Issues），不要给 admin
2. **指令约束：** 在 CLAUDE.md 或 AGENTS.md 中明确声明：

```markdown
## MCP 安全约束
- 不允许通过 GitHub MCP 删除分支
- 不允许 force push 到 main/master
- 创建 Issue 前需人工确认标题
```

3. **使用 Fine-grained Token：** 限定到特定仓库，不是全账号生效

---

### 坑 4：CLAUDE.md 太详细导致 Token 爆炸

**现象：** CLAUDE.md 写了 500 行，每次问答都要额外消耗 2000+ token 的系统提示。用了几十次对话后，Token 配额提前耗尽。

**根因：** CLAUDE.md 是每次会话全量注入的，内容越多成本越高。但你也不知道哪些内容会真正用到。

**解决：** 区分"高频必加载"和"低频按需加载"：

- **高频内容（放 CLAUDE.md）：** 技术栈核心约束、命名规范、模块结构（控制在 100-200 行）
- **低频内容（放 Skill）：** 详细的代码审查清单、测试模板、构建脚本排错指南、API 文档索引

可以给你的 CLAUDE.md 内容分级：

```markdown
## Core Rules（每次加载）
- Hilt for DI
- Compose + Material3
- Kotlin Coroutines + Flow

## Extended Rules（按需加载）
详细约束请参考 Skills: android-project-context
```

---

### 坑 5：忘记 Security 约束导致 AI 提交敏感文件

**现象：** AI 在执行 git 操作时，把 `.env`、`local.properties`、`google-services.json` 一起提交了。

**解决：** 在 CLAUDE.md / AGENTS.md 中显式声明敏感文件黑名单：

```markdown
## 禁止提交的文件
- .env
- local.properties
- google-services.json
- *.keystore
- *.jks
- sentry.properties
- crashlytics.properties
```

同时在 `.gitignore` 中确保这些文件已被忽略（双重保险）。

---

## 本章小结

| 工具 | 解决什么问题 | 配置难度 | 收益 |
|------|------------|---------|------|
| Skills | 给 AI 注入专业工作流，告别重复 prompt | ★☆☆ 低 | 即时见效 |
| MCP | 打通 AI 与外部工具的数据通道 | ★★☆ 中 | 团队共享 |
| Rules 文件 | 消除每次对话的上下文复述 | ★☆☆ 低 | 即时见效 |

三者构成一个闭环：**Rules 告诉 AI "你是谁"，Skills 告诉 AI "你怎么做"，MCP 让 AI "能做到"**。

下一章将对比社区中热度最高的 Skills，帮你从数十个选项中挑出最适合 Android 项目的那些。
