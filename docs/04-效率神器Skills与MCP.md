# Skills 和 MCP：AI 编程的真正威力放大器

> 时效性标注
> - 最后更新：2026-05
> - 统计周期：2026-01 ~ 2026-05（工具配置与实践）
> - 适用说明：MCP 能力、权限策略与客户端支持可能变化，接入前请核对官方文档

用了半年 AI 编程，我发现真正的分水岭不是模型，不是工具，而是 Skills 和 MCP。

Claude、GPT、Gemini——各家模型在代码生成质量上的差距其实没那么大。**真正拉开效率差距的，是你有没有给 AI 装上"专业大脑"和"外部手脚"。**

这篇聊聊我如何一步步把 AI Coding 从"偶尔有用"调教到"离不开"的状态。

---

## 那个让我醍醐灌顶的瞬间

忘了在哪一周了，我在做一个 Compose 项目，每次让 AI 帮忙写 UI 代码，都会出现同样的低级错误：

```kotlin
// AI 经常写出这种有问题的代码
@Composable
fun UserCard(user: User, modifier: Modifier = Modifier) {
    val context = LocalContext.current  // 每次重组都重新获取 Context
    var name by remember { mutableStateOf(user.name) }  // remember 应该跟 key
    
    LaunchedEffect(Unit) {
        // 副作用没有明确的 key 依赖，容易出 bug
        fetchUserDetails(user.id)
    }
}
```

每次我都要手动指正："重组优化呢？State 提升呢？Modifier 顺序呢？"

直到有一天，我给项目加了一个 Compose Review Skill。内容就两三百行——几份检查清单、几条最佳实践、一个输出格式模板。**再让 AI 写 Composable 代码时，质量直接上了一个台阶：**

```kotlin
@Composable
fun UserCard(
    user: User,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),  // padding 放在最后，不影响 ripple
        enabled = !isLoading,
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .semantics { role = Role.Button },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = user.avatarUrl,
                contentDescription = user.name,  // 可访问性
            )
            Text(user.name)
        }
    }
}
```

State 从外部传入（hoisting 做对了），Modifier 顺序正确，contentDescription 到位——**AI 不是变聪明了，是 Skill 在它耳边悄悄说了几句话。**

这就是 Skill 的本质：**给 AI 注入你项目的专业知识。**

---

## Skills 到底是个什么东西？

很多人以为 Skill 就是个"比较长的 prompt 模版"——这理解差了十万八千里。

**Skill 是一个完整的指令注入系统。** 当你的输入匹配了某个 Skill 的触发词，AI 工具会把整个 SKILL.md 的内容动态注入到 system prompt 里。具体流程是这样的：

```
用户输入 "帮我检查一下这段 Compose 代码"
        │
        ▼
Skill 引擎扫描所有 SKILL.md
        │
        ├─ 匹配 triggers: ["检查 Compose 代码", "compose code review"]
        │
        ▼
将该 SKILL.md 的完整指令注入 system prompt
        │
        ▼
AI 以"Compose 代码审查专家"的身份回应
```

这和手写 prompt 的天壤之别在于：

- **持久化**：写到 SKILL.md 里，不是粘到对话框里就丢了。下了班回来接着用。
- **触发词自动匹配**：不用每次都说"你是一个 Compose 架构师，请按以下标准审查...”。说"检查代码"就够了。
- **可组合**：一个对话里可以同时触发多个 Skill。比如 "review compose" → 加载审查 Skill，再加一句"创建 Issue 记录这些问题" → 触发 GitHub MCP。
- **Git 托管、团队共享**：你写的 Skill 可以被别人复用，别人的 Skill 你也可以直接用。

### 拆解一份好 SKILL.md

一份能打的 SKILL.md，分两部分：Front Matter（元信息）+ 正文（指令体）：

```yaml
---
name: android-compose-review
description: 审查 Jetpack Compose 代码的最佳实践与性能
triggers:
  - "review compose"
  - "检查 Compose 代码"
  - "compose code review"
  - "review compose code"
  - "帮我看看这个Composable"
  - "检查UI组件"
---
```

