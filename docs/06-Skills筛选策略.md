# 第 6 章 · Skills 筛选策略

> **TL;DR** 面对上百个社区 Skills，本章给你一套可量化的筛选框架：先用评分矩阵定位适合你的层级，再用决策流程解决功能重叠问题，最后手把手写出团队专属的 Android Skill。

---

## 6.1 筛选矩阵：找到你的最佳配置

### 本节目标

建立一套可落地、可量化的 Skills 评估体系，让你在 5 分钟内根据团队现状锁定最优 Skill 组合。

### 前置知识

- 熟悉第 4 章介绍的 Skills 机制、MCP 协议
- 浏览过第 5 章的高赞 Skills 清单
- 了解自己项目的技术栈：Kotlin / Java / Compose / XML / Gradle (Groovy vs Kotlin DSL)

### 正文

社区 Skills 数量已经突破 200+，盲目全部启用只会让 Token 预算爆炸、上下文噪音飙升。我们需要一个**三维评分矩阵**来精准筛选。

#### 1. 核心评估维度

| 维度 | 权重 | 评分标准（1-5 分） |
|------|------|-------------------|
| 团队规模适配 | 30% | 5=完美契合当前人数；1=完全不合 |
| 项目阶段匹配 | 25% | 5=正是当前阶段所需；1=毫无关系 |
| 技术栈契合度 | 25% | 5=深度适配 Android；1=纯 Web/通用 |
| Token 效率 | 20% | 5=<500 token/次；1=>3000 token/次 |

**总分公式**：`Score = Σ(维度评分 × 权重)`

- **4-5 分**：强烈推荐，立即启用
- **3-3.9 分**：可选，按需启用
- **<3 分**：不推荐，暂不启用

#### 2. 按团队规模 + 项目阶段推荐配置

##### 场景 A：1-3 人 · 新项目

> 特点：快速原型验证，频繁迭代，尚未形成编码规范

```text
推荐配置（Top 5）：
┌─────────────────────────────────────────────┐
│ 1. brainstorming         → 需求澄清必装     │
│ 2. android-code-review   → 弥补缺少 Reviewer│
│ 3. architecture-design   → 架构决策辅助     │
│ 4. compose-ui-generator  → UI 快速生成      │
│ 5. gradle-helper         → 构建脚本排错     │
└─────────────────────────────────────────────┘

不推荐：
✗ dispatcher/superagent-heavy → 小团队用不上并行调度
✗ compliance-check → 新项目无合规需求
✗ legacy-migration → 无历史债务
```

<!-- TODO: 补充截图：场景 A 评分表填写示例 -->

##### 场景 B：4-10 人 · 成熟项目

> 特点：有 CI/CD，有 Code Review 流程，存在中等技术债务

```text
推荐配置（Top 8）：
┌─────────────────────────────────────────────┐
│ 1. brainstorming         → 需求评审阶段     │
│ 2. writing-plans         → 复杂任务分拆     │
│ 3. executing-plans       → 按计划执行       │
│ 4. android-code-review   → 自动化审查       │
│ 5. systematic-debugging  → Bug 修复流程     │
│ 6. test-driven-development → 提高覆盖率    │
│ 7. gradle-helper         → 构建优化         │
│ 8. memory-bank           → 跨会话记忆       │
└─────────────────────────────────────────────┘

按阶段启用（避免同时加载）：
  需求阶段：brainstorming + writing-plans
  开发阶段：executing-plans + test-driven-development
  提交阶段：android-code-review
  Bug 修复：systematic-debugging
```

<!-- TODO: 补充截图：场景 B 按阶段启用流程图 -->

##### 场景 C：10+ 人 · 重构项目

> 特点：多模块，历史代码复杂，需要严格流程管控

