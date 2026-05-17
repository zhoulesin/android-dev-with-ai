# 第 5 章 · 高赞 AI Skills 对比

> 本章聚焦 GitHub 社区和 Superpowers 生态中热度最高的 Skills，按功能维度分组对比，帮助团队根据自身项目特点做出选择。每个对比项均基于实际 Android 项目的使用反馈。

---

## 5.1 Memory 类 Skills（规则记忆、上下文持久化）

**本节目标：** 理解 Memory 类 Skills 的运作机制，学会选择最适合自己项目的方案。

AI Coding 工具最致命的缺陷是"金鱼记忆"——每次新会话都要重新描述项目背景、技术栈、架构约定。Memory 类 Skills/规则文件正是为解决这个问题而生的。不同方案在持久化粒度、Token 成本和团队共享能力上差异巨大，选错方案等于每次对话都在交重复的"上下文税"。

### 5.1.1 核心方案对比

| 方案 | 功能 | 评分 | Token 成本 | 适合团队 |
|------|------|------|-----------|---------|
| **CLAUDE.md / AGENTS.md** | 原生规则文件，AI 工具启动时自动加载为 system prompt | ★★★★★ | 极高（全量注入） | 个人/小团队，项目规范性强的场景 |
| **memory-bank** | 跨会话持久化上下文，分层级存储项目记忆（brief → tech → patterns → progress） | ★★★★ | 中高（自动注入摘要层） | 中大型项目，多会话长期协作 |
| **spec-driven / spec-kit** | 规格驱动开发：先写规格文档，AI 严格按规格执行，偏离即报错 | ★★★★ | 中（按需加载单个 spec） | 需求明确的商业项目，大中型团队 |
| **repomix / repopack** | 打包整个（或部分）代码库为结构化上下文文件，一键喂给模型 | ★★★ | 高（一次性打包大量文件） | 遗留项目上手、跨模块重构、给外部 AI 提供代码上下文 |

### 5.1.2 深入对比

#### CLAUDE.md / AGENTS.md（原生规则文件）

**工作机制：** 工具（Claude Code / OpenCode / Gemini CLI）启动会话时，自动从项目根目录或 `~/.agents/` 读取 `.md` 文件，注入为 system prompt 前缀。内容可以定义：项目概述、技术栈、代码风格偏好、命名规范、常用命令、架构约束。

```
project-android/
├── CLAUDE.md          ← 顶层规则（Git 作用域：整个项目）
├── AGENTS.md          ← 备选方案（Gemini CLI 默认读取）
├── app/
│   └── CLAUDE.md      ← 模块级规则（Git 作用域：app 模块）
└── feature-login/
    └── CLAUDE.md      ← 功能级规则（Git 作用域：登录功能）
```

**实际收益示例（Android 项目）：**

```markdown
<!-- app/CLAUDE.md 片段 -->
## 技术栈约束
- UI：Jetpack Compose + Material3
- DI：Hilt（禁止手动注入）
- 网络：Retrofit + Moshi（禁止 Gson）
- 数据库：Room + Flow 返回
- 最低 SDK：26

## 命名规范
- Composable 函数：PascalCase，使用 `@Composable` 注解
- ViewModel：`[Feature]ViewModel` 后缀
- Repository：`[Feature]Repository` 后缀
- 资源命名：`feature_type_name`（如 `login_btn_submit`）
```

**缺点：** 每次会话全量注入，内容越多 Token 消耗越大。一个 500 行的 CLAUDE.md 可能导致每次对话额外消耗 1000+ token 的系统提示。没有层级化加载机制，只能靠 Git 作用域分区。

**评分：★★★★★（5/5）** —— 最原生、最稳定、工具支持最广。Android 项目首选基础方案。

<!-- TODO: 补充截图：CLAUDE.md 在 Claude Code 中的加载日志 -->

---

#### memory-bank

**工作机制：** 在 CLAUDE.md 之上增加了"记忆分层"概念，将会话间需要持久化的信息按访问频率和更新频率分级存储：

```
.cursor/instructions/
├── memory-brief.md      ← 第1层：项目一句话介绍（极少变化）
├── memory-tech.md       ← 第2层：技术栈、架构决策（迭代时更新）
├── memory-patterns.md   ← 第3层：代码模式、约定（逐渐积累）
└── memory-progress.md   ← 第4层：当前进度、待办（每会话更新）
```

每次会话只自动注入第 1-2 层，按需加载第 3-4 层，有效控制 Token 成本。

