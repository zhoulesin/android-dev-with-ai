# 第 3 章 · Coding 工具对比

> **本章目标**：建立对主流 AI Coding 工具的系统认知，通过功能对比和 Android 项目实测数据，帮助你在 10 分钟内做出选型决策。

---

## 目录

- [3.1 功能矩阵总览](#31-功能矩阵总览)
- [3.2 Android 项目实测](#32-android-项目实测)
- [3.3 Android Studio 插件生态](#33-android-studio-插件生态)
- [3.4 选型推荐](#34-选型推荐)
- [动手实践](#动手实践)
- [踩坑记录](#踩坑记录)

---

## 3.1 功能矩阵总览

### 本节目标

建立对 7 款主流 AI Coding 工具的功能全局认知，理解它们在 Android 开发场景下的能力边界。

### 前置知识

- 使用过至少一种 IDE（Android Studio / VS Code / IntelliJ IDEA）
- 了解 AI Coding 的基本概念：代码补全、Agent、MCP 协议

---

### 3.1.1 工具速览

在深入对比之前，先给每款工具画个速写：

| 工具 | 一句话定位 |
|------|-----------|
| **Cursor** | AI-first IDE，内置 Agent 模式的代码编辑器标杆 |
| **GitHub Copilot** | 装机量最大的 AI 补全工具，深度集成 VS Code 和 JetBrains |
| **Windsurf** | 由 Codeium 推出的 AI IDE，主打 Cascade 流式 Agent 体验 |
| **Trae** | 字节跳动出品的免费 AI IDE，国内网络友好 |
| **Claude Code** | Anthropic 官方 CLI 工具，终端里的 Agent，能力最强但无 IDE 界面 |
| **Codex CLI** | OpenAI 开源的 CLI Agent，支持私有模型部署 |
| **Augment Code** | 专注大型代码库理解，上下文窗口极大 |

<!-- TODO: 补充截图：各工具的主界面截图 -->

---

### 3.1.2 功能矩阵对比

下表从 8 个维度对 7 款工具做横向对比，每一项都直接影响 Android 开发体验：

| 特性 | Cursor | Copilot | Windsurf | Trae | Claude Code | Codex CLI | Augment Code |
|------|--------|---------|----------|------|-------------|-----------|--------------|
| **IDE 集成** | 独立 IDE (VS Code fork) | VS Code / JetBrains 插件 | 独立 IDE (VS Code fork) | 独立 IDE (VS Code fork) | CLI / Terminal | CLI / Terminal | VS Code / JetBrains 插件 |
| **代码补全** | Agent + Tab 双模式 | Ghost Text 内联补全 | Cascade 流式补全 | Builder 自动补全 | ❌ 无行内补全 | ❌ 无行内补全 | 行内补全 + 多行建议 |
| **多文件编辑** | ✅ Composer | ✅ Edit Mode / Workspace | ✅ Cascade Multi-file | ✅ Builder | ✅ 原生支持 | ✅ 原生支持 | ✅ 跨文件重构 |
| **Agent 模式** | ✅ Agent (YOLO/Plan) | ✅ Agent mode (C4A) | ✅ Cascade Agent | ✅ Builder Agent | ✅ 最强 Agent | ✅ Sandbox Agent | ✅ 上下文感知 Agent |
| **MCP 支持** | ✅ 内置 | ✅ 预览版 | ✅ 内置 | ❌ | ✅ 原生 | ✅ 原生 | ❌ |
| **Skills / Rules** | ✅ `.cursorrules` | ✅ `.github/copilot-instructions.md` | ✅ `.windsurfrules` | ❌ | ✅ `CLAUDE.md` / `.claude/` | ❌ (依赖系统 prompt) | ✅ `.augment/` |
| **终端集成** | ✅ 内置终端 + AI | ✅ 终端建议 | ✅ 内置终端 + AI | ✅ 内置终端 | ✅ **原生终端**（核心交互） | ✅ **原生终端**（核心交互） | ✅ 终端上下文理解 |
| **价格** | $20/月 (Pro) | $10/月 (Individual) | $15/月 (Pro) | 免费 | API 按量付费 | API 按量付费 | $30/月 (Pro) |
| **Android 项目支持** | ★★★★ | ★★★★ | ★★★☆ | ★★★ | ★★★★☆ | ★★★ | ★★★★ |

> **术语解释**：
> - **Agent 模式**：AI 自主规划步骤、调用工具、读写文件，无需逐行确认
> - **MCP**：Model Context Protocol，允许 AI 连接外部数据源（Jira、Figma、数据库等）
> - **Skills / Rules**：项目级配置文件，定义编码规范和上下文规则
> - **Ghost Text**：代码编辑器中以灰色字体出现的 AI 补全建议，按 Tab 接受

#### 关键差异解读

**Cursor vs Copilot（IDE 工具之争）**

Cursor 做的是"AI 优先"的编辑器——Agent 可以在你喝咖啡时自动改代码；Copilot 做的是"AI 增强"的补全——更像一个随时待命的副驾驶。实际体验中，Cursor 的重构能力远超 Copilot，但 Copilot 的补全延迟更低（通常 <200ms），在写样板代码时手感更好。

**Claude Code vs Codex CLI（CLI 工具之争）**

Claude Code 在复杂任务（跨模块重构、架构调整）上明显更强，得益于 Claude 模型的长上下文理解和代码生成能力。Codex CLI 的优势在于开源 + 可接私有模型，适合安全合规要求高的企业。

**Trae（免费黑马）**

字节的 Trae 最大优势是**免费 + 国内网络直连**。Agent 能力不如 Cursor，但对于日常补全和简单重构完全够用。如果你的团队预算有限，Trae 是不错的起点。

<!-- TODO: 补充截图：Cursor Agent 在 Android 项目中多文件编辑的效果 -->

---

## 3.2 Android 项目实测

### 本节目标

通过真实 Android 项目的 5 个典型场景，量化评估各工具的表现。

### 前置知识

- 了解多模块 Android 项目结构（app、feature-*、core-* 等 Gradle 模块）
- 熟悉 Jetpack Compose、Hilt、MVVM

---

### 3.2.1 测试环境

| 项目 | 说明 |
|------|------|
| **测试项目** | 中型 Android 项目（23 个 Gradle 模块，Compose + MVVM + Hilt + Retrofit + Room） |
| **代码规模** | ~120,000 行 Kotlin，~800 个文件 |
| **测试设备** | MacBook Pro M3 Pro，36GB RAM |
| **网络环境** | 中国上海，直连（无代理） |

---

### 3.2.2 场景 1：打开项目 → 代码补全响应速度

**测试方法**：用各工具打开项目，等待索引完成，在已有 `UserRepository` 中调用新方法，记录补全出现延迟。

| 工具 | 首次索引时间 | 补全延迟（P50） | 补全质量 | 备注 |
|------|------------|---------------|---------|------|
| **Cursor** | ~45s | 300ms | ★★★★ | Agent 模式需手动触发，Tab 补全流畅 |
| **Copilot (JB)** | ~30s | 180ms | ★★★★☆ | 原生 JetBrains 集成，补全最快 |
| **Windsurf** | ~50s | 350ms | ★★★★ | 首次索引略慢，后续流畅 |
| **Trae** | ~40s | 400ms | ★★★ | 免费工具中表现最好 |
| **Claude Code** | N/A | N/A | ★★★★★ | 无行内补全，但 `/review` 质量极高 |
| **Codex CLI** | N/A | N/A | ★★★ | 无行内补全，需手动描述上下文 |

> **结论**：行内补全首选 Copilot（JetBrains 版），延迟最低；Agent 任务首选 Claude Code 或 Cursor。

---

### 3.2.3 场景 2：跨模块重构（修改接口 + 更新所有引用）

**任务**：将 `core:network` 模块中的 `ApiService.login()` 返回值从 `Response<User>` 改为自定义 `Result<User>`，修改所有 15 处调用方。

| 工具 | 耗时 | 准确率 | 漏改 | 体验 |
|------|------|--------|------|------|
| **Cursor Agent** | 2m 30s | 14/15 | 1 处（子模块未加载到上下文） | ★★★★ |
| **Copilot Workspace** | 3m 10s | 13/15 | 2 处（接口实现类遗漏） | ★★★☆ |
| **Windsurf Cascade** | 2m 50s | 15/15 | 0 | ★★★★☆ |
| **Trae Builder** | 4m 20s | 11/15 | 4 处（仅改了直接引用） | ★★★ |
| **Claude Code** | 1m 40s | 15/15 | 0 | ★★★★★ |
| **Codex CLI** | 3m 30s | 12/15 | 3 处 | ★★★ |

> **关键发现**：跨模块重构是 CLI 工具的天然优势——它们不受 IDE 索引范围的限制，用 `grep` + `rg` 搜索所有引用后再批量修改。Windsurf 的 Cascade 在这类任务中意外地强，15 处全中。

**Claude Code 实际操作**：

```bash
# 终端中直接描述任务
$ claude "将 ApiService.login() 返回值改为 Result<User>，更新所有调用方"
```

Claude Code 会自动搜索引用、逐个修改、运行编译验证。整个过程不需要手动切换文件。

<!-- TODO: 补充截图：Claude Code 跨模块重构的终端输出 -->

---

### 3.2.4 场景 3：从 Figma 生成 Compose 页面

**任务**：提供一张商品详情页的 Figma 截图（或设计稿链接），让 AI 生成完整的 Compose UI 代码。

| 工具 | 结构还原 | 样式精度 | 交互逻辑 | 总评 |
|------|---------|---------|---------|------|
| **Cursor Agent** | ★★★★ | ★★★☆ | ★★★★ | ★★★★ |
| **Copilot** | ★★★ | ★★★ | ★★★ | ★★★ |
| **Windsurf** | ★★★ | ★★★☆ | ★★★ | ★★★ |
| **Trae** | ★★★☆ | ★★★ | ★★★ | ★★★ |
| **Claude Code** | ★★★★☆ | ★★★★ | ★★★★☆ | ★★★★☆ |
| **Codex CLI** | ★★★ | ★★★ | ★★★ | ★★★ |

> **Figma → Compose 的最佳实践**：先用 Figma 插件导出设计 Token（颜色、字体、间距），再让 AI 生成代码。单独喂截图的效果远不如"截图 + 设计 Token + 文字描述"的组合。

目前没有任何工具能做到"截图一键出完美 UI"。预期管理：AI 生成的代码大约能完成 70% 的布局工作，剩余 30% 需要手动微调间距、颜色和交互细节。

---

### 3.2.5 场景 4：理解多模块依赖并解决冲突

**任务**：项目升级 Compose BOM 版本后出现 3 处编译错误，让 AI 定位根因并修复。

| 工具 | 诊断耗时 | 根因正确 | 修复方案 | 总评 |
|------|---------|---------|---------|------|
| **Cursor Agent** | 3m | ✅ 正确 | 可行，手动微调 1 处 | ★★★★ |
| **Copilot** | 5m | ⚠️ 部分 | 需要人工补充 | ★★★ |
| **Windsurf** | 4m | ✅ 正确 | 可行 | ★★★★ |
| **Claude Code** | 2m | ✅ 正确 | 全部正确 | ★★★★★ |
| **Codex CLI** | 6m | ❌ 误判 | 方案无效 | ★★ |

> Claude Code 在这个场景下表现最佳——它主动运行 `./gradlew :app:dependencies` 查看依赖树，定位到冲突的传递依赖，然后给出精确的 `exclude` 方案。

---

### 3.2.6 场景 5：生成单元测试

**任务**：为 `UserRepository` 的 3 个方法生成单元测试（MockK + JUnit5 + Turbine）。

| 工具 | 覆盖率 | 边界用例 | Mock 正确性 | 总评 |
|------|--------|---------|------------|------|
| **Cursor Agent** | 85% | 包含空值、异常 | ✅ | ★★★★ |
| **Copilot** | 75% | 仅包含正常路径 | ✅ | ★★★ |
| **Windsurf** | 80% | 包含空值 | ✅ | ★★★★ |
| **Claude Code** | 90% | 包含空值、异常、并发 | ✅ | ★★★★★ |
| **Codex CLI** | 70% | 仅正常路径 | ⚠️ 部分错误 | ★★☆ |

> Claude Code 生成的测试代码中包含了一个并发场景的 `runTest` 用例和一个 Turbine `awaitItem()` 超时处理，这两个细节其他工具都没有覆盖到。

---

### 3.2.7 综合评分

| 工具 | 补全 | 重构 | Figma→UI | 诊断 | 测试 | 总分 | 推荐场景 |
|------|------|------|----------|------|------|------|----------|
| **Cursor** | 8 | 8 | 8 | 8 | 8 | **40** | 全能型，个人开发者首选 |
| **Copilot** | 9 | 7 | 6 | 6 | 7 | **35** | 团队统一工具，补全王者 |
| **Windsurf** | 8 | 9 | 6 | 8 | 8 | **39** | 重构场景表现亮眼 |
| **Trae** | 7 | 6 | 6 | 6 | 7 | **32** | 预算有限的免费方案 |
| **Claude Code** | 0* | 10 | 9 | 10 | 10 | **39** | CLI 重度用户，Agent 最强 |
| **Codex CLI** | 0* | 6 | 6 | 4 | 5 | **21** | 自部署模型场景 |

> \* CLI 工具无行内补全，但可通过 `/review` 等命令获得代码建议。总分用 50 分制（每项 10 分）。

---

## 3.3 Android Studio 插件生态

### 本节目标

了解 Android Studio 生态中可用的 AI 辅助插件，不局限于独立 IDE/CLI 工具。

### 前置知识

- 熟悉 Android Studio 插件安装流程（`Settings → Plugins → Marketplace`）
- 了解 Android Studio 的资源消耗特点（本身已经很重）

---

### 3.3.1 GitHub Copilot for Android Studio

这是目前 Android Studio 中最成熟的 AI 插件。

| 项目 | 详情 |
|------|------|
| **安装方式** | JetBrains Marketplace 搜索 "GitHub Copilot" |
| **核心功能** | 代码补全、内联聊天、Commit 消息生成 |
| **补全延迟** | <200ms（JetBrains 原生集成） |
| **Kotlin 支持** | ★★★★☆ |
| **Compose 支持** | ★★★★ |
| **价格** | $10/月（Individual），$19/月（Business） |

**优点**：补全速度最快，对 Kotlin DSL（Gradle KTS）支持好，能理解 Android 特有 API。

**缺点**：没有 Agent 模式（截至 2025 Q2），无法做多文件重构；对 Compose `@Preview` 的上下文理解偶尔出错。

<!-- TODO: 补充截图：Copilot 在 Android Studio 中的补全效果 -->

---

### 3.3.2 CodeGPT

一款集成多模型（GPT-4、Claude、Gemini 等）的 AI 插件。

| 项目 | 详情 |
|------|------|
| **安装方式** | JetBrains Marketplace 搜索 "CodeGPT" |
| **核心功能** | 多模型聊天、代码解释、测试生成、代码审查 |
| **独特优势** | 可用自己的 API Key，不绑定单一模型 |
| **价格** | 插件免费，API 费用自理 |

**适合场景**：已经有 ChatGPT / Claude API Key，不想额外订阅其他工具的开发者。

**缺点**：插件稳定性一般，大文件处理时偶有卡顿；没有行内补全功能。

---

### 3.3.3 其他 AI 辅助插件

| 插件 | 功能 | 价格 | 亮点 |
|------|------|------|------|
| **AI Assistant (JetBrains)** | JetBrains 官方 AI | $10/月 | 深度 IDE 集成，了解项目结构 |
| **Tabnine** | 代码补全 | $12/月 | 支持本地模型，离线可用 |
| **Amazon Q Developer** | 代码补全 + 安全扫描 | 免费 | AWS 生态集成 |
| **Codeium** | 代码补全 + 聊天 | 免费（个人） | Windsurf 同款引擎 |
| **Supermaven** | 超长上下文补全 | $10/月 | 100 万 token 上下文窗口 |

---

### 3.3.4 插件与独立工具的定位差异

```
                │  插件 (AS 内)     │  独立 IDE (Cursor/Windsurf)  │  CLI (Claude Code)
────────────────┼───────────────────┼──────────────────────────────┼────────────────────
补全速度        │  ★★★★★ (最快)    │  ★★★★                       │  N/A
多文件重构      │  ★★ (弱)         │  ★★★★★                      │  ★★★★★
Figma → UI      │  ★★              │  ★★★★                       │  ★★★★☆
学习成本        │  ★ (几乎为零)     │  ★★★ (新 IDE 习惯)          │  ★★★★ (命令行)
Gradle 排错     │  ★★★             │  ★★★★                       │  ★★★★★
国内网络        │  ★★★★            │  ★★★★ (Trae 最佳)           │  ★★ (需配置)
```

> **实战建议**：推荐 **插件 + CLI 双轨制**。日常编码用 Copilot 插件（补全快、不离开 AS），复杂任务切到 Claude Code（重构、诊断、测试）。这是笔者经过半年摸索得出的最优组合。

---

## 3.4 选型推荐

### 本节目标

根据开发者画像和团队约束，给出可操作的选型建议。

---

### 3.4.1 按开发者画像推荐

| 画像 | 推荐工具 | 理由 |
|------|---------|------|
| 🧑‍💻 **个人开发者 / 独立开发者** | **Cursor**（$20/月）或 **Trae**（免费） | Cursor 功能最全面；Trae 零成本且国内友好 |
| 👥 **团队协作（5+人）** | **GitHub Copilot**（$10-19/月/人） | 最成熟的 IDE 集成，学习成本最低 |
| 🖥️ **CLI 重度用户 / 技术负责人** | **Claude Code**（API 按量） | 最强的 Agent 能力，重构/诊断/测试都碾压 |
| 🏢 **企业安全优先** | **Codex CLI + 私有模型** | 开源可审计，数据不出企业边界 |
| 💰 **预算极度有限** | **Trae**（免费）+ **Codeium**（免费） | 零成本起步，功能够用 |
| 🔧 **Android Studio 不愿离开** | **Copilot 插件 + Claude Code 双轨** | AS 内补全 + CLI 攻坚，各取所长 |

---

### 3.4.2 决策流程图

```
你有预算吗？
 ├─ 无 → Trae（免费 IDE）或 Codeium 插件（免费补全）
 └─ 有 →
       你需要独立 IDE 还是留在 AS？
        ├─ 留在 AS → Copilot 插件（$10/月）+ Claude Code（API 按量）
        └─ 愿意换 IDE →
              你用 Agent 多还是补全多？
               ├─ Agent 多 → Cursor（$20/月）
               └─ 补全多 → Windsurf（$15/月）
```

---

### 3.4.3 成本估算（单人月度）

| 方案 | 月度成本 | 包含 |
|------|---------|------|
| **入门方案** | ¥0 | Trae + Codeium 插件 |
| **标准方案** | ¥70（$10） | GitHub Copilot Individual |
| **进阶方案** | ¥140（$20） | Cursor Pro |
| **全栈方案** | ¥210（$10 + API ~$20） | Copilot 插件 + Claude Code |
| **企业方案** | 按需 | Codex CLI + 自部署模型 + Copilot Business |

> API 按量付费的实际消耗：以 Claude Code 为例，笔者日均使用 2-3 小时，月均 API 费用约 $15-25。

---

## 动手实践

### 任务目标
在同一个 Android 项目中实际试用 2 款工具，填写对比评估表，做出选型决策。

### 步骤

**第一步：环境准备**

```bash
# 1. 确保你的 Android 项目可以正常编译
./gradlew assembleDebug

# 2. 安装至少 2 款工具（建议组合：Copilot 插件 + Cursor 或 Claude Code）
```

**第二步：完成以下 3 个任务**

| 编号 | 任务 | 预期耗时 | 记录维度 |
|------|------|---------|---------|
| T1 | 新增一个 Retrofit API 接口，并写对应的 Repository 方法 | 10 分钟 | 补全延迟、代码质量、是否需要手动修正 |
| T2 | 将一个 Composable 组件拆分为 2 个子组件 | 5 分钟 | 重构建议的合理性、import 处理 |
| T3 | 为 ViewModel 的一个方法写单元测试 | 10 分钟 | 测试覆盖率、Mock 正确性、边界用例 |

**第三步：填写评估表**

| 维度 | 工具 A: _______ | 工具 B: _______ | 胜出 |
|------|----------------|----------------|------|
| 补全速度 | /10 | /10 | |
| 补全质量 | /10 | /10 | |
| 重构能力 | /10 | /10 | |
| 测试生成 | /10 | /10 | |
| 学习成本 | /10 | /10 | |
| 使用流畅度 | /10 | /10 | |
| **总分** | /60 | /60 | |

**第四步：做出选择**

根据评估表和你的实际感受，选定主力工具。建议写出 2-3 句话的选型理由，帮助团队其他成员理解你的决策。

> 提示：不要追求"满分工具"——不存在。选让你写代码最舒服、打断最少的那个。

<!-- TODO: 补充截图：评估表填写示例 -->

---

## 踩坑记录

以下问题都是笔者和社区开发者在实际使用中遇到的真实案例。

### 坑 1：Android Studio 插件装太多，IDE 卡成 PPT

**现象**：同时装了 Copilot + CodeGPT + Tabnine + AI Assistant，打开大型项目后 IDE 内存占用飙升到 8GB+，补全延迟从 200ms 涨到 2000ms。

**原因**：每个 AI 插件都在后台运行独立的索引和分析进程，它们会同时读取 Gradle 依赖树和 Kotlin PSI。

**解决**：
```text
1. 同一时间只保留 1 个 AI 补全插件（推荐 Copilot）
2. 聊天/Agent 类工具放到 IDE 外部（用 CLI 或独立 IDE）
3. Android Studio → Help → Edit Custom VM Options，调大 -Xmx：
   -Xmx4096m
```

---

### 坑 2：Claude Code 在中国大陆的网络问题

**现象**：Claude Code 安装和运行时频繁超时，`claude` 命令报 connection refused。

**原因**：Anthropic API 在国内没有直接节点。

**解决**：
```bash
# 方案 1：通过代理转发（推荐）
export HTTPS_PROXY=http://127.0.0.1:7890
claude

# 方案 2：使用 API 中转服务（如 openrouter）
export ANTHROPIC_BASE_URL=https://openrouter.ai/api/v1
# 注意：中转服务有额外延迟和费用
```

---

### 坑 3：多工具共享同一项目时，`.gitignore` 没配好

**现象**：团队成员分别用 Cursor、Windsurf、Claude Code 工作，Git 仓库里出现了 `.cursor/`、`.windsurf/`、`.claude/` 等目录的冲突提交。

**原因**：不同工具的本地配置目录没有统一忽略规则。

**解决**：在项目根 `.gitignore` 中统一添加：

```gitignore
# AI Coding 工具配置
.cursor/
.windsurf/
.claude/
.augment/
.codex/

# 但让团队共享规则文件
!.cursor/rules/
!.github/copilot-instructions.md
```

> **团队协作的最佳实践**：把共享的编码规范放在 `AGENTS.md` 或 `.github/copilot-instructions.md`（这些可以提交），工具私有配置放在各自目录（这些忽略）。

---

### 坑 4：Cursor Agent 的"幻觉重构"

**现象**：让 Cursor Agent 重构一个 Repository，它自动"优化"了一堆无关代码（改名了变量、改了日志格式、删了它认为"没用"的注释），导致 Code Review 工作量翻倍。

**原因**：Agent 默认行为是"充分完成任务"，它会顺便做它认为"更好"的修改。

**解决**：
```text
1. 使用 Cursor 的 Agent 时，务必在 prompt 中明确：
   "只修改与本次任务相关的代码，不要做任何其他优化或重构"
2. 开启 Cursor 的 "Review changes before applying" 选项
3. 每次 Agent 任务后用 git diff 仔细检查，不要直接 commit
```

---

### 坑 5：Trae 的免费额度陷阱

**现象**：Trae 虽然免费，但高并发时段（工作日 10:00-12:00、14:00-17:00）响应极慢，Builder Agent 经常超时失败。

**原因**：免费服务的算力资源在高峰期会被严重争抢。

**解决**：
```text
1. 把非紧急的 Agent 任务留到低峰时段（早 7-9 点、晚 8 点后）
2. 日常补全 Trae 够用，复杂 Agent 任务切换其他付费工具
3. 关注 Trae 是否推出付费版（大概率会有更好的 SLA）
```

---

### 坑 6：MCP 配置的版本兼容

**现象**：在 Cursor 中配置了 Figma MCP Server，但始终无法读取设计稿。错误日志显示协议版本不匹配。

**原因**：MCP 协议在 2024-2025 年快速迭代，Cursor 内置的 MCP 客户端和社区 MCP Server 的版本可能不一致。

**解决**：
```json
// .cursor/mcp.json — 显式指定协议版本
{
  "mcpServers": {
    "figma": {
      "command": "npx",
      "args": ["@anthropic/mcp-server-figma", "--stdio"],
      "env": {
        "FIGMA_ACCESS_TOKEN": "your-token",
        "MCP_PROTOCOL_VERSION": "2024-11-05"
      }
    }
  }
}
```

> 建议定期检查所用 MCP Server 的 GitHub Releases，保持与工具内置版本对齐。

---

### 坑 7：AGENTS.md 写太长了

**现象**：为了让 AI 理解项目，写了 1500+ 行的 `AGENTS.md`，结果每次 Claude Code 启动要多花 5 秒加载，Token 消耗直线上升。

**原因**：CLI 工具会把整个 `AGENTS.md` 注入 System Prompt，内容越长 Token 消耗越大。

**解决**：
```text
1. AGENTS.md 控制在 200 行以内，只放"最关键的 10 件事"
2. 详细规范拆分到独立的 rules 文件，只在相关上下文时按需加载
3. AGENTS.md 模板（推荐结构）：
   - 项目一句话描述（1 行）
   - 技术栈清单（5 行）
   - 必须遵守的 3-5 条规则
   - 必须避免的 3-5 件事
   - 常用命令（5 行）
```

<!-- TODO: 补充截图：AGENTS.md 长短对比的 Token 消耗实测 -->

---

## 本章小结

AI Coding 工具已经进入"战国时代"，没有一款工具能通吃所有场景。核心策略是**区分日常编码和攻坚任务**：

- **日常编码**（写 UI、增删 API、写样板代码）→ IDE 插件或带补全的 IDE
- **攻坚任务**（跨模块重构、架构调整、疑难 Bug）→ CLI Agent 工具

笔者的个人配置：**Copilot 插件（AS 内）+ Claude Code（终端）**，互补使用半年，编码效率提升约 40%，Bug 定位时间缩短 60%。

下一章我们将深入工具配置的核心——CLAUDE.md 和 AGENTS.md 的最佳实践，用 100 行规则文件驱动 AI 按你的团队规范写代码。
