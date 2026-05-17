# AI 赋能 Android 开发 · 最佳实践教程

## 概述

面向有经验的 Android 开发者，系统化阐述如何将 AI Coding 工具和流程正确引入 Android 开发，从工具选型、Skills 配置到全流程集成，提供可落地的最佳实践。

## 交付形式

- Markdown 文档 + 配图/截图
- 教程风格：每章包含 本节目标 → 前置知识 → 正文 → 动手实践 → 踩坑记录
- 图片存放于 `docs/images/`

## 目标读者

已有一定 AI 工具使用经验的 Android 开发者，需要系统化最佳实践和团队落地指南。

## 目录结构

```
android-dev-with-ai/
├── README.md
├── docs/
│   ├── images/
│   ├── 01-背景与趋势.md
│   ├── 02-大模型选型.md
│   ├── 03-Coding工具对比.md
│   ├── 04-效率神器Skills与MCP.md
│   ├── 05-高赞Skills对比.md
│   ├── 06-Skills筛选策略.md
│   ├── 07-接入项目全流程.md
│   ├── 08-常见陷阱与解决方案.md
│   ├── 09-效率与准确率保障.md
│   ├── 10-Token经济学.md
│   └── 11-持续更新.md
```

## 章节设计

### 基础篇（1-4）

**第 1 章 · 背景与趋势**
- GPT + Agent 如何改变开发方式
- 传统流程 vs AI 辅助流程对比
- 2024-2025 AI Coding 生态全景

**第 2 章 · 大模型选型**
- GPT-4o / Claude Opus / Gemini / GLM / DeepSeek 横向对比
- Kotlin、Compose、Gradle 实测表现
- 速度、成本、中文支持评估

**第 3 章 · Coding 工具对比**
- Cursor / Copilot / Trae / Windsurf / Codex CLI 功能矩阵
- Android 项目实测

**第 4 章 · 效率神器**
- Skills 机制、MCP 协议、Rules 文件设计

### 实战篇（5-9，核心，优先撰写）

**第 5 章 · 高赞 Skills 对比**
按功能维度分组：Memory 类、流程管理类、Coding 规范类、Android 专项类
每类 Top 3-5 个，含评分和适用场景

**第 6 章 · Skills 筛选策略**
- 筛选矩阵、功能重叠决策、自定义 Skill 编写

**第 7 章 · 接入项目全流程（最长章节约 4000 字）**
- 产品阶段：PRD → 技术方案
- UI 阶段：Figma → Compose/XML
- 开发阶段：编码、重构、Review
- 测试阶段：单测、UI 测试、集成测试

**第 8 章 · 常见陷阱与解决方案**
- 迭代版本差异、Figma 还原度、上下文溢出、多模块依赖、代码风格

**第 9 章 · 效率与准确率保障**
- Prompt 方法论、Rules 最佳实践、三层防线 Review、度量指标

### 进阶篇（10-11）

**第 10 章 · Token 经济学**
- 消耗实测对比、性价比公式、精益配置策略

**第 11 章 · 持续更新**
- 信息渠道、Rules 演进、知识库建设、季度回顾

## 写作策略

分两阶段：
- Phase 1：先写核心实战篇（5-9 章）
- Phase 2：补齐基础篇（1-4）和进阶篇（10-11）