**与 CLAUDE.md 的关键区别：** CLAUDE.md 是"静态宪法"，memory-bank 是"动态日志"。CLAUDE.md 告诉你"项目应该怎么写"，memory-bank 告诉你"项目现在写到哪了"。

**适合场景：** 多分支并行开发、多人轮流用 AI 写代码、长期维护的项目。

**评分：★★★★（4/5）** —— 理念好但生态较新，需要自己维护记忆文件，缺乏自动化更新工具。

<!-- TODO: 补充截图：memory-bank 的 Cursor Rules 配置界面 -->

---

#### spec-driven / spec-kit

**工作机制：** 源自 Spec-Driven Development 运动（代表项目：GitHub spec-kit），核心流程是先写一份结构化的功能规格说明书（Spec），AI 严格按 Spec 执行。任何偏离 Spec 的行为都会被 AI 拦截。

```
docs/specs/
├── 2026-05-17-user-login.md   ← 登录功能规格
├── 2026-05-18-profile-page.md ← 个人页规格
└── ...
```

一份典型的 Android 功能 Spec 包含：
- 功能描述、验收标准（AC）
- UI 原型描述（Composable 结构预期）
- 数据模型定义（Kotlin data class）
- 状态管理策略（StateFlow / MutableState）
- 测试场景清单

**实际体验：** 以实现"用户登录功能"为例，先花 15 分钟写好 Spec，然后 AI 一小时内完成全部代码（Composable + ViewModel + Repository + 单元测试）。过程中 AI 几乎不需要提问，因为 Spec 已经回答了所有上下文问题。

**与 Brainstorming Skill 的关系：** 在 Superpowers 生态中，Brainstorming Skill 是 spec-driven 的"前置流程"——AI 通过提问帮你把模糊需求具象化为 Spec。

**评分：★★★★（4/5）** —— 大幅降低反复沟通成本，但前期写 Spec 本身有学习成本。适合需求明确的项目，不适合探索性原型。

---

#### repomix / repopack

**工作机制：** 将代码库按指定规则打包成一个（或几个）结构化文件，直接作为上下文喂给模型。支持 `.gitignore` 排除、自定义 include/exclude 规则、Token 预算控制。

```bash
# 打包 app 模块（排除 build 目录）
npx repomix app/ --ignore "build/,**/*.kt.bak" --output android-context.xml
```

生成的文件包含文件树结构 + 每个文件的内容，LLM 可以基于此理解整个模块。

**Android 项目特有痛点：** Android 项目有大量自动生成文件（`R.java`、`BuildConfig.java`、Hilt 生成的 `*_Factory.java`），不打标签直接打包会导致上下文体积爆炸。**建议：** 只用 `repomix` 打包手写源代码目录（`app/src/main/java`）。

**评分：★★★（3/5）** —— 适合一次性"批量理解"场景，但不适合日常开发（Token 成本太高）。用它做遗留项目上手或给外部 AI（如 ChatGPT Web）提供上下文是合理的。

---

### 5.1.3 动手实践

**任务：为你的 Android 项目创建分层 CLAUDE.md 机制**

1. 在项目根目录创建 `CLAUDE.md`，写入技术栈约束和全局命名规范（参考上文示例）
2. 在 `app/` 目录创建 `app/CLAUDE.md`，写入模块特有规则（如依赖注入约定、组件划分）
3. 在 `feature-login/` 目录创建 `feature-login/CLAUDE.md`，写入该功能的具体实现约束
4. 启动一次新的 AI 会话，测试 AI 是否遵循了各层规则
5. 观察 Token 消耗变化，决定是否需要精简某些层级

### 5.1.4 踩坑记录

| 踩坑 | 现象 | 解决方案 |
|------|------|---------|
| CLAUDE.md 太长 | 每次对话的前几个回复质量明显下降（Token 预算被 system prompt 侵占） | 控制在 300 行以内，用 Git 作用域分层，避免单文件超过 500 行 |
| CLAUDE.md 未被读取 | AI 无视你定义的 Kotlin 风格规则 | 检查文件是否在 Git 跟踪范围内、文件名是否正确（`CLAUDE.md` 非 `claude.md`）、检查工具版本是否支持 |
| memory-bank 文件过时 | AI 基于旧进度生成代码，覆盖了已完成的工作 | 每次会话结束时更新 `memory-progress.md`，或用 Git Hook 自动记录 |
| spec 写太细或太粗 | 太细：AI 全部照搬，没有发挥空间；太粗：AI 频繁提问打断流程 | Spec 聚焦"做什么"和"怎么验证"，不限定"怎么写"。给出接口签名，不规定实现细节 |
| repomix 打包了 build 目录 | 生成的上下文文件超过 200MB | 配置 `.repomixignore`，排除 `build/` `.gradle/` `*.class` `*.apk` 等 |

