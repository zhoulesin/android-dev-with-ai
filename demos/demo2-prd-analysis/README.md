# Demo 2：PRD 解读 —— LLM 全流程解析

> 对应文章：[第 7 章 §7.1 产品阶段](../docs/07-接入项目全流程.md)

## 演示目标

展示将 PRD 交给 AI 后，AI 如何完成**全流程解析**：从用户故事到任务看板，每一个产出都直接指导开发。

## 输入（BEFORE）

| 文件 | 说明 |
|------|------|
| `PRD.md` | 完整 PRD：5 个用户故事 + 4 个功能模块 + 技术约束 |
| `PROMPT.md` | 系统 Prompt：要求 LLM 按结构化模板解析 |

## 产出（AFTER）— 6 份结构化文档

| 文件 | 产出内容 | 对应开发用途 |
|------|---------|-------------|
| `analysis-report.md` | 架构决策、风险评估、模块划分 | Tech Lead 评审 |
| `feature-decomposition.md` | 功能树、MoSCoW 优先级、依赖图、工作量 | Sprint 规划 |
| `data-model.md` | Article 实体、Room Entity、JSON 格式 | data 层开发 |
| `api-design.md` | Repository 接口、DAO 设计、缓存策略 | data 层开发 |
| `ui-component-map.md` | Screen→Component 树、UiState 三态、ViewModel 模板 | feature 层开发 |
| `task-board.md` | 21 项任务、规模、依赖、关键路径 | 逐日开发跟踪 |

## 解析流程

```
PRD.md
  │
  ▼
LLM 解析 ──┬── 提取功能 → feature-decomposition (5个功能模块，MoSCoW)
           ├── 提取数据 → data-model (实体、字段约束、JSON格式)
           ├── 设计接口 → api-design (Repository/DAO/缓存策略)
           ├── 映射UI   → ui-component-map (Screen树、UiState)
           └── 拆解任务 → task-board (21项，含依赖和关键路径)
```

## 验证要点

- [x] 8 个用户故事全部覆盖到具体任务
- [x] 数据模型包含 Room 映射说明
- [x] API 设计含缓存策略决策
- [x] UI 组件映射含三级树形结构
- [x] 任务清单含规模估算和依赖关系

## 关键实践

1. **PRD 结构化是前提**：用户故事格式（US-XX）+ 验收标准，AI 才能准确拆解
2. **一份 PRD → 六份产出**：AI 的多维度解析能力远超人工效率
3. **产出即 Spec**：data-model → 指导 data 层编码，ui-map → 指导 feature 层编码
4. **人审重点**：架构决策（ADR）是否正确、模块边界是否合理