```text
推荐配置（Top 10）：
┌──────────────────────────────────────────────┐
│ 1. brainstorming         → 重构方案评审      │
│ 2. writing-plans         → 分步重构计划      │
│ 3. executing-plans       → 严格按计划执行     │
│ 4. android-code-review   → 多维度审查        │
│ 5. systematic-debugging  → 回归 Bug 定位     │
│ 6. test-driven-development → 重构安全保障    │
│ 7. architecture-design   → 架构决策记录      │
│ 8. legacy-migration      → 历史代码迁移策略   │
│ 9. gradle-helper         → 多模块构建管理    │
│ 10. memory-bank          → 跨会话上下文保持   │
└──────────────────────────────────────────────┘

额外建议：
  • 启用 Rules 文件定义模块边界（参见 9.2 节）
  • 设置 Token 上限避免上下文溢出（参见 10.3 节）
  • 建立 Skill 评审委员会（参见 6.4 节）
```

#### 3. 自行打分实操表

拿出纸笔或 Excel，对每个候选 Skill 填下表：

| Skill 名称 | 团队规模 (1-5) | 项目阶段 (1-5) | 技术栈 (1-5) | Token效率 (1-5) | 加权总分 | 决策 |
|-----------|:-----------:|:-----------:|:----------:|:-------------:|:------:|:----:|
| brainstorming | 5 | 4 | 4 | 4 | 4.3 | ✅ |
| superagent-heavy | 1 | 2 | 3 | 2 | 1.9 | ❌ |
| legacy-migration | 2 | 5 | 4 | 3 | 3.4 | ⚠️ |
| ... | | | | | | |

> 权重：30% + 25% + 25% + 20%

<!-- TODO: 补充截图：自行打分实操表填写范例 -->

### 动手实践

**任务**：为你当前项目完成 Skills 评分表

1. 列出你当前启用的所有 Skill（检查 `.claude/skills/` 目录）
2. 用上述矩阵给每个 Skill 打分
3. 将总分 < 3 的 Skill 禁用
4. 对比禁用前后的 Token 消耗变化

**预期结果**：你可能发现 20-30% 的 Skill 实际上是不需要的。

### 踩坑记录

- **坑 1**："评分高就全部加载"——评分只是参考，内存中的并发 Skill 建议不超过 6 个，否则上下文噪音会降低模型输出质量。
- **坑 2**："小团队也用 dispatcher"——并行调度 Skill 在小团队反而增加决策成本，等你团队到了 4 人以上再考虑。
- **坑 3**："只看 GitHub Star 数"——Star 多不代表适合你，社区热度 ≠ 技术栈契合度。

---

## 6.2 功能重叠时的决策流程

### 本节目标

当两个以上 Skills 功能高度相似（如 memory-bank vs spec-driven vs 原生 CLAUDE.md 的 Memory 能力），学会用五步决策法快速选型。

### 正文

Skills 生态的必然结果：同类功能会有多个实现。选择困难时，按以下流程决策。

#### 决策五步法

```
Step 1: 功能覆盖度对比
   ↓
Step 2: 维护活跃度检查
   ↓
Step 3: Token 消耗实测
   ↓
Step 4: 社区口碑评估
   ↓
Step 5: 最终决策
```

#### Step 1：功能覆盖度对比

以 **Memory 类（跨会话记忆保持）** 为例：

| 能力 | memory-bank | spec-driven | 原生 CLAUDE.md | 说明 |
|------|:----------:|:----------:|:------------:|------|
| 自动上下文保存 | ✅ | ❌ | ⚠️ 静态 | CLAUDE.md 需手动维护 |
| 结构化项目记忆 | ✅ | ✅ | ❌ | spec-driven 偏 PRD 方向 |
| 跨会话恢复 | ✅ | ✅ | ❌ | |
| Android 项目感知 | ⚠️ 通用 | ⚠️ 通用 | ✅ 可定制 | CLAUDE.md 可写 build.gradle 等 |
| 学习成本 | 中 | 高 | 低 | |
| Token 开销 | ~800/session | ~1200/session | ~0 | CLAUDE.md 几乎零开销 |