三个关键字段：

- **name**：唯一标识，用 kebab-case。选好名字别反复改。
- **description**：一句话描述，方便其他人一眼看懂这个 Skill 干嘛的。
- **triggers**：核心！触发词列表，**中英文都要覆盖**。用户输入中包含任意一个，Skill 就被激活。

正文部分是真正的"专业知识注入"。它不像文章那样写段落，而是**用检查清单（checklist）+ 指令（祈使句）+ 输出格式模板**的组合：

```markdown
# Compose Review Skill

When reviewing Compose code, follow this checklist strictly:

## State Management
- Check state hoisting: stateless composables should accept state as parameters
- Verify `remember` usage: expensive calculations must be wrapped
- Check `derivedStateOf` for derived values to prevent unnecessary recomposition
- Ensure `mutableStateOf` is not used inside ViewModel (use `MutableStateFlow` instead)

## Recomposition Optimization
- No side effects in composable function body (use LaunchedEffect, SideEffect, etc.)
- Stable parameters: use @Stable or @Immutable annotation for custom classes
- Inline lambdas with `remember` to avoid recomposition of parent

## Modifier Order
- Modifiers are applied left-to-right: `Modifier.padding().clickable()` ≠ `Modifier.clickable().padding()`
- `Modifier.clickable` + `Modifier.background` order matters for ripple effect
- `Modifier.size` should precede `Modifier.padding` for correct layout

## Output Format
For each composable function reviewed:
- **Function**: FunctionName
- **Issues Found**: [count]
- **Severity**: 🔴 Critical / 🟡 Warning / 🟢 Suggestion
- **Details**: Line-by-line breakdown
- **Fix**: Corrected code snippet
```

写 Skill 的几条黄金法则：

1. **用英文写指令，用中英文写触发词。** AI 对英文指令的理解更精准。触发词中英文都要有，用人话覆盖用户的自然表达。
2. **用祈使句。** "Check...", "Verify...", "Ensure..."。别写"建议考虑..."——AI 会以为不是必须执行的。
3. **输出格式写死。** 指定输出结构，每次返回一致，方便后续做自动化。
4. **目录结构要干净。** 一个 Skill 就是一个文件夹：

```
.claude/skills/android-compose-review/
├── SKILL.md                    # 核心定义（必须）
└── references/                  # 参考文档（按需引用）
    ├── compose-best-practices.md
    └── modifier-order-guide.md
```

<!-- TODO: 补充截图：一个真实的 SKILL.md 在 Claude Code 中触发后的 AI 输出效果 -->

### 我的 Android 开发必备 Skills

半年下来，这几个 Skill 在我的日常工作中出场率最高：

- **android-compose-review**：审查 Compose 代码。上面已经展示过了。写 UI 时几乎每次都触发。
- **systematic-debugging**：系统化调试流程。崩溃了？不是先猜原因，而是先收集栈信息→分析根因→验证假设。
- **android-thread-check**：检查线程安全。`viewModelScope.launch { api.fetchUser() }` 这种忘记 `withContext(IO)` 的代码，一抓一个准。
- **gradle-helper**：Gradle 编译报错排错。引用 `references/common-gradle-errors.md` 中积累的踩坑记录。
- **custom-lint-check**：我自建的一个，检查是否用了禁止的 API（比如直接用 Glide 而不是 Coil，直接用 Gson 而不是 Moshi）。

**写自定义 Skill 是你和能干的同行拉开差距的最快方式。** 别人的 Skill 解决通用问题，你项目的特殊规则——比如"用户模块的加密算法必须用 AES-256 而不是 DES"——只能你自己写。

---

## MCP：让 AI 长出手脚

有了 Skills，AI 在"脑子"层面已经很强了。但还有一个致命限制：**AI 不能直接操作外部世界。**

你让 AI 看个设计稿，它做不到——你得截图，贴到对话框里。你让 AI 帮你提个 PR，它做不到——你得手动把代码 push 上去。整个过程像在指挥一个困在房间里的人干活，靠喊话。