---

## 5.2 流程管理类 Skills

**本节目标：** 掌握从创意到交付的完整 AI 辅助流程链，判断哪些 Skill 值得引入 Android 项目。

流程管理类 Skills 定义了"AI 辅助开发的正确姿势"——不是让 AI 直接写代码，而是通过系统化流程让 AI 在正确的阶段做正确的事。以下对比基于 Superpowers 生态中经过大量实战验证的 Skills。

### 5.2.1 核心方案对比

| Skill | 阶段 | 流程完整性 | 学习成本 | Android 适配度 | 核心价值 |
|-------|------|-----------|---------|---------------|---------|
| **brainstorming** | 创意 → 设计 | ★★★★★ | 低 | ★★★★★ | 用结构化对话把模糊需求变成可落地的设计文档 |
| **writing-plans** | 设计 → 实施计划 | ★★★★★ | 低 | ★★★★★ | 把设计拆成 2-5 分钟一个的独立任务，每个任务含完整代码 |
| **executing-plans** | 计划 → 执行 | ★★★★ | 低 | ★★★★ | 按任务列表逐项执行，检查点验证，遇阻停止 |
| **subagent-driven-development** | 计划 → 执行（进阶） | ★★★★★ | 中 | ★★★★★ | 每个任务派给独立子 Agent 执行，任务间 Review 把关 |
| **test-driven-development** | 编码阶段 | ★★★★★ | 中 | ★★★ | Red-Green-Refactor 铁律，禁止无测试的代码 |
| **systematic-debugging** | 修 Bug 阶段 | ★★★★★ | 低 | ★★★★★ | 四阶段根因分析法，禁止症状修复 |
| **requesting-code-review** | Review 阶段 | ★★★★ | 低 | ★★★★★ | 派子 Agent 审查变更，输出分级的 Issues |
| **receiving-code-review** | Review 阶段 | ★★★★ | 低 | ★★★★★ | 接收 Review 反馈的正确姿势：验证 → 评估 → 实施 |
| **finishing-a-development-branch** | 交付阶段 | ★★★★ | 低 | ★★★★★ | 验证测试 → 提供合并/PR/保留/丢弃四种选择 |

### 5.2.2 流程链全景

Superpowers 生态定义了从创意到交付的完整流程链：

```
brainstorming → writing-plans → executing-plans / subagent-driven-dev → finishing-a-development-branch
     ↓                ↓                ↓（每任务）                             ↓
 设计 Spec        实施计划        TDD + Review                         交付决策
                                 systematic-debugging
                                 （出 Bug 时启用）
```

**实际案例：用流程链开发一个 Android 注销确认弹窗**

```
1. brainstorming: AI 提问 4 轮 → 产出 Spec（Dialog 样式、文案、取消二次确认）
2. writing-plans: AI 拆分为 8 个任务（Composable → ViewModel → Test → Theme → Navigation）
3. subagent-driven-development:
   Task 1: 写 AlertDialog Composable + 单元测试
   Task 2: Review → 修复 Issues → 继续
   Task 3: 写 ConfirmationViewModel + StateFlow + 测试
   ...
   Task 8: 集成 Navigation 路由 + 端到端验证
4. finishing: 所有测试通过 → 用户选择 "Push and create PR"
```

总耗时约 2 小时，其中 AI 编码约 1.5 小时，人工 Review 约 0.5 小时。

<!-- TODO: 补充截图：brainstorming 对话示例，展示 AI 提问 → 用户回答 → AI 产出 Spec 的交互过程 -->

### 5.2.3 TDD 在 Android 项目中的适配度分析

TDD Skill 的规则非常严格（"没有失败的测试就删代码重来"），在 Android 项目中面临几个现实挑战：