**结论**：
- 如果你需要**自动、动态**的项目记忆 → `memory-bank`
- 如果你偏重**需求 → 实现**的完整追踪 → `spec-driven`
- 如果团队**保持小而简单** → 写一份好的 `CLAUDE.md` 就够了

#### Step 2：维护活跃度检查

用 GitHub API 或直接浏览仓库检查：

```bash
# 检查最近提交时间
curl -s https://api.github.com/repos/{owner}/{repo}/commits?per_page=3 | \
  jq '.[].commit.author.date'

# 检查 Issue 响应速度
curl -s https://api.github.com/repos/{owner}/{repo}/issues?state=open&per_page=5 | \
  jq '.[] | {title: .title, created: .created_at, comments: .comments}'
```

活跃度红线：
- 最近一次提交 > 6 个月 → 🚩 高风险
- Open Issue > 50 且近期无回复 → 🚩 社区不活跃
- PR 合并周期 > 30 天 → 🚩 维护者响应慢

#### Step 3：Token 消耗实测

在相同任务下对比 Token 消耗：

```python
# 示例：在 OpenCode/Cursor/Claude Code 中分别实测
# 任务：为 LoginViewModel 写单元测试

# Skill A: memory-bank
# Input tokens:  4,200
# Output tokens: 1,800
# Total:         6,000

# Skill B: spec-driven
# Input tokens:  5,800
# Output tokens: 2,100
# Total:         7,900

# 原生 CLAUDE.md（无额外 Skill）
# Input tokens:  2,100
# Output tokens: 1,600
# Total:         3,700
```

> 结论：CLAUDE.md 零额外 Token 开销，但在复杂场景下记忆效果不如 memory-bank。**按任务复杂度选型**——简单项目用 CLAUDE.md，复杂项目用 memory-bank。

<!-- TODO: 补充截图：Token 消耗对比柱状图 -->

#### Step 4：社区口碑评估

关注以下信息源：

| 来源 | 关注点 |
|------|-------|
| GitHub Discussions | 真实用户反馈、边缘案例 |
| Reddit r/androiddev | Android 开发者视角 |
| Twitter/X #AICoding | 最新趋势 |
| Discord/Slack 社区 | 实时讨论 |

#### Step 5：决策模板

```markdown
## Skill 选型决策：{Skill类别}

**背景**：{为什么需要这类 Skill}

**候选**：
1. {Skill A} - {一句话特点}
2. {Skill B} - {一句话特点}
3. {Skill C} - {一句话特点}

**对比结果**：
| 维度 | A | B | C |
|------|---|---|---|
| 功能覆盖 | ⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| 维护活跃 | ⭐⭐⭐+ | ⭐⭐⭐⭐ | ⭐⭐ |
| Token开销 | ⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐ |
| 社区口碑 | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐ |
| 总分 | 15 | 14 | 12 |

**决策**：选择 A，原因是...
**备用方案**：如果 A 不满足需求，切换到 C 的成本是...
```

### 动手实践

**任务**：对你项目中的一组功能重叠 Skill 执行五步法决策

1. 找出当前同时启用的相似 Skill（如 2 个以上的 review 类 Skill）
2. 用 Step 2 的命令检查它们的最新提交时间
3. 在同一个任务上对比 Token 消耗
4. 填充决策模板，形成团队文档

### 踩坑记录

- **坑 1**："功能覆盖广一定好"——功能多 ≠ 每项都做得好。一个专注做一件事的 Skill 往往比大而全的更可靠。
- **坑 2**："Star 多就可以放心用"——star 数的增长可能只是某次营销活动的效果，不反映代码质量。
- **坑 3**："CLAUDE.md 够用了不需要 Skill"——对于 4 人以上团队，纯 Markdown 的 Memory 能力无法替换 memory-bank 的跨会话状态管理。

---

## 6.3 自定义 Skill 编写实战

### 本节目标

