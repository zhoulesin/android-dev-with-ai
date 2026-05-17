# 我测评了 20 个热门 AI Skills，这些是真正提升 Android 开发效率的

> 时效性标注
> - 最后更新：2026-05
> - 统计周期：2026-01 ~ 2026-04（多项目实测）
> - 适用说明：Skills 触发策略与模型价格变化快，评分请结合当前项目环境复核

去年底开始，AI Coding 圈子里最火的词不是某个新模型，而是 **Skills**。

如果你最近看过几个主流技能仓库——`anthropics/skills`、`VoltAgent/awesome-claude-skills`、`spencerpauly/awesome-cursor-skills`、`RooCodeInc/Roo-Code`——你会发现 Skills 目录正在疯狂膨胀。有的仓库已经有 50+ 个 Skills，覆盖从"写 SQL"到"审查 GDPR 合规"的一切。

问题来了：**你怎么知道哪些 Skills 真的有用？**

大部分技巧文章告诉你"装这个装那个"，但没人告诉你装了之后 Token 账单涨了多少、生成的代码是不是反而更糟、或者那个 Skill 三个月没更新了。

这篇文章是我过去四个月的实测记录。我在三个 Android 项目（一个单人 Side Project，一个 4 人团队中型项目，一个 12 人大仓项目）上逐一测试了 20 多个 Skills，从**有用性、Token 成本、维护质量和 Android 适配度**四个维度打分。下面是全部结果，好的坏的都说。

---

## 评测框架：四个维度

在选择 Skill 之前，我建了一个简单的评分框架。每个 Skill 从以下四个维度打分（满分 5）：

| 维度 | 衡量标准 |
|------|---------|
| **有用性** | 引入后代码质量是否有可感知的提升？AI 跑偏次数是否减少？ |
| **Token 成本** | 每个会话额外消耗多少上下文？（越低越好） |
| **维护质量** | Skill 多久更新一次？文档是否清晰？社区反馈好不好？ |
| **Android 适配度** | 对 Android 开发场景是否"开箱即用"？还是需要大量定制？ |

其中"有用性"权重最高——如果一个 Skill 很有用，我可以接受它消耗一些 Token；但如果成本高而收益不明显，直接淘汰。

让我按类别逐一说明。

<!-- TODO: 补充截图：四维度评分雷达图 -->

---

## Memory 类：让 AI 不再"失忆"

AI Coding 工具最致命的缺陷是什么？不是代码质量，不是速度，而是**每次新会话都像第一次认识你的项目**。

你刚花两小时跟 AI 对齐了技术栈偏好，关掉终端再打开，它又开始建议你用 `LiveData` 而不是 `Flow`，用 `Gson` 而不是 `Moshi`。这就是"上下文税"——你每次都要重新交一遍。

Memory 类 Skills 就是来解决这个问题的。我测了四个主流方案。

### CLAUDE.md / AGENTS.md（原生规则文件）

**评分：★★★★★ 有用性 5 | 成本 高 | 维护 N/A | Android 5**

这是最基础的方案，也是**唯一我不想用也必须用的**。你只需要在项目根目录放一个 Markdown 文件，AI 工具启动时自动读取并注入为 system prompt。没有安装成本，没有学习曲线，所有主流 AI Coding 工具都支持。

在单人 Side Project 里，一个 200 行的 `CLAUDE.md` 足够让我省下每次会话开头那 5 分钟的项目背景介绍。文件里我写了：

- 技术栈：Kotlin 1.9+, Compose BOM 2024.01, Hilt, Room, Retrofit + Moshi, Coil
- 架构约束：Clean Architecture 三层，domain 层零 Android 依赖
- 禁止事项：硬编码字符串、`!!` 操作符、ViewModel 中直接使用 `viewModelScope.launch` 做长时间协程

效果立竿见影。引入之前，AI 生成代码时混用 `LiveData` 和 `Flow` 的概率大概是 40%；引入之后，降到 5% 以下。

但 CLAUDE.md 的最大问题是**全量注入**——无论你这次对话只需要改一个字符串，还是重构整个网络层，整个规则文件都会被塞进 system prompt。一个 500 行的 CLAUDE.md 每次会话额外吃掉约 1000+ token。

