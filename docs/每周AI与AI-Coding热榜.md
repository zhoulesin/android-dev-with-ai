# 每周 AI / AI-Coding GitHub 热榜追踪（自 2026-01 起）

> 时效性标注
> - 最后更新：2026-05
> - 统计周期：2026-01 ~ 至今（周维度）
> - 适用说明：热榜波动快，本页用于“记录趋势”，不作为单一选型依据

这页用于固定记录每周最热的 AI 与 AI-Coding 相关 GitHub 仓库，避免被碎片化信息流带偏。

## 一、现成可用的数据源（已联网核验）

### 1) 官方周榜（主入口）

- GitHub Trending Weekly：  
  [https://github.com/trending?since=weekly](https://github.com/trending?since=weekly)

说明：官方源，最稳；但不按 AI 主题自动过滤，需要人工筛选 AI / AI-Coding 相关项目。

### 2) 历史归档（可追溯到 2026-01）

- `bonfy/github-trending`（按日期沉淀，便于回看周内热点）  
  [https://github.com/bonfy/github-trending](https://github.com/bonfy/github-trending)

已检索到 2026-01 的归档样例：
- [2026-01-06](https://github.com/bonfy/github-trending/blob/master/2026-01-06.md)
- [2026-01-09](https://github.com/bonfy/github-trending/blob/master/2026-01-09.md)
- [2026-01-20](https://github.com/bonfy/github-trending/blob/master/2026-01-20.md)

### 3) AI-Coding 专项补充（非周榜，但可用于候选池）

- `VoltAgent/awesome-claude-skills`  
  [https://github.com/VoltAgent/awesome-claude-skills](https://github.com/VoltAgent/awesome-claude-skills)
- `spencerpauly/awesome-cursor-skills`  
  [https://github.com/spencerpauly/awesome-cursor-skills](https://github.com/spencerpauly/awesome-cursor-skills)
- `cline/cline`（生态与能力变化观察）  
  [https://github.com/cline/cline](https://github.com/cline/cline)
- `RooCodeInc/Roo-Code`（生态与能力变化观察）  
  [https://github.com/RooCodeInc/Roo-Code](https://github.com/RooCodeInc/Roo-Code)

## 二、筛选准则（可复现）

为避免“主观拍脑袋”，每周入选仓库按以下规则筛选。

### 1) 纳入条件（满足任意两条）

- 本周在 Trending/归档中出现，且与 AI / AI-Coding 直接相关  
- 对开发流程有明确可落地价值（如 Skills、MCP、Agent Harness、评测/治理工具）  
- 在近 4 周内出现重复上榜，体现持续热度而非一次性噪声  
- 有可验证的仓库活动信号（近期提交、Issue 讨论、版本发布等）

### 2) 排除条件（命中任意一条）

- 与 AI-Coding 关联弱，仅“蹭 AI 关键词”的泛工具仓库  
- 明显违规、灰黑产、可疑引流或高风险合规内容  
- 镜像/搬运/空壳仓库，缺少可验证维护行为  
- 同一能力位的重复候选过多（保留代表性最强的一个）

### 3) 每周选取配比

- `AI 通用`：2 个（模型能力、Agent 框架、评测/观测基础设施）
- `AI-Coding`：3 个（Skills、MCP、IDE/CLI 代理生态、工程工具链）
- 若某周 AI-Coding 信号明显更强，可调整为 1+4，但需在备注写明原因

### 4) 同名/同类去重规则

- 同周同主题只保留一个“主代表仓库”，其余记入备注  
- 跨周连续出现的仓库，允许保留，但备注需补“新变化点”  
- 若只是改名或分叉热度，优先保留官方主仓

### 5) 记录最小字段（不可省）

- 仓库链接 + 一句话价值定位 + 数据源链接 + 备注（趋势判断）

## 三、每周记录（从 2026-01 开始）

> 规则：每周固定一个观察日，记录约 5 个代表仓库（建议 AI 通用 2 个 + AI-Coding 3 个），并遵循上面的筛选准则。

| 周次 | 观察日期 | AI 通用热仓（约2） | AI-Coding 热仓（约3） | 数据源链接 | 备注 |
|---|---|---|---|---|---|
| 2026-W02 | 2026-01-06 | [langgenius/dify](https://github.com/langgenius/dify), [NVIDIA/TensorRT-LLM](https://github.com/NVIDIA/TensorRT-LLM) | [github/spec-kit](https://github.com/github/spec-kit), [datawhalechina/hello-agents](https://github.com/datawhalechina/hello-agents), [charmbracelet/crush](https://github.com/charmbracelet/crush) | [2026-01-06](https://raw.githubusercontent.com/bonfy/github-trending/master/2026-01-06.md) | 初始化样本 |
| 2026-W03 | 2026-01-13 | [OpenBMB/ChatDev](https://github.com/OpenBMB/ChatDev), [open-webui/open-webui](https://github.com/open-webui/open-webui) | [ruvnet/claude-flow](https://github.com/ruvnet/claude-flow), [github/awesome-copilot](https://github.com/github/awesome-copilot), [github/github-mcp-server](https://github.com/github/github-mcp-server) | [2026-01-13](https://raw.githubusercontent.com/bonfy/github-trending/master/2026-01-13.md) | Agent+Skills 升温 |
| 2026-W04 | 2026-01-20 | [google/langextract](https://github.com/google/langextract), [The-Pocket/PocketFlow](https://github.com/The-Pocket/PocketFlow) | [ComposioHQ/awesome-claude-skills](https://github.com/ComposioHQ/awesome-claude-skills), [davila7/claude-code-templates](https://github.com/davila7/claude-code-templates), [github/github-mcp-server](https://github.com/github/github-mcp-server) | [2026-01-20](https://raw.githubusercontent.com/bonfy/github-trending/master/2026-01-20.md) | Skills 生态增强 |
| 2026-W05 | 2026-01-27 | [assafelovic/gpt-researcher](https://github.com/assafelovic/gpt-researcher), [Comfy-Org/ComfyUI](https://github.com/Comfy-Org/ComfyUI) | [firecrawl/firecrawl-mcp-server](https://github.com/firecrawl/firecrawl-mcp-server), [yctimlin/mcp_excalidraw](https://github.com/yctimlin/mcp_excalidraw), [danielmiessler/Fabric](https://github.com/danielmiessler/Fabric) | [2026-01-27](https://raw.githubusercontent.com/bonfy/github-trending/master/2026-01-27.md) | MCP 相关高频出现 |
| 2026-W06 | 2026-02-03 | [microsoft/agent-lightning](https://github.com/microsoft/agent-lightning), [microsoft/BitNet](https://github.com/microsoft/BitNet) | [davila7/claude-code-templates](https://github.com/davila7/claude-code-templates), [GreyDGL/PentestGPT](https://github.com/GreyDGL/PentestGPT), [looplj/axonhub](https://github.com/looplj/axonhub) | [2026-02-03](https://raw.githubusercontent.com/bonfy/github-trending/master/2026-02-03.md) | 模型训练+工具链并进 |
| 2026-W07 | 2026-02-10 | [OpenBMB/MiniCPM-o](https://github.com/OpenBMB/MiniCPM-o), [google/langextract](https://github.com/google/langextract) | [openai/skills](https://github.com/openai/skills), [ComposioHQ/awesome-claude-skills](https://github.com/ComposioHQ/awesome-claude-skills), [github/gh-aw](https://github.com/github/gh-aw) | [2026-02-10](https://raw.githubusercontent.com/bonfy/github-trending/master/2026-02-10.md) | 官方技能库出现 |
| 2026-W08 | 2026-02-17 | [anthropics/claude-quickstarts](https://github.com/anthropics/claude-quickstarts), [kyutai-labs/moshi](https://github.com/kyutai-labs/moshi) | [hesreallyhim/awesome-claude-code](https://github.com/hesreallyhim/awesome-claude-code), [github/gh-aw](https://github.com/github/gh-aw), [github/gh-aw-mcpg](https://github.com/github/gh-aw-mcpg) | [2026-02-17](https://raw.githubusercontent.com/bonfy/github-trending/master/2026-02-17.md) | GitHub Agentic Workflows 走强 |
| 2026-W09 | 2026-02-24 | [huggingface/skills](https://github.com/huggingface/skills), [NevaMind-AI/memU](https://github.com/NevaMind-AI/memU) | [muratcankoylan/Agent-Skills-for-Context-Engineering](https://github.com/muratcankoylan/Agent-Skills-for-Context-Engineering), [siteboon/claudecodeui](https://github.com/siteboon/claudecodeui), [xpzouying/xiaohongshu-mcp](https://github.com/xpzouying/xiaohongshu-mcp) | [2026-02-24](https://raw.githubusercontent.com/bonfy/github-trending/master/2026-02-24.md) | 记忆与上下文工程升温 |
| 2026-W10 | 2026-03-03 | [alibaba/OpenSandbox](https://github.com/alibaba/OpenSandbox), [X-PLUG/MobileAgent](https://github.com/X-PLUG/MobileAgent) | [K-Dense-AI/claude-scientific-skills](https://github.com/K-Dense-AI/claude-scientific-skills), [davila7/claude-code-templates](https://github.com/davila7/claude-code-templates), [asheshgoplani/agent-deck](https://github.com/asheshgoplani/agent-deck) | [2026-03-03](https://raw.githubusercontent.com/bonfy/github-trending/master/2026-03-03.md) | Sandbox + Agent IDE 化 |
| 2026-W11 | 2026-03-10 | [NousResearch/hermes-agent](https://github.com/NousResearch/hermes-agent), [hpcaitech/Open-Sora](https://github.com/hpcaitech/Open-Sora) | [alirezarezvani/claude-skills](https://github.com/alirezarezvani/claude-skills), [apify/agent-skills](https://github.com/apify/agent-skills), [modelcontextprotocol/swift-sdk](https://github.com/modelcontextprotocol/swift-sdk) | [2026-03-10](https://raw.githubusercontent.com/bonfy/github-trending/master/2026-03-10.md) | 多生态 skills 并行 |
| 2026-W12 | 2026-03-17 | [volcengine/OpenViking](https://github.com/volcengine/OpenViking), [langchain-ai/deepagents](https://github.com/langchain-ai/deepagents) | [hesreallyhim/awesome-claude-code](https://github.com/hesreallyhim/awesome-claude-code), [decolua/9router](https://github.com/decolua/9router), [badlogic/pi-skills](https://github.com/badlogic/pi-skills) | [2026-03-17](https://raw.githubusercontent.com/bonfy/github-trending/master/2026-03-17.md) | 多客户端互通需求上升 |
| 2026-W13 | 2026-03-24 | [bytedance/deer-flow](https://github.com/bytedance/deer-flow), [browser-use/browser-use](https://github.com/browser-use/browser-use) | [hesreallyhim/awesome-claude-code](https://github.com/hesreallyhim/awesome-claude-code), [affaan-m/everything-claude-code](https://github.com/affaan-m/everything-claude-code), [github/github-mcp-server](https://github.com/github/github-mcp-server) | [2026-03-24](https://raw.githubusercontent.com/bonfy/github-trending/master/2026-03-24.md) | “harness + skills + mcp”组合明确 |
| 2026-W14 | 2026-03-31 | [SakanaAI/AI-Scientist-v2](https://github.com/SakanaAI/AI-Scientist-v2), [microsoft/agent-lightning](https://github.com/microsoft/agent-lightning) | [luongnv89/claude-howto](https://github.com/luongnv89/claude-howto), [github/CopilotForXcode](https://github.com/github/CopilotForXcode), [manaflow-ai/cmux](https://github.com/manaflow-ai/cmux) | [2026-03-31](https://raw.githubusercontent.com/bonfy/github-trending/master/2026-03-31.md) | 终端侧 Agent 监控工具出现 |
| 2026-W15 | 2026-04-07 | [NousResearch/hermes-agent](https://github.com/NousResearch/hermes-agent), [HKUDS/DeepTutor](https://github.com/HKUDS/DeepTutor) | [vercel-labs/agent-skills](https://github.com/vercel-labs/agent-skills), [affaan-m/everything-claude-code](https://github.com/affaan-m/everything-claude-code), [ComposioHQ/awesome-claude-plugins](https://github.com/ComposioHQ/awesome-claude-plugins) | [2026-04-07](https://raw.githubusercontent.com/bonfy/github-trending/master/2026-04-07.md) | 官方/社区插件化并进 |
| 2026-W16 | 2026-04-14 | [shiyu-coder/Kronos](https://github.com/shiyu-coder/Kronos), [microsoft/VibeVoice](https://github.com/microsoft/VibeVoice) | [gsd-build/get-shit-done](https://github.com/gsd-build/get-shit-done), [coreyhaines31/marketingskills](https://github.com/coreyhaines31/marketingskills), [badlogic/pi-skills](https://github.com/badlogic/pi-skills) | [2026-04-14](https://raw.githubusercontent.com/bonfy/github-trending/master/2026-04-14.md) | 垂直技能库开始细分 |
| 2026-W17 | 2026-04-21 | [openai/openai-agents-python](https://github.com/openai/openai-agents-python), [kyegomez/swarms](https://github.com/kyegomez/swarms) | [coreyhaines31/marketingskills](https://github.com/coreyhaines31/marketingskills), [bugbasesecurity/pentest-copilot](https://github.com/bugbasesecurity/pentest-copilot), [github/CopilotForXcode](https://github.com/github/CopilotForXcode) | [2026-04-21](https://raw.githubusercontent.com/bonfy/github-trending/master/2026-04-21.md) | 框架与垂直代理并行 |
| 2026-W18 | 2026-04-28 | [deepseek-ai/DeepSeek-V3](https://github.com/deepseek-ai/DeepSeek-V3), [openai/openai-cs-agents-demo](https://github.com/openai/openai-cs-agents-demo) | [ComposioHQ/awesome-codex-skills](https://github.com/ComposioHQ/awesome-codex-skills), [browserbase/skills](https://github.com/browserbase/skills), [CJackHwang/ds2api](https://github.com/CJackHwang/ds2api) | [2026-04-28](https://raw.githubusercontent.com/bonfy/github-trending/master/2026-04-28.md) | Codex skills 生态抬头 |
| 2026-W19 | 2026-05-05 | [MervinPraison/PraisonAI](https://github.com/MervinPraison/PraisonAI), [LearningCircuit/local-deep-research](https://github.com/LearningCircuit/local-deep-research) | [decolua/9router](https://github.com/decolua/9router), [browserbase/skills](https://github.com/browserbase/skills), [raullenchai/Rapid-MLX](https://github.com/raullenchai/Rapid-MLX) | [2026-05-05](https://raw.githubusercontent.com/bonfy/github-trending/master/2026-05-05.md) | 本地推理+代理桥接增强 |
| 2026-W20 | 2026-05-12 | [CloakHQ/CloakBrowser](https://github.com/CloakHQ/CloakBrowser), [bytedance/UI-TARS](https://github.com/bytedance/UI-TARS) | [huggingface/skills](https://github.com/huggingface/skills), [wanshuiyin/Auto-claude-code-research-in-sleep](https://github.com/wanshuiyin/Auto-claude-code-research-in-sleep), [justlovemaki/AIClient2API](https://github.com/justlovemaki/AIClient2API) | [2026-05-12](https://raw.githubusercontent.com/bonfy/github-trending/master/2026-05-12.md) | 周榜样本已覆盖到 2026-05 |

> 后续周次可按同样格式追加；建议每月补一段“趋势点评”。

## 四、每月趋势总结（2026-01 ~ 2026-05）

### 2026-01（W02-W05）

- **MCP 开始从“概念”走向“工具形态”**：`github/github-mcp-server`、`firecrawl/firecrawl-mcp-server`、`mcp_excalidraw` 连续出现，说明“模型直连工具”成为主流需求。  
- **Skills 生态进入目录化阶段**：`ComposioHQ/awesome-claude-skills`、`github/spec-kit` 等出现频次高，团队开始把技能资产化。  
- **Agent 框架与研究型项目并行上升**：`ChatDev`、`gpt-researcher`、`PocketFlow` 等显示“从 demo 到任务编排”的转向。

### 2026-02（W06-W09）

- **官方技能仓库信号增强**：`openai/skills`、`huggingface/skills` 热度上升，说明“技能作为产品层接口”逐步标准化。  
- **Agentic Workflows 工程化加速**：`github/gh-aw` 与 `gh-aw-mcpg` 同时出现，表明 GitHub 场景进入“可编排工作流”阶段。  
- **上下文工程/记忆能力成为新焦点**：`memU`、`Agent-Skills-for-Context-Engineering` 等项目抬头，反映“长流程任务稳定性”成为核心矛盾。

### 2026-03（W10-W14）

- **“Agent harness + skill system + MCP”组合成型**：`deer-flow`、`everything-claude-code`、`github-mcp-server` 同期高频，闭环逐步清晰。  
- **跨客户端桥接需求明显上升**：`9router`、`agent-deck` 等出现，说明团队希望在 Claude/Codex/Cursor/Cline 之间复用同一套能力层。  
- **终端与本地开发体验被重新定义**：`cmux`、`claude-howto`、`CopilotForXcode` 等显示“Agent 时代的 IDE/终端融合”正在加速。

### 2026-04（W15-W18）

- **官方与社区双轨并进**：`vercel-labs/agent-skills`、`ComposioHQ/awesome-claude-plugins` 与社区大集合并行，生态分层更清晰。  
- **技能库开始垂直化**：`marketingskills`、`pentest-copilot` 等垂直技能仓出现，说明“通用技能”向“场景技能”演化。  
- **Codex 生态开始抬头**：`awesome-codex-skills` 出现，显示多模型/多客户端技能迁移需求在增强。

### 2026-05（W19-W20）

- **本地推理与代理桥接继续走强**：`Rapid-MLX`、`CloakBrowser`、`UI-TARS` 等反映“本地执行能力 + 代理自动化”持续升温。  
- **Skills 进入平台化竞争阶段**：`huggingface/skills`、`browserbase/skills`、`Auto-claude-code-research-in-sleep` 同期活跃，技能分发与复用成为核心。  
- **AI-Coding 基础设施向“网关化”演进**：`AIClient2API`、`9router` 等持续出现，说明团队越来越重视统一路由、成本与可用性控制。

## 五、最小维护流程（5 分钟）

1. 打开 GitHub Weekly Trending，筛出 AI / AI-Coding 相关候选
2. 对照归档仓库确认是否“短期噪声”还是“连续升温”
3. 落表（周次、仓库名、链接、一句话观察）
4. 若对你项目有直接价值，加入 `docs/11-持续更新.md` 的“月度观察”