手把手写出团队第一个 Android 专用 Skill，掌握 Skill 的目录结构、触发词设计和上下文注入策略。

### 正文

社区 Skill 再丰富，也无法覆盖你的项目特有的技术债、命名规范和模块结构。自定义 Skill 是解决"最后一公里"问题的关键。

#### 1. Skill 目录结构

```text
.claude/skills/android-dev/
├── SKILL.md              # 必需：Skill 定义文件
├── references/           # 可选：上下文注入文件
│   ├── build-config.md   # build.gradle 内容快照
│   ├── proguard-rules.md # 混淆规则
│   ├── module-map.md     # 模块依赖关系
│   └── naming-conventions.md  # 命名规范
└── scripts/              # 可选：自动化脚本
    └── check-lint.sh
```

#### 2. SKILL.md 核心结构

```markdown
---
name: android-dev-assist
description: Android 项目开发辅助，注入项目规范、构建配置和模块上下文
triggers:
  - "构建失败"
  - "build failed"
  - "创建新模块"
  - "add module"
  - "依赖冲突"
  - "dependency"
mode: auto
---

# Android Dev Assist Skill

为当前 AI 会话注入以下项目上下文：

## 项目信息
- 包名: com.example.myapp
- 最低 SDK: 26
- 目标 SDK: 34
- Kotlin 版本: 2.0.0
- AGP 版本: 8.5.0

## 模块结构
{{REFERENCES:references/module-map.md}}

## 编码规范
{{REFERENCES:references/naming-conventions.md}}

## 执行规则
1. 创建新文件时，检查 references/module-map.md 确定所属模块
2. 使用 Kotlin DSL 编写 build.gradle.kts
3. 遵循 references/naming-conventions.md 命名
4. 涉及 ProGuard 时，同时更新 references/proguard-rules.md
```

#### 3. 触发词设计原则

```text
好的触发词特征：
  ✅ 具体明确："构建失败" > "build"
  ✅ 覆盖中英文："依赖冲突" + "dependency"
  ✅ 包含特征关键词："gradle sync failed"、"sync 失败"
  ✅ 避免过度宽泛：不要用 "代码"、"文件" 等通用词

差的触发词示例：
  ❌ "android" —— 太宽泛，每句话都可能触发
  ❌ "bug" —— 同上
  ❌ "帮我" —— 无意义
```

#### 4. 上下文注入策略

Skill 的核心价值在于**在正确的时机注入正确的上下文**。三种策略：

| 策略 | 用法 | 适用场景 | Token 开销 |
|------|------|---------|:---------:|
| **静态内联** | 直接把内容写在 SKILL.md | 不会频繁变动的常量 | 低 |
| **{{REFERENCES}}** | 引用外部文件 | 构建配置、版本号等 | 中 |
| **动态生成** | 脚本运行时获取 | git 分支、最近提交等 | 高 |

示例——`references/module-map.md`：

```markdown
# 模块依赖关系

```
app (application)
├── :feature:login (android-library)
│   ├── :core:network (android-library)
│   └── :core:common (android-library)
├── :feature:profile (android-library)
│   ├── :core:network (android-library)
│   └── :core:database (android-library)
├── :core:network (android-library)
└── :core:database (android-library)
```

**模块间规则**：
- `app` 可以依赖所有 feature 和 core 模块
- `feature:*` 只能依赖 `core:*` 模块，不能依赖其他 feature
- `core:*` 禁止依赖 `feature:*` 模块
```

#### 5. 完整示例：Android Code Review Skill

一个可直接使用的代码审查 Skill：