**怎么办？** 用 Git 作用域分层。我在 `app/` 目录下放了模块级的 CLAUDE.md（约 80 行，写注入约定和组件划分），在 `feature-debug/` 下面放了功能级的（约 40 行，写该功能的特殊约束）。这样当 AI 操作 `feature-debug/` 目录时，只会加载根目录 + `feature-debug/` 的规则，不会把整个 app 模块的规则都带进来。

**结论：必装。但控制单文件在 300 行以内，配合 Git 作用域分层。**

<!-- TODO: 补充截图：CLAUDE.md 在 Claude Code 中的加载日志 -->

### memory-bank（跨会话记忆）

**评分：★★★★ 有用性 4 | 成本 中高 | 维护 中 | Android 4**

memory-bank 是在 CLAUDE.md 之上的"动态记忆层"。它的核心思路很对：CLAUDE.md 是"宪法"（项目应该怎么写），memory-bank 是"日志"（项目现在写到哪了）。

它分了四个记忆层级：

```
memory-brief.md      ← 第1层：项目一句话介绍（几乎从不改）
memory-tech.md       ← 第2层：技术栈决策（迭代时才改）
memory-patterns.md   ← 第3层：代码模式积累（逐渐变多）
memory-progress.md   ← 第4层：当前进度和待办（每次会话都更新）
```

每层按需加载，不把所有东西都塞进 system prompt。理论上，这完美解决了 CLAUDE.md 的全量注入问题。

**实际体验：**

在 4 人团队的中型项目上，我试了两周。好处是 AI 确实能"记得"上一次会话的进度——比如知道"用户模块的 Repository 已经写完了，这次的任务是写它的单元测试"。这在多会话协作中很有用。

但问题是**维护麻烦**。`memory-progress.md` 理论上每次会话结束时要更新，但实际上，当你一口气写完 5 个功能后，根本想不起来去更新它。两周后，memory-bank 里记录的还是第一天的项目状态。

而且，至今没有好的自动化更新工具。你只能手动维护那些 Markdown 文件，而这个维护成本在两周后让我放弃了。

**结论：理念好，执行差。如果你的工具链支持自动进度更新，值得一试。否则，一个维护良好的 CLAUDE.md 加上每次会话开头的几句项目状态描述，效果差不多。**

### spec-driven / spec-kit（规格驱动开发）

**评分：★★★★ 有用性 4 | 成本 中 | 维护 高 | Android 4**

这其实更像一种"开发方法论"而非单纯的 Skill。流程是：写结构化 Spec → AI 严格按 Spec 执行 → 偏离即报错。

我特意用一个"注销确认弹窗"功能做了 A/B 对比：

- **不用 spec-driven**：直接告诉 AI "写一个注销确认弹窗"。结果 AI 理解偏了——它写了一个简单的 AlertDialog 但没做二次确认、没处理取消后恢复状态、文案也不对。来回沟通了 4 轮才改对，总耗时 1.5 小时。

- **用 spec-driven**：花 15 分钟写了一份结构化 Spec（UI 原型、交互流程、状态管理策略、测试清单），然后把 Spec 喂给 AI。AI 一次性完成了全部代码——Composable + ViewModel + Repository + 测试——0 轮沟通，总耗时 1 小时（含写 Spec 的 15 分钟）。

差值就在于：**Spec 替你回答了 AI 所有可能产生歧义的地方**。一份写得好的 Spec 应该聚焦在"做什么"和"怎么验证"，不限定"怎么写"——给 AI 实现自由度，但框死边界。

坦白说，spec-driven 不适合所有场景。如果你在做一个探索性原型，写 Spec 本身就是浪费时间——你连自己要什么都不确定。但如果需求明确（比如商业项目的迭代功能），先花 15 分钟写 Spec 可以让后续 AI 编码时间减少 40%。

**结论：对需求明确的 Android 功能开发，强烈推荐。对探索性原型，跳过。**

### repomix / repopack（代码库打包）

**评分：★★★ 有用性 3 | 成本 高（一次性） | 维护 高 | Android 3**

这个方案是——把你的代码库打包成一个结构化文件，一键喂给模型。

**适合的场景只有一个：让 AI 快速理解一个它从没见过的代码库。** 比如你接手了一个 50 万行的遗留项目，把 `app/src/main/java` 打包后，AI 就能理解整体结构。