MCP（Model Context Protocol）就是打破这堵墙的协议。它是 Anthropic 推出的开放标准，让 AI 能直接调用外部工具。

**形象点的类比：**

- AI 模型 = 大脑，负责思考和决策
- MCP = 神经系统，连接大脑和四肢
- MCP Server = 四肢。GitHub Server 是手（提 PR、建 Issue），Figma Server 是眼睛（读设计稿），数据库 Server 是记忆拓展（查数据）

技术层面上，MCP 走的是 JSON-RPC over stdio/HTTP。一个 MCP Server 本质上就是一个暴露了标准接口的进程：

```
AI Client  ──JSON-RPC──►  MCP Server  ──REST/API──►  GitHub / Figma / 数据库
```

对于 Android 开发者来说，这几个 MCP Server 最实用：

| MCP Server | 能干嘛 | 典型场景 |
|------------|--------|---------|
| **GitHub MCP** | 操作 Issues、PR、代码、Release | "分析这个 Bug 报告，定位代码，创建修复分支并提 PR"——一句话完成 |
| **Figma MCP** | 读取设计稿节点、样式、组件变量 | "把 Figma 里这个登录页直接用 Compose 实现" |
| **Filesystem MCP** | 沙盒化文件读写 | 批量重命名资源文件、迁移包路径 |
| **Jira/Linear MCP** | Sprint 任务管理 | Sprint 规划 → AI 拆分子任务 → 自动分配 |

### 配置 GitHub MCP（10 分钟搞定）

最值得先配置的就是 GitHub MCP。写完代码直接在对话中说"帮我提个 PR"——

**步骤一：生成 Personal Access Token**

GitHub → Settings → Developer settings → Personal access tokens → Fine-grained tokens。

权限选最小原则：只勾选当前仓库、只给 Issues + Pull requests + Contents 的读写权限。

**步骤二：写入配置文件**

不同工具的配置文件位置不一样。以 OpenCode 为例，在项目根目录的 `opencode.json` 里加：

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

**重要：不要把 token 直接写在配置文件里。** 用 `${GITHUB_TOKEN}` 引用环境变量，然后在 `~/.zshrc` 或 `~/.bashrc` 中 export。

**步骤三：验证**

重启工具，输入："列出所有可用的 MCP Server"。AI 应该返回 GitHub Server 加载成功，提供了哪些 Tools。

### Figma → 代码：设计稿直出的真实体验

Figma MCP 是我觉得很惊艳的一个。以前的设计→开发管线是这样的：

```
设计师出设计稿 → 开发者打开 Figma → 肉眼量间距 → 手动写代码 → 调了好几次对不齐
```

现在有了 Figma MCP：

```
"读取这个 Figma 链接中的登录页面设计稿，用 Jetpack Compose 实现"
    │
    ▼
AI 通过 Figma MCP 读取节点层级和样式
    │
    ▼
AI 生成 Compose 代码，包含正确的间距、颜色、字体样式
    │
    ▼
代码质量检查 → Skill 再次介入，确保 Compose 最佳实践
```

Figma MCP 不会完美还原设计稿——它读取的是节点数据，不是视觉预览。但比起手动量间距、猜颜色值，效率和准确度有质的飞跃。

### 实战流程：从崩溃日志到修复 PR，一句话搞定

这是我最喜欢演示给同事看的链路：

```
"读取 Crashlytics 里 Top 3 的崩溃，定位代码，修复并提 PR"
    │
    ▼
1. AI 通过 MCP 读取崩溃数据
2. 分析堆栈，定位到具体文件和行号
3. 读取本地代码，结合崩溃原因写修复
4. 通过 GitHub MCP：创建分支 → 写入修复 → 提交 → 创建 PR
5. PR 描述自动附带崩溃原因分析和 regression 风险点
```

以前这一套我自己做至少要 30 分钟。**现在 30 秒。** 不是替换我的判断力，而是省掉了所有机械操作。

<!-- TODO: 补充截图：AI 通过 MCP 自动提交 PR 的完整过程 -->