| 挑战 | 具体表现 | 应对策略 |
|------|---------|---------|
| Compose UI 测试 | `ComposeTestRule` 需要 Activity 宿主，测试启动慢（每次 5-10 秒） | 将逻辑抽到 ViewModel，UI 层只做薄薄的胶水层；ViewModel 单测启动 < 1 秒 |
| Android 依赖 | Room、Retrofit 等 SDK 需要 Android Context 或在 Robolectric 中运行 | 用 Repository 接口隔离，测试用 Fake Repository（内存实现） |
| Gradle 构建慢 | 每次 "Run test to verify it fails" 需要等待 Gradle 配置 | 将纯 Kotlin 模块（domain、data 接口层）抽为 `kotlin("jvm")` 模块，测试秒级执行 |
| 遗留代码无测试 | TDD 要求先写测试，但很多项目已有大量无测试代码 | 对新增功能严格 TDD，对修改的遗留代码先补"特征测试"再重构 |

**建议：** Android 项目中对 `domain` 和 `data` 层严格 TDD（收益极高），对 `presentation` 层适度放宽（ViewModel 逻辑 TDD，Composable UI 用截图测试或手动验证）。不要一开始就追求 TDD 全覆盖，从高价值模块切入。

### 5.2.4 动手实践

**任务：用流程链实现一个小功能**

1. 启动一次 AI 会话，明确告知要开发"设置页暗色模式切换"功能
2. 使用 brainstorming Skill，让 AI 通过提问帮你定义功能和验收标准
3. AI 产出 Spec 后，使用 writing-plans Skill 生成实施计划
4. 观察 AI 拆分任务的粒度，手动调整你认为不合理的地方
5. 选择 executing-plans 或 subagent-driven-development 执行（如果有子 Agent 支持优先后者）
6. 每完成 2-3 个任务，使用 requesting-code-review Skill 做中间审查
7. 全部完成后，用 finishing-a-development-branch Skill 处理代码交付

### 5.2.5 踩坑记录

| 踩坑 | 现象 | 解决方案 |
|------|------|---------|
| brainstorming 跳过 | 上去就让 AI 写代码，结果 AI 理解偏了方向，浪费 3 小时 | 任何超过 15 分钟的功能开发，先走 brainstorming。哪怕是"就加一个按钮"也可以快速走设计确认 |
| TDD 坚持太死 | Compose UI 测试写起来极其痛苦，TDD 阶段写了 50 行测试代码只测了一个按钮的 `onClick` | 区分测试策略：ViewModel/Repository 严格 TDD，Composable 用 Preview 和手动验证兜底 |
| 子 Agent 任务上下文不足 | 子 Agent 分配到"添加 Repository 层"任务，但不了解项目中已有的 Retrofit 配置，写出的代码不符合项目约定 | 给 writing-plans 的计划中每个任务标注依赖上下文（如"参考 `app/src/main/java/.../UserRepository.kt` 的写法"） |
| Review 流于形式 | requesting-code-review 返回的 Issues 每次都是"代码风格良好，无重大问题" | Review 子 Agent 需要清晰的检查清单，在 Plan 中指定具体 Review 标准（如"检查是否遵循 Hilt 注入规范"） |

---

## 5.3 Coding 规范类 Skills

**本节目标：** 理解如何用 Skills/Rules 文件约束 AI 生成代码的风格和质量，让 AI 写出的代码像团队成员写的一样。

Coding 规范类 Skills 的核心价值是**风格一致性**——AI 本身擅长写代码，但如果不加约束，同一项目中可能同时出现 `mutableStateOf` 和 `StateFlow`、`Glide` 和 `Coil`、`LiveData` 和 `Flow` 的混用。规范类 Skills 通过在 system prompt 中注入约束，让 AI 遵循统一风格。

### 5.3.1 规范分级策略

实际项目中建议将规范分为三级：

```
Level 1: 全局规则（CLAUDE.md）—— 所有对话自动加载
  ├── 技术栈声明
  ├── 架构模式
  ├── 命名规范
  └── 禁止事项

Level 2: 模块规则（模块级 CLAUDE.md）—— 按 Git 作用域加载
  ├── 模块特定依赖
  ├── 模块内组件划分
  └── 模块对外接口约定

Level 3: 按需规则（手动 @mention）—— 聊天中手动激活
  ├── 特定类库使用规则
  ├── 安全审查规则
  └── 性能检查列表
```

### 5.3.2 各类规范清单与推荐

#### 语言/框架规则

| 规则分类 | 典型内容 | 推荐级别 | 示例 |
|---------|---------|---------|------|
| **Kotlin Style** | 命名约定、Lambda 写法、作用域函数选择、`lateinit` vs `lazy` | ★★★★★ 必配 | "使用 `data class` 而非 POJO；优先 `val` 而非 `var`；禁止裸 `!!` 操作符" |
| **Compose Best Practices** | `remember` 使用时机、副作用处理、重组优化、状态提升 | ★★★★★ 必配 | "可观察状态使用 `StateFlow` + `collectAsStateWithLifecycle()`；禁止在 Composable 内启动协程" |
| **Gradle Conventions** | 依赖管理方式、模块化规范、构建配置共享 | ★★★★ 推荐 | "使用 version catalog (`libs.versions.toml`)；禁止模块间直接 `implementation` 传递依赖" |