但对于日常开发，属于"杀鸡用牛刀"。Android 项目里尤其要注意：一定要排除 `build/`、`.gradle/`、`R.java`、Hilt 生成的工厂类——否则一个 200MB 的上下文文件会直接把你的 Token 预算烧光。

**结论：作为一次性工具检查代码库结构有用，不适合日常使用。配置 `.repomixignore` 是使用的前提条件。**

---

## 流程管理类：AI 辅助开发的正确打开方式

Memory 类 Skills 解决的是"AI 不知道上下文"的问题，而流程管理类解决的是一个更根本的问题：**AI 辅助开发的流程应该是怎样的？**

很多人用 AI 的方式是：想到什么就让它写什么。这跟没有设计图就开始砌墙差不多——速度快，但迟早得拆了重来。

Superpowers 生态定义了一套从创意到交付的完整流程链：

```
brainstorming → writing-plans → executing-plans → code-review → finishing
```

我逐个测了这条链上的 9 个 Skills。下面是我留下的和扔掉的。

### brainstorming（头脑风暴）

**评分：★★★★★ 有用性 5 | 成本 中 | 维护 高 | Android 5**

这是我这半年用过的最有价值的单个 Skill——没有之一。

它的工作原理很简单：当你描述一个需求之后，AI 不会直接开始写代码，而是通过结构化提问帮你把模糊需求具象化为设计文档。通常 3-5 轮对话，产出物包含功能描述、验收标准、UI 原型、数据模型、状态管理策略。

**这改变了我使用 AI 的方式。** 之前我的工作流是：

```
有需求 → 告诉 AI → AI 写代码 → 发现不对 → 修改描述 → AI 重写 → 还不对 → 手动改
```

引入 brainstorming 之后变成了：

```
有需求 → 告诉 AI → AI 提问 4 轮 → 我确认 Spec → AI 一次写对
```

一开始我觉得"多花了 10 分钟聊需求"是在浪费时间。用了两周后发现：**那 10 分钟省下了后面 40 分钟的沟通和重工。** 一项简单的 ROI 计算：任何预计需要 15 分钟以上的功能开发，走 brainstorming 都是正收益。

而且我发现在 Android 场景中特别管用。因为 Android 开发涉及太多边界情况了——生命周期、配置变更、权限变化、屏幕旋转——这些在 brainstorming 阶段都会自然浮现出来，而不是等 AI 写完了代码才发现没处理。

**结论：必装。每次 15 分钟以上的功能开发都强制走一遍。禁止跳过。**

### writing-plans（编写实施计划）

**评分：★★★★★ 有用性 5 | 成本 中 | 维护 高 | Android 5**

如果说 brainstorming 是"把需求变成设计"，那 writing-plans 就是"把设计拆成任务"。

它的核心价值在于：**让 AI 把工作拆成 2-5 分钟一个的独立小任务，每个任务产出完整的代码文件。** 这意味着你不会陷入"AI 写了 200 行代码但只有前 50 行能用"的尴尬。

以一个"设置页暗色模式切换"功能为例，writing-plans 拆出了 8 个任务：

1. 创建 ThemeState 数据类 + 测试
2. 创建 ThemeViewModel + StateFlow + 测试
3. 创建 SettingsRepository 接口（domain 层）
4. 实现 SettingsRepository（data 层 + DataStore）
5. 创建 SettingsScreen Composable
6. 集成 Navigation 路由
7. 端到端集成测试
8. Review + 修复

每个任务的输出都是一个独立的、可验证的代码块。写完一个，验证通过，再写下一个。

**这对 Android 项目特别友好**——因为每个小任务编译快（10 秒以内），测试运行快（1 秒以内），反馈循环极短。不像传统方式，AI 一口气吐 500 行代码，你 Gradle 编译一次 3 分钟，结果发现一半不可用。

**结论：brainstorming 的天然搭档。有了 brainstorming 产出的 Spec，writing-plans 几乎零人工干预就能产出合理的任务拆分。**

### test-driven-development（TDD）

**评分：★★★★★ 有用性 5 | 成本 低 | 维护 高 | Android 3**