---

## Skills + MCP 组合：这才是终极形态

单个 Skill 或单个 MCP 都不够震撼。真正让人头皮发麻的是它们组合起来的效果。

举个例子：你早上到公司，Slack 上有个新任务——"接入第三方支付 SDK，更新配置，提测"。你的工作流变成这样：

```
"我在做一个支付功能，帮我做以下事情：
1. 从 Figma 设计稿读取支付页面 UI
2. 根据项目 CLAUDE.md 中的技术栈规范生成代码
3. 代码写完后用 compose-review Skill 审查
4. 用 gradle-helper Skill 验证新 SDK 的 Gradle 依赖不冲突
5. 通过 GitHub MCP 创建 PR，描述写清楚变动内容"
```

每一个环节都有 Skill 或 MCP 兜底，不是靠 AI 临时发挥，而是靠一套固定的质量控制体系。

---

## Rules 文件：一切的基础地基

Skills 和 MCP 都依赖于一个前提：**AI 知道你项目是什么。**

如果你每次对话都要解释"我这个项目用 Hilt，用 Moshi，用 Room，Min SDK 是 26"——那 Skills 再好用也白搭。这就是 CLAUDE.md（或 AGENTS.md）要做的事。

**CLAUDE.md 是项目宪法。** 不是给 AI 的可选参考，而是每次对话自动注入的基础上下文。

### Skills 和 Rules 的区别，一句话讲清楚

- Rules 告诉 AI **"你的项目是什么"**（技术栈、架构、规范）
- Skills 告诉 AI **"遇到某类问题该怎么做"**（代码审查清单、调试流程、测试模板）

举个例子：AI 读到 CLAUDE.md 里的"项目使用 Compose + Hilt"，知道了"我是谁"。你接着说"帮我写个登录页"，Compose-review Skill 被触发，告诉 AI "该怎么正确地写这个登录页。"两者缺一不可。

### 一份能用的 Android 项目 CLAUDE.md 模板

下面这份是我实际在用、经过验证的模板。我把每一段的用意标出来，方便你按自己的项目改：

```markdown
# Project: [项目名]

## 技术栈（Technology Stack）
- Language: Kotlin 2.0 + KSP
- UI: Jetpack Compose 1.7 + Material3
- Architecture: MVVM + Clean Architecture (3-layer)
- DI: Hilt (禁止手动注入)
- Build: Gradle KTS 8.x + Version Catalog (libs.versions.toml)
- Network: Retrofit 2.9 + Moshi (禁止 Gson)
- Database: Room 2.7 + Flow 返回
- Async: Kotlin Coroutines 1.9 + Flow
- Testing: JUnit5 + MockK 1.13 + Turbine 1.1 + Compose Testing
```
*这一段是"弹药库清单"，AI 生成代码时要知道有哪些库可用、哪些被禁止。禁止项很重要——如果不写"禁止 Gson"，AI 可能随机选一个 JSON 库。*

```markdown
## 架构约束（Architecture Rules）
- ViewModel 暴露 StateFlow<UiState>，不暴露 LiveData
- Repository 返回 Flow<T>，不返回 suspend fun（除非单次查询）
- UseCase 是单一职责的无状态类，用 operator fun invoke()
- 网络模型与 UI 模型分离：NetworkDto → Domain Model → UiState
- 数据层不可持有 UI 层引用（禁止 Context 传入 Repository）
```
*这一段是"建筑设计规范"，指定分层架构中的强制约束。不写清楚的话 AI 可能把网络模型直接暴露给 UI，后面要重构就麻烦了。*

```markdown
## 命名规范（Naming Conventions）
- Composable 函数：PascalCase，文件对应函数名
- ViewModel：`[Feature]ViewModel`（如 `LoginViewModel`）
- Repository：`[Feature]Repository` 接口 + `[Feature]RepositoryImpl` 实现
- State：`[Feature]UiState` data class 或 sealed interface
- Event/Action：`[Feature]Action` sealed interface
```
*统一命名不只是为了好看，更影响 AI 的代码搜索和引用。当你说"找一下 Login 的 ViewModel"，AI 知道该搜什么文件名。*