#### 架构约束规则

| 规则分类 | 典型内容 | 推荐级别 | 示例 |
|---------|---------|---------|------|
| **Clean Architecture** | 分层依赖方向、DTO vs Domain Model、UseCase 粒度 | ★★★★ 推荐 | "domain 层无 Android 依赖；data 层实现 domain 接口；presentation 层只依赖 domain" |
| **MVVM 约束** | ViewModel 职责边界、State 建模、事件处理 | ★★★★★ 必配 | "ViewModel 暴露 `StateFlow<UiState>`；UI 事件用 `Channel` 或 `SharedFlow`；禁止在 ViewModel 中持有 Context" |
| **MVI 约束** | Intent/Action 定义、Reducer 纯函数、SideEffect 处理 | ★★★ 可选 | "Action 为 sealed class；Reducer 为纯函数；SideEffect 在 `LaunchedEffect` 中处理" |

#### 安全规范规则

| 规则分类 | 典型内容 | 推荐级别 | 示例 |
|---------|---------|---------|------|
| **ProGuard/R8** | 混淆规则检查、保留项声明 | ★★★ 推荐 | "Release 构建必须开启混淆；第三方 SDK 的 keep 规则写在独立文件" |
| **权限检查** | 运行时权限请求流程、Android 13+ 细分权限 | ★★★★★ 必配 | "所有 `Manifest.permission.*` 使用前检查 `ContextCompat.checkSelfPermission` 或 Accompanist 权限 API" |
| **密钥管理** | API Key 存储、签名密钥保护 | ★★★★★ 必配 | "禁止硬编码密钥；使用 `local.properties` + `BuildConfig` 注入；签名密钥只能存在于 CI 密钥库" |

### 5.3.3 规范落地实战：一个完整的 CLAUDE.md 示例

```markdown
# Android 项目 CLAUDE.md

## 技术栈
- Kotlin 1.9+, Compose BOM 2024.01.00
- Hilt 2.50, Room 2.6, Retrofit 2.9, Coil 2.5
- Coroutines + Flow (禁止 RxJava)
- Min SDK 26, Target SDK 34
- 测试：JUnit5 + Turbine + MockK + ComposeTestRule

## 架构规范
- 严格 Clean Architecture：domain / data / presentation 三层
- domain 层为纯 Kotlin 模块（`kotlin("jvm")` 插件）
- ViewModel 只暴露 `StateFlow<UiState>`，禁止暴露可变状态
- Repository 接口定义在 domain 层，实现在 data 层

## 代码风格
- 禁止 `!!`，需要用 `requireNotNull` 或 `?: error("...")`
- Composable 函数第一个参数：`modifier: Modifier = Modifier`
- 使用 `remember` + `mutableStateOf` 仅限局部 UI 状态
- 全局状态使用 `StateFlow` + `collectAsStateWithLifecycle()`

## 禁止事项
- 硬编码字符串（必须用 `R.string`）
- 硬编码颜色（必须用 MaterialTheme 或主题色）
- 在 ViewModel 中使用 `viewModelScope.launch` 启动长时间协程
- 直接 import `android.*` 到 domain 层
```

### 5.3.4 动手实践

**任务：为项目创建三级规范体系**

1. 诊断现状：用 AI 在项目中生成 3 个不同功能的代码片段，观察风格是否一致
2. 根据不一致之处，编写 Level 1 全局规则文件（CLAUDE.md）
3. 针对最复杂的一个模块，编写 Level 2 模块规则文件
4. 再次用 AI 生成代码，观察一致性改进效果
5. 记录加入规则前后的比较：代码风格一致性、AI 提问次数、生成代码的可用率

### 5.3.5 踩坑记录