这个 Skill 的规则极其严格：**没有失败的测试，别写实现代码。** 它强制执行 Red-Green-Refactor 循环。

先说我为什么打分给了 5 分有用性却只给了 Android 3 分适配度。

**TDD 在纯 Kotlin 模块上表现完美。** 我的 domain 层和 data 接口层，编译 2 秒、测试 0.5 秒。在这种环境下，Red-Green-Refactor 循环非常流畅——写一个失败的测试 → 写最少代码让测试通过 → 重构。AI 在这个节奏下表现异常稳定，生成的测试代码覆盖率通常在 90% 以上。

**但在 Compose UI 层，TDD 是噩梦。** `ComposeTestRule` 需要 Activity 宿主，每次测试启动 5-10 秒。一个简单的"点击按钮后弹窗出现"测试，你花在等待上的时间比写代码的时间还长。而且 Compose 的语义树测试总有各种奇奇怪怪的问题——`onNodeWithText` 找不到节点、`performClick` 因为重组延迟而失败。

**我的策略：domain 和 data 层严格 TDD，presentation 层分两层处理——ViewModel 逻辑 TDD（因为 ViewModel 测试是纯 Kotlin），Composable UI 用 Preview 和手动验证兜底。**

这个策略运行了三个月，效果很好。逻辑层 Bug 率下降了大约 70%，UI 层的开发效率也没有因为 TDD 而下降。

**结论：domain/data 层必用 TDD。Compose UI 层用 TDD 你的 Gradle 构建时间会翻倍的。把 Compose 当成"薄胶水层"来对待。**

### systematic-debugging（系统化调试）

**评分：★★★★★ 有用性 5 | 成本 低 | 维护 高 | Android 5**

如果你只装三个 Skills，我会建议：CLAUDE.md、brainstorming、还有这个。

systematic-debugging 强制 AI 走四阶段根因分析：症状识别 → 假设生成 → 验证 → 修复。**禁止"改一改试试看"式的症状修复。**

给了我最大的惊喜是：AI 在 debug 模式下会主动要求你提供 `logcat` 输出、Layout Inspector 截图、或者 git bisect 的结果。这不比你自己瞎蒙效率高 10 倍？而且它不是猜——它真的在构造假设然后让你验证。

在 Android 场景中尤其好用。因为 Android 的崩溃堆栈通常很长，而 AI 能快速过滤掉框架层噪音，聚焦在你的业务代码上。一次典型的"应用闪退"排查，不用 Skill 时可能花 30 分钟（看 logcat → 猜测 → 改代码 → 编译 → 再测），用 systematic-debugging 后平均 10 分钟解决了。

**结论：必装。bug 出现时第一个激活的 Skill。**

### 我放弃的流程 Skills

**receiving-code-review**（接收代码审查反馈）和 **requesting-code-review**（请求代码审查）：

这两个的理念没问题——一个是"收到 Review 意见后怎么正确处理"，一个是"派子 Agent 审查你的代码变更"。

但在我单人开发的项目里，review 产出过于形式化了。requesting-code-review 返回的结果 80% 是"代码风格良好，无重大问题"——这不是因为代码真的好，而是因为 AI 审查缺少具体的检查清单。你必须手动告诉它"请检查 Hilt 注入是否正确、Composable 是否包含 Preview、StateFlow 暴露是否正确"，否则它只会做最表层的风格检查。

有团队 Code Review 流程的项目里可能更有用。但我现在更倾向于在 CLAUDE.md 里把检查规则写死，让 AI 在写代码时就遵循，而不是事后审查。

**finishing-a-development-branch**（完成开发分支）也差不多——它做的事情（运行测试、生成 PR 描述）我手动做只要 2 分钟。如果你有严格的交付流程需要标准化，这个有用；否则，手动更快。

---

## Coding 规范类：我用"禁止"而非"推荐"

规范类不是独立的 Skill 文件，而是嵌入在 CLAUDE.md 中的约束规则。核心原则只有一个：

**AI 对"推荐"的遵守度大约 30%，对"禁止"的遵守度超过 90%。**

所以我的 CLAUDE.md 里没有"请尽量使用 Flow"这种废话，而是直接写"禁止使用 LiveData"。效果差异巨大。