```markdown
## Coding 规则（Coding Rules）
- 日志用 Timber，不用 Log.*
- 字符串用 R.string，不硬编码
- 禁止在 Composable 中执行网络请求或数据库操作
- 错误处理统一用 sealed class Result<T>（Success / Error / Loading）
```

```markdown
## 测试规则（Testing Rules）
- ViewModel 测试：MockK + Turbine + StandardTestDispatcher
- Repository 测试：InMemory Room + Fake Retrofit
- JUnit5 为默认测试框架
```

### 常见踩坑：CLAUDE.md 写了 500 行怎么办？

最常见的误区就是把 CLAUDE.md 当"项目全部知识"来写。500 行的 CLAUDE.md 会导致每次对话都额外消耗几千 token，几十次对话下来配额就没了。

**解决思路：分层管理。**

- **CLAUDE.md（必加载，控制在 150 行左右）：** 技术栈核心约束、命名规范、模块结构。这些是每次对话都可能用到的。
- **Skill（按需加载，不限行数）：** 详细的代码审查清单、测试模板、构建排错指南。只在需要时触发，不浪费 token。
- **references/ 目录下的文档（手动引用，不自动加载）：** API 规范文档、架构详细说明。只在 SKILL.md 中通过"When you need to check API specs, read references/..."引用。

### AGENTS.md：给 Subagent 的操作手册

如果你用的是 OpenCode 或 Superpowers 这类支持 Subagent 的系统，还需要一个 AGENTS.md。它告诉 Subagent 怎么调用 Skills、安全和权限边界：

```markdown
# AGENTS.md - Subagent 调度规则

## Skill 调用约定
- 编码任务前必须先调用 system-debugging → writing-plans，不可跳过
- 所有 Pull Request 前调用 android-compose-review

## Subagent 安全边界
- 不允许修改 build.gradle.kts（依赖变更需人工 Review）
- 不允许 git push --force
- 不允许提交 .env、local.properties、google-services.json
```

这个文件里的安全约束非常重要。你需要显式告诉 AI 什么不能做——AI 不会自动知道 `google-services.json` 不该提交。

### 另一个坑：MCP Token 权限开太大

给 GitHub MCP 配了 admin token → AI 不小心删了个远程分支。这种事不是没发生过。

安全三原则：

1. **Token 最小权限。** 用 Fine-grained token，限定到具体仓库，权限只给需要的。
2. **在 Rules 文件中写死约束。** CLAUDE.md 或 AGENTS.md 里显式声明：
   - 不允许删除分支
   - 不允许 force push 到 main/master
   - 创建 Issue 前需人工确认
3. **双保险。** `.gitignore` 里确认 `.env`、`local.properties`、`*.keystore` 都已忽略，防止 AI 误提交。

---

## 总结

放一张对比图帮你看清三者的定位：

| 工具 | 解决什么问题 | 通俗说法 |
|------|------------|---------|
| **Skills** | 给 AI 注入专业工作流，告别重复 prompt | "我该怎么做" |
| **MCP** | 打通 AI 与外部工具的数据通道 | "我能做到的" |
| **Rules 文件** | 消除每次对话的上下文复述 | "我是谁" |

**一句记住：Rules 告诉 AI "你是谁"，Skills 告诉 AI "你该怎么做"，MCP 让 AI "能做到"。**

很多人花了几个月时间比模型、比工具，不如花一个下午把这三样东西配好。配完之后你会发现，AI 不再是一个需要你操心每一句话的实习生，而是一个真正长在你项目上的搭档。

下一章，我们来看看社区里最火的那些 Skills，从中挑出最适合 Android 项目的组合。

<!-- TODO: 补充截图：Skill 触发匹配过程的调试日志 -->
<!-- TODO: 补充截图：MCP 配置验证的完整对话截图 -->
<!-- TODO: 补充截图：CLAUDE.md 在 Claude Code 启动时的加载日志 -->