| 踩坑 | 现象 | 解决方案 |
|------|------|---------|
| 规则太松 | AI 依然混用 LiveData 和 Flow，返工率高 | 把"推荐"改为"禁止"用语。AI 对"请尽量使用 Flow"的遵守度远低于"禁止 LiveData" |
| 规则太严 | AI 拒绝写任何 Compose 预览代码，因为规则说"禁止硬编码字符串" | 区分"生产代码规则"和"开发辅助代码规则"。Preview 中用 `@Preview` + 硬编码数据是合理的 |
| 规则冲突 | CLAUDE.md 说"Clean Architecture"，但项目实际结构是"MVVM 单模块" | 规则必须反映真实代码状态。先整理项目结构让它符合规范，再写规则。不要写"理想中"的规则 |
| 规则被忽略 | 安全规则说"检查权限"，但 AI 生成的代码直接调用了 `CAMERA` 权限 API | 核心规则用强调句式："⚠️ 强制规则：所有运行时权限在调用前必须检查" |

---

## 5.4 Android 专项 Skills

**本节目标：** 了解目前社区中专门面向 Android 开发的 Skills 或规则文件，评估引入价值。

Android 开发有独特的工具链和 API 生态，通用 Skills 在 Android 场景中需要适配。以下是目前最实用的 Android 专项 Skills/Rules。

### 5.4.1 当前可用的 Android 专项方案

| 方案 | 功能 | 评级 | 成熟度 | 说明 |
|------|------|------|--------|------|
| **Compose Preview Rules** | 约束 AI 生成的 Composable 包含 Preview、Label、多设备预览 | ★★★★ | 社区方案 | 通过 CLAUDE.md 注入规则实现 |
| **Compose Testing Rules** | 指导 AI 写 Compose UI 测试（`createComposeRule` + `onNodeWithText` + `performClick`） | ★★★ | 社区方案 | 需要配合 Robolectric 或 Instrumentation 环境 |
| **Gradle Dependency Rules** | version catalog 约束、依赖冲突检测、构建优化提示 | ★★★★ | 自建方案 | 在 CLAUDE.md 中定义 Gradle 约定 |
| **Room & Navigation & Hilt 规则包** | 针对三大 Android 架构组件的代码生成约束 | ★★★ | 自建方案 | 需要团队自己编写和维护规则 |
| **Android Security Rules** | TLS 配置检查、数据存储安全、WebView 安全、Intent 安全等 | ★★★★ | 自建方案 | 建议从 OWASP MASVS 提取关键检查项 |

### 5.4.2 自定义 Android 专项 Skill 的设计思路

目前专门面向 Android 的 AI Skills 生态还不够成熟，大部分需要团队自己编写。以下是基于实战总结的自定义 Skill 设计框架：

**Skill 设计三要素：**
1. **触发条件：** 明确什么场景激活该 Skill（如"当用户要求创建新的 Composable 组件时"）
2. **检查规则：** 具体而可验证的约束（如"检查是否使用了 `collectAsStateWithLifecycle()` 而非 `collectAsState()`"）
3. **示例代码：** 正确写法和错误写法的对比（AI 最擅长从对比中学习）

**自建 Compose Skill 示例框架：**

```
[Skill: compose-best-practices]
触发：用户要求编写或修改 @Composable 函数时自动激活

检查清单：
□ 是否使用 Modifier 参数（默认值 Modifier）
□ 是否将 Modifier 放在第一个可选参数位置
□ 状态提升是否正确（无不必要的 remember）
□ 是否避免在 Composable 内部启动协程
□ 是否包含 @Preview 函数
□ 是否有至少两个设备的 Preview（手机 + 平板/折叠屏）
□ 重组安全性（derivedStateOf 使用是否正确）
□ 动画 API 是否使用了正确的 Compose Animation 库
```

### 5.4.3 动手实践

**任务：创建你的第一个 Android 专项规则文件**

1. 选择项目中最频繁使用的一项技术（如 Hilt 注入）
2. 列出该技术最常见的 5 个易错点（如"在 Fragment 中使用 `@AndroidEntryPoint` 但忘记改父类"）
3. 将易错点转化为规则语句（每个规则包含：触发条件 + 检查项 + 正确示例 + 错误示例）
4. 写入项目根目录的 CLAUDE.md
5. 让 AI 基于这个规则生成新代码，观察是否避免了常见错误

### 5.4.4 踩坑记录

| 踩坑 | 现象 | 解决方案 |
|------|------|---------|
| Compose 版本差异导致代码不可用 | AI 生成的 Compose API 在项目实际使用的 BOM 版本中不存在 | 在 CLAUDE.md 中明确声明 `composeBom = "2024.01.00"` 或更具体的版本号 |
| Gradle 规则被 AI 误解 | AI 在 `build.gradle.kts` 中使用了 Groovy 语法 | 明确声明使用 Kotlin DSL：`build.gradle.kts`（注意 `.kts` 后缀） |
| Security 规则太泛 | AI 对每种数据存储都加了加密，导致 Room 查询性能下降 | 区分"敏感数据加密"和"普通数据存储"，给出具体场景而非一刀切 |
| AndroidX 命名混乱 | AI 交替使用 `androidx.compose.material3.*` 和 `androidx.compose.material.*` | 在规则中明确声明 Material 版本和导入路径约定 |