以下是我经过四个月迭代后，CLAUDE.md 中存活下来的核心规则清单：

### Level 1：全局规则（每次会话自动加载）

```
技术栈声明：
- Kotlin 1.9+, Compose BOM 2024.01
- Hilt 2.50, Room 2.6, Retrofit + Moshi（禁止 Gson）
- Coroutines + Flow（禁止 RxJava）
- Min SDK 26, Target 34

架构约束：
- 严格 Clean Architecture 三层：domain（纯 Kotlin 模块，kotlin("jvm")）→ data → presentation
- ViewModel 只暴露 StateFlow<UiState>，禁止暴露可变状态
- Repository 接口定义在 domain 层，实现在 data 层

代码风格禁止项：
- 禁止 !! 操作符（用 requireNotNull 或 ?: error("...")）
- 禁止硬编码字符串（必须 R.string）
- 禁止硬编码颜色（必须 MaterialTheme）
- 禁止 ViewModel 中直接 import android.*
```

### Level 2：模块规则（按 Git 作用域加载）

```markdown
# app/CLAUDE.md
## Hilt 注入约定
- Fragment 必须使用 @AndroidEntryPoint
- ViewModel 通过 Hilt 注入（@HiltViewModel + @Inject constructor）
- 禁止手动 ViewModelFactory

## Compose 约定
- Composable 函数第一个参数：modifier: Modifier = Modifier
- 使用 collectAsStateWithLifecycle() 而非 collectAsState()
- 每个 Composable 文件至少包含一个 @Preview
```

### Level 3：按需规则（手动激活）

安全规则和性能规则我不常驻在 system prompt 里——它们太长了。当用户故事涉及敏感数据或性能敏感代码时，我再手动 @mention 激活。

**这里有一个重要的教训：不要一次性堆砌规则。** 我第一版的 CLAUDE.md 有 800 多行，结果 AI 在前 3 轮对话中行为明显异常——system prompt 太长，挤占了模型的"思考空间"。后来砍到 300 行，效果反而提升了。

**80/20 法则在这里很适用：20% 的规则产生 80% 的效果。** 最核心的 5-8 条"禁止项"比 30 条"推荐项"有效得多。

---

## Android 专项：我自建了一些 Skill

社区目前几乎没有"开箱即用"的 Android 专项 Skill。我能找到的都是通用 Skill 在 Android 场景下的调整版，或者需要团队自己编的规则文件。

以下是我自建并实际使用的几个：

### 1. Compose 组件生成器（自建）

触发：当用户要求创建新的 Composable 组件时。

检查清单强制验证：
- 是否有 `modifier: Modifier = Modifier` 作为第一个可选参数？
- 是否正确使用 `collectAsStateWithLifecycle()`（如果有 Flow 订阅）？
- 是否包含 `@Preview` 函数？
- Preview 是否至少覆盖两个设备（手机 + 平板/折叠屏）？
- 状态是否向上提升了？（无多余的 `remember` 滥用）

这个 Skill 我用得最频繁。最大的收益是**杜绝了 AI 把业务逻辑写在 Composable 里面**——这是 AI 写 Compose 代码最常见的错误。

### 2. Hilt 依赖检查器（自建）

触发：当用户要求添加新模块或修改依赖注入代码时。

检查清单：
- 新的 ViewModel 是否加了 `@HiltViewModel`？
- 构造函数是否加了 `@Inject`？
- Fragment 是否标记了 `@AndroidEntryPoint`？
- Module 类是否正确标注了 `@Module` 和 `@InstallIn`？
- 是否出现了 Hilt 无法解析的循环依赖？

这个 Skill 帮我省了最多**编译等待时间**。以前 AI 生成 Hilt 代码后，要等 Gradle 编译完才知道注入正确与否——一次编译 2-3 分钟。现在这个 Skill 在前置检查阶段就能拦截 90% 的 Hilt 配置错误。

### 3. Navigation 图验证器（自建）

触发：当用户要求添加新页面或路由时。

检查清单：
- 新路由是否已在 Navigation Graph 中注册？
- 参数类型是否匹配？
- 深层链接是否正确配置？
- Safe Args 是否已生成？

这个暂时还比较简单，主要是在编译前拦截明显的配置遗漏。