```markdown
---
name: android-code-review
description: Review Android 代码，检查架构分层、性能问题和 Kotlin 最佳实践
triggers:
  - "review"
  - "代码审查"
  - "code review"
  - "帮我看看这段代码"
  - "检查代码"
mode: manual
---

# Android Code Review Skill

## 审查清单

### 1. 架构与分层（Architecture）
- [ ] ViewModel 未持有 View/Context 引用
- [ ] Repository 层不直接访问 ViewModel
- [ ] 模块依赖方向正确（core ← feature ← app）
- [ ] UseCase 粒度合理，单一职责

### 2. Kotlin 最佳实践
- [ ] 避免不必要的 `!!` 非空断言
- [ ] 优先使用 `data class` 而非手动实现 equals/hashCode
- [ ] Flow 收集使用 `repeatOnLifecycle` 而非 `launchWhenX`
- [ ] `sealed class` > `enum`（需要携带数据时）
- [ ] 使用 `object` 单例时注意线程安全

### 3. Compose 专项
- [ ] `remember` 中避免副作用操作
- [ ] `derivedStateOf` 使用得当，避免无效重组
- [ ] `key()` 用于稳定列表项标识
- [ ] Modifier 链顺序正确（padding → background → clickable）
- [ ] `@Stable` 标注正确使用

### 4. 性能与内存
- [ ] 无主线程 I/O 操作
- [ ] Bitmap 使用后及时回收
- [ ] RecyclerView/Compose LazyList 正确复用
- [ ] WebView 生命周期管理正确
- [ ] 避免 Activity/Fragment 内存泄漏

### 5. 测试
- [ ] ViewModel 单元测试覆盖率 ≥ 80%
- [ ] Repository 使用 Fake 实现测试
- [ ] 测试命名遵循 `given_when_then` 格式
- [ ] Robolectric 测试不使用真实设备依赖

### 6. 安全
- [ ] 敏感信息未硬编码（API Key, Token）
- [ ] ProGuard 规则已更新（如有新增 @Keep）
- [ ] WebView `setJavaScriptEnabled` 有正当理由
- [ ] 输入校验覆盖所有外部数据源

## 输出格式

```markdown
## Code Review 结果

**文件**：`{file_path}`
**严重程度**：🔴 严重 / 🟡 警告 / 🟢 建议

### 🔴 严重问题
- {问题描述} (行 {N})
- 修复建议：{具体代码示例}

### 🟡 警告
- {问题描述} (行 {N})
- 修复建议：{具体代码示例}

### 🟢 建议
- {改进点}
```

### 动手实践

**任务**：为你的项目创建团队专属 Skill

1. 在 `.claude/skills/` 下创建 `android-dev/` 目录结构
2. 编写 `SKILL.md`，至少包含 3 个触发词
3. 创建 `references/module-map.md`（至少记录 3 个模块的依赖关系）
4. 用一句话描述你的 Skill，提交到团队仓库

**验证方法**：在 AI 工具中说触发词，确认 Skill 被加载，检查上下文是否包含你的项目信息。

<!-- TODO: 补充截图：Skill 加载成功后的对话界面 -->

### 踩坑记录

- **坑 1**："触发词设成 '*' 想让 Skill 每次都加载"——这会导致每次对话都注入大量上下文，Token 消耗暴涨，模型输出质量反而下降。
- **坑 2**："{{REFERENCES}} 路径写错"——引用路径相对于 SKILL.md 所在目录，不是项目根目录。
- **坑 3**："references 文件太长"——单文件超过 500 行会导致上下文占据过多 token 预算，建议拆分成多个小文件，按需加载。

---

## 6.4 维护与演进

### 本节目标

确保 Skill 体系不会变成"一次性配置"，建立可持续的维护机制。

### 正文

#### 1. Skills 版本管理策略

| 策略 | 适用场景 | 优点 | 缺点 |
|------|---------|------|------|
| **Git 子模块** | 社区 Skill 需要追踪上游更新 | 版本可控，可回溯 | 新人克隆需 `--recursive` |
| **独立仓库 + 符号链接** | 多项目共享同一套 Skill | 一处更新，多项目生效 | 跨平台兼容性（Windows） |
| **直接纳入项目仓库** | 团队自定义 Skill | 简单粗暴 | 多项目需手动同步 |
| **MCP 服务分发** | 100+ 人大团队 | 集中管理，灰度发布 | 需要服务端基础设施 |

推荐的小团队方案：

```bash
# 社区 Skill 用 Git 子模块管理
git submodule add https://github.com/example/android-skill.git \
  .claude/skills/community/android-code-review