---

## 5.5 综合对比矩阵

**本节目标：** 在不同维度的综合对比中找到最适合自己团队的 Skills 组合。

### 5.5.1 核心 Skills 综合评分表

| Skill Name | 类别 | 综合评分 | 学习曲线 | Token 成本 | Android 适配度 | 维护活跃度 | 是否自建 |
|-----------|------|---------|---------|-----------|---------------|-----------|---------|
| CLAUDE.md / AGENTS.md | Memory | ★★★★★ | 极低 | 高 | ★★★★★ | N/A（工具原生） | 是 |
| memory-bank | Memory | ★★★★ | 中 | 中高 | ★★★★ | 中 | 是 |
| spec-kit | Memory | ★★★★ | 中 | 中 | ★★★★ | 高 | 否（开源） |
| repomix | Memory | ★★★ | 低 | 高（一次性） | ★★★ | 高 | 否（开源） |
| brainstorming | 流程管理 | ★★★★★ | 低 | 中 | ★★★★★ | 高（Superpowers） | 否 |
| writing-plans | 流程管理 | ★★★★★ | 低 | 中 | ★★★★★ | 高（Superpowers） | 否 |
| executing-plans | 流程管理 | ★★★★ | 低 | 中 | ★★★★ | 高（Superpowers） | 否 |
| subagent-driven-dev | 流程管理 | ★★★★★ | 中 | 高 | ★★★★★ | 高（Superpowers） | 否 |
| test-driven-development | 流程管理 | ★★★★★ | 中 | 低 | ★★★ | 高（Superpowers） | 否 |
| systematic-debugging | 流程管理 | ★★★★★ | 低 | 低 | ★★★★★ | 高（Superpowers） | 否 |
| requesting-code-review | 流程管理 | ★★★★ | 低 | 中 | ★★★★★ | 高（Superpowers） | 否 |
| receiving-code-review | 流程管理 | ★★★★ | 低 | 低 | ★★★★★ | 高（Superpowers） | 否 |
| finishing-a-dev-branch | 流程管理 | ★★★★ | 低 | 低 | ★★★★★ | 高（Superpowers） | 否 |
| Kotlin Style Rules | 编码规范 | ★★★★★ | 低 | 低 | ★★★★★ | N/A | 是 |
| Compose Rules | 编码规范 | ★★★★★ | 低 | 低 | ★★★★★ | N/A | 是 |
| Gradle Rules | 编码规范 | ★★★★ | 低 | 低 | ★★★★★ | N/A | 是 |
| Clean Architecture Rules | 编码规范 | ★★★★ | 中 | 低 | ★★★★ | N/A | 是 |
| MVVM/MVI Rules | 编码规范 | ★★★★★ | 低 | 低 | ★★★★★ | N/A | 是 |
| Security Rules | 编码规范 | ★★★★★ | 低 | 低 | ★★★★★ | N/A | 是 |
| Android 架构组件 Rules | Android 专项 | ★★★ | 中 | 低 | ★★★★★ | N/A | 是 |

### 5.5.2 按团队类型推荐组合

| 团队类型 | 推荐 Skills 组合 | 理由 |
|---------|----------------|------|
| **个人开发者 + 小项目** | CLAUDE.md + brainstorming + systematic-debugging | 极简组合，覆盖"不乱写"+"别跑偏"+"修 Bug 不猜" |
| **3-5 人团队 + 中型项目** | CLAUDE.md 分级 + spec-driven + writing-plans + subagent-driven-dev + Kotlin/Compose Rules | 加入流程管理和规范约束，保证多人 AI 输出一致 |
| **10+ 人团队 + 大型项目** | 全套 Superpowers 流程链 + memory-bank + 完整 Coding 规范 + 自建 Android 专项 Rules | 需要记忆持久化和严格的规范体系，适合有专人维护 Skills 的团队 |
| **遗留项目快速上手** | repomix + CLAUDE.md + systematic-debugging | 先让 AI 理解整个代码库，再用规则约束后续修改 |

### 5.5.3 决策流程图