### 4. 社区值得关注的

如果你不想自己从头建 Android Skills，以下是我在 2026-05 仍在跟踪的仓库（都已验证可访问）：

- **Anthropic 官方技能仓**：[`anthropics/skills`](https://github.com/anthropics/skills)  
  适合作为“技能格式和写法”的基准参考，不建议无脑整仓引入。
- **跨客户端技能聚合**：[`VoltAgent/awesome-claude-skills`](https://github.com/VoltAgent/awesome-claude-skills)  
  覆盖面广，适合先筛再用，避免一次性引入过多导致上下文污染。
- **Cursor 技能聚合**：[`spencerpauly/awesome-cursor-skills`](https://github.com/spencerpauly/awesome-cursor-skills)  
  适合做 Cursor 生态下的能力补充，重点挑质量高、维护活跃的条目。
- **Claude Code 生态清单**：[`hesreallyhim/awesome-claude-code`](https://github.com/hesreallyhim/awesome-claude-code)  
  除 Skills 外也覆盖 hooks / commands / orchestrators，适合做全局盘点。
- **Codex 技能聚合**：[`ComposioHQ/awesome-codex-skills`](https://github.com/ComposioHQ/awesome-codex-skills)  
  适合多客户端并行团队，做 Claude/Codex 技能迁移和对照测试。

建议你按“每周热榜+月度复盘”的机制维护候选池，避免只凭一次收藏做长期选型。  
可直接复用：[`每周 AI / AI-Coding GitHub 热榜追踪`](./每周AI与AI-Coding热榜.md)。

---

## 综合对比矩阵：20+ Skills 一图看尽

以下是我过去四个月测试的 22 个 Skills/规则方案的综合评分。**"每日使用"列是我当前工作流中的实际使用频率。**

| Skill / 规则 | 类别 | 有用性 | Token 成本 | 维护质量 | Android 适配 | 每日使用 |
|-------------|------|--------|-----------|---------|-------------|---------|
| CLAUDE.md | Memory | ★★★★★ | 高 | N/A | ★★★★★ | 每天 |
| brainstorming | 流程 | ★★★★★ | 中 | 高 | ★★★★★ | 每天 |
| systematic-debugging | 流程 | ★★★★★ | 低 | 高 | ★★★★★ | 每天 |
| writing-plans | 流程 | ★★★★★ | 中 | 高 | ★★★★★ | 高频 |
| TDD（domain/data层） | 流程 | ★★★★★ | 低 | 高 | ★★★★ | 高频 |
| Compose 组件生成器 | Android | ★★★★ | 低 | 自建 | ★★★★★ | 高频 |
| Hilt 依赖检查器 | Android | ★★★★ | 低 | 自建 | ★★★★★ | 高频 |
| Kotlin Style Rules | 规范 | ★★★★★ | 低 | N/A | ★★★★★ | 每天 |
| Compose Rules | 规范 | ★★★★★ | 低 | N/A | ★★★★★ | 每天 |
| spec-driven | Memory | ★★★★ | 中 | 高 | ★★★★ | 中频 |
| Gradle Rules | 规范 | ★★★★ | 低 | N/A | ★★★★★ | 中频 |
| MVVM Rules | 规范 | ★★★★★ | 低 | N/A | ★★★★★ | 每天 |
| memory-bank | Memory | ★★★★ | 中高 | 中 | ★★★★ | 低频 |
| Navigation 图验证器 | Android | ★★★ | 低 | 自建 | ★★★★★ | 中频 |
| Security Rules | 规范 | ★★★★ | 低 | N/A | ★★★★★ | 按需 |
| subagent-driven-dev | 流程 | ★★★★★ | 高 | 高 | ★★★★ | 低频 |
| repomix | Memory | ★★★ | 高（一次性） | 高 | ★★★ | 按需 |
| executing-plans | 流程 | ★★★★ | 中 | 高 | ★★★★ | 低频 |
| requesting-code-review | 流程 | ★★★ | 中 | 高 | ★★★★ | 低频 |
| receiving-code-review | 流程 | ★★★ | 低 | 高 | ★★★★ | 低频 |
| finishing-dev-branch | 流程 | ★★★ | 低 | 高 | ★★★★ | 低频 |
| Clean Arch Rules | 规范 | ★★★★ | 低 | N/A | ★★★★ | 每天 |

<!-- TODO: 补充截图：实际项目中 Token 消耗面板截图 -->

---

## Token 成本到底有多高？

这是一个每个写 Skills 文章的人都应该回答但实际上很少人回答的问题。

以我的 4 人团队中型项目为基准（每人每天约 10 次 AI 会话，使用 Claude Sonnet 级别模型）：

| 配置方案 | 月 Token 消耗 | 月费用估算 | 效率倍数（相对裸用） |
|---------|-------------|-----------|-------------------|
| **裸用 AI**（无 Skills/规则） | ~5M | ~$15 | 1.0x |
| **仅 CLAUDE.md**（300 行） | ~7M (+40%) | ~$21 | 1.3x |
| **CLAUDE.md + 核心流程链**（brainstorming + writing-plans + TDD + debugging） | ~9M (+80%) | ~$27 | 1.8x |
| **全套**（含 subagent-driven，每次派子 Agent） | ~14M (+180%) | ~$42 | ~2.5x |

关键发现：**CLAUDE.md + 核心流程链的组合，ROI 最高。** Token 成本增加 80%，但效率提升 80%，净收益为正。而全套方案虽然绝对效率更高，但 Token 成本翻了三倍，适合对效率要求极高且预算充足的团队。

> 以上数据基于 2026 年 Q1 的模型定价，实际费用以各平台最新定价为准。效率倍数为内部自评，仅供参考。

---

## 我的每日 Skill 配置（最终版）

经过了四个月的增删改查，我现在稳定使用的 Skills 组合是：

**每天自动加载的：**

```
项目根 CLAUDE.md（220 行，全局规则）
+ app/CLAUDE.md（80 行，Hilt + Compose 约定）
+ Kotlin/Compose/MVVM Rules（嵌入 CLAUDE.md 中的规范约定）
```

**每次功能开发激活的：**

```
brainstorming → writing-plans →（执行阶段启用 TDD + Compose/Hilt 检查器）
```

**出 Bug 时激活的：**

```
systematic-debugging（我把它设为最高触发优先级）
```

**按需使用的：**

```
spec-driven（需求明确的大型功能）
Security Rules（涉及敏感数据的模块）
repomix（接手不熟悉的模块时）
```

就这么多了。12 个总项目，但**真正每天高频使用的只有 6-7 个**。

---

## 最重要的感悟：Skills 像依赖，少而精胜过广而泛

四个月的测试让我得出一个反直觉的结论：

**你的第一个版本的 Skills 清单应该尽可能短。**

我见过很多团队一次性引入 10+ 个 Skills，然后发现 AI 行为怪异——因为多个 Skills 存在规则冲突，或者 system prompt 太长导致模型注意力分散。每次只加 1-2 个，用两周验证效果，数据说好才留。

另一个容易被忽略的点：**规则必须反映真实的代码状态，而非你"希望"的状态。**

我犯过这个错：在 CLAUDE.md 里写了"Clean Architecture 三层"，但实际上项目的 data 和 domain 层还没拆分开。结果 AI 按 Clean Architecture 的规则生成了代码，但放不进现有的项目结构中。你把规则写成了"理想状态"的宣言，只会让 AI 写出的代码更没法用。

**先整理项目结构让它符合规范，再写规范规则。不要反过来。**

这也引出了我对整个 Skills 生态的核心看法：**Skills 是放大镜，不是魔法棒。** 如果你的项目结构混乱，Skills 只会放大这种混乱。如果你的技术选型摇摆不定（LiveData vs Flow、XML vs Compose），Skills 没法替你做出选择——它只会忠实地反映这种摇摆。

先把地基打好——清晰的项目结构、一致的技术选型、经过验证的编码规范。然后再用 Skills 把这些约定注入到每一段 AI 生成的代码中。**地基不好，上层建筑再花哨也是危楼。**

<!-- TODO: 补充截图：brainstorming 对话示例，展示 AI 提问 → 用户回答 → AI 产出 Spec 的交互过程 -->

---

> **下一篇：** 第 6 章——当多个 Skills 功能重叠时如何取舍，以及如何为你的 Android 项目编写高质量的自定义 Skill。