# 团队自定义 Skill 直接纳入项目仓库
mkdir -p .claude/skills/team/android-dev/
```

```bash
# 更新所有社区 Skill 到最新版本
git submodule update --remote .claude/skills/community/*
```

#### 2. 定期审查周期

```text
┌──────────────────────────────────────────────────┐
│ 季度审查清单（建议每 3 个月执行一次）              │
├──────────────────────────────────────────────────┤
│ □ 审查所有启用 Skill 的 Token 消耗报告            │
│ □ 检查社区 Skill 是否有 Breaking Change 更新      │
│ □ 新增 Skill 评估（社区新涌现的好 Skill）         │
│ □ 废弃 Skill 清理（6 个月未使用且评分 < 3 的）    │
│ □ 自定义 Skill 的触发词是否需要调整               │
│ □ references 文件是否需要刷新（版本号、模块结构） │
│ □ 团队反馈收集（哪些 Skill 好用/不好用）          │
└──────────────────────────────────────────────────┘
```

#### 3. 团队共识建立

Skill 引入不是个人行为。让团队认可你的 Skill 配置：

```markdown
## Skill 提案模板

**提案人**：{姓名}
**日期**：{YYYY-MM-DD}

**Skill 名称**：{name}
**解决什么问题**：{当前痛点的具体描述，附实际案例}

**替代方案**：
1. {现有 Skill 替代} → 不推荐原因
2. {不引入} → 为什么不行

**预期收益**：
- Token 成本影响：预计 {+/-} X tokens/session
- 开发效率提升：预计 {X}%

**试用期**：2 周
**衡量指标**：{如何判断成功/失败}
**决策**：□ 批准  □ 拒绝  □ 有条件通过（条件：___）
```

### 动手实践

**任务**：建立团队 Skill 审查日历

1. 在团队日历中创建季度重复事件
2. 将上述审查清单录入事件描述
3. 指定轮值负责人
4. 第一次审查时填充模板记录结果

### 踩坑记录

- **坑 1**："配置完就不管了"——社区 Skill 的 Breaking Change 会让你某天突然发现功能失效。务必关注社区 Skill 仓库的 Release Notes。
- **坑 2**："每个人随意添加 Skill"——不加管理的 Skill 体系 3 个月后必然演变成上下文噪音沼泽。建立审批机制，即便是 3 人小团队。
- **坑 3**："子模块忘了更新"——CI 中加入检查脚本：如果子模块版本落后 3 个月以上，CI 发出 Warning。

```bash
#!/bin/bash
# 检查社区 Skill 子模块是否过期（加入 CI pipeline）
for skill in .claude/skills/community/*/; do
  last_commit=$(git -C "$skill" log -1 --format=%ct)
  three_months_ago=$(date -v-3m +%s)
  if [ "$last_commit" -lt "$three_months_ago" ]; then
    echo "⚠️  $(basename "$skill") 超过 3 个月未更新"
  fi
done
```

---

## 本章小结

现在你已经掌握：

1. **筛选矩阵** — 3 个维度 + 1 个公式 = 5 分钟锁定最佳 Skill 组合
2. **五步决策法** — 功能重叠时的标准决策流程
3. **自定义 Skill** — 从目录结构到触发词，从上下文注入到完整 CR Skill
4. **维护机制** — 季度审查 + 版本管理 + 团队共识

**下一步**：带上你的 Skill 配置，进入第 7 章《接入项目全流程》，学习如何在产品的每个阶段有效使用 AI。

<!-- TODO: 补充截图 -->
<!-- TODO: 补充各场景的 Token 消耗实测数据图表 -->