```
开始
 │
 ├─ 项目已有 CLAUDE.md？
 │   ├─ 是 → 检查是否超过 500 行？
 │   │        ├─ 是 → 精简或分层
 │   │        └─ 否 → 继续
 │   └─ 否 → 创建 CLAUDE.md（优先级最高）
 │
 ├─ 团队 > 3 人？
 │   ├─ 是 → 引入 spec-driven 流程（brainstorming → writing-plans）
 │   └─ 否 → 至少使用 brainstorming
 │
 ├─ 有子 Agent 支持的工具？
 │   ├─ 是 → 用 subagent-driven-development 执行计划
 │   └─ 否 → 用 executing-plans
 │
 ├─ 编译耗时 < 10 秒（纯 Kotlin 模块）？
 │   ├─ 是 → 在 domain/data 层启用 TDD
 │   └─ 否 → TDD 仅限 domain 层
 │
 └─ 项目含敏感数据/高安全要求？
     ├─ 是 → 添加 Security Rules
     └─ 否 → 跳过
```

### 5.5.4 月度成本预估

以中等规模 Android 项目（5 人团队，每人每天 10 次 AI 会话）为例：

| 配置方案 | 每月 Token 消耗（约） | 每月费用估算（Claude Sonnet 水平） | 效率提升（相比不用 Skills） |
|---------|---------------------|--------------------------------|--------------------------|
| 无 Skills（裸用 AI） | 5M tokens | ~$15 | 1.0x（基准） |
| 仅 CLAUDE.md | 7M tokens（+40% 系统提示开销） | ~$21 | 1.3x |
| CLAUDE.md + 流程链 | 9M tokens | ~$27 | 1.8x |
| 全套（含 subagent-driven） | 14M tokens（子 Agent 额外会话） | ~$42 | 2.5x |

> 注：Token 费用为 2026 年价格参考，实际以各平台官方定价为准。效率提升倍数为内部团队自评数据，仅供参考。

<!-- TODO: 补充截图：实际项目中 Token 消耗面板截图 -->

### 5.5.5 动手实践

**任务：为你的团队做 Skills 选型**

1. 对照 5.5.2 表格，确定你团队的类型
2. 列出当前已在使用的 Skills/规则文件
3. 用 5.5.3 决策流程，找出缺失的关键环节
4. 优先引入缺失的 1-2 个 Skills，运行两周
5. 记录引入前后的对比数据：代码风格一致率、AI 提问次数、编码耗时、代码可用率
6. 根据数据决定下一轮引入哪些 Skills

### 5.5.6 踩坑记录

| 踩坑 | 现象 | 解决方案 |
|------|------|---------|
| Skill 堆砌 | 一次性引入 10+ 个 Skills，AI 行为怪异，频繁触发冲突的规则 | 渐进式引入，每轮只加 1-2 个，验证稳定后再加下一个 |
| 所有项目套用一个配置 | A 项目（Kotlin 多模块 + Compose）的规则用在 B 项目（Java 单模块 + XML）上完全不对 | 每个项目独立配置 CLAUDE.md 和 Skills，利用 Git 作用域区分 |
| 只配规则不验证 | 写了大量规则但没有定期检查 AI 是否遵守 | 每周用 AI 生成 10 个功能片段的代码，人工检查规则遵守率，低于 80% 就要加强规则表述 |
| 忽视 Token 成本 | 引入了全套 Skills 后月底发现账单翻了三倍 | 定期统计 Token 消耗分布，找出消耗最大的 Skill，评估性价比 |

---

## 本章小结

1. **CLAUDE.md 是地基，必须建好。** 不管选多少 Skills，一个清晰的项目规则文件是所有选择的起点。
2. **流程管理类 Skills 是 Android 项目最快的效率杠杆。** 尤其是 brainstorming → writing-plans → subagent-driven 这条链，能将"AI 写跑偏"的概率从 60% 降到 10% 以下。
3. **编码规范类 Skills 靠"禁止"不靠"推荐"。** AI 对模糊建议的遵从度远低于明确禁止。
4. **Android 专项 Skills 大部分需要自建。** 社区生态还在早期，但自定义编写并不复杂——本质就是检查清单 + 示例代码。
5. **渐进式引入，用数据说话。** 不要一次性引入所有 Skills，每个 Skills 引入后花两周验证效果再决定是否保留。

> **下一章预告：** 第 6 章 · Skills 筛选策略——当多个 Skills 功能重叠时如何决策，以及如何编写自定义 Skill 让你的 Android 项目如虎添翼。
