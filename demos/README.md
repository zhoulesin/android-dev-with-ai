# Demos：AI 参与 Android 开发全流程

> 对应文章：[第 7 章 · 接入项目全流程](../docs/07-接入项目全流程.md)

按文章全流程逐步展示：**人给什么 → AI 产出什么 → 人审什么**。6 个 demo 串联一个完整闭环，上一个 AFTER 是下一个的 BEFORE。

## Demo 索引

| # | 文章章节 | 演示内容 | BEFORE | AFTER |
|---|---------|---------|--------|-------|
| 1 | §7.0 前置配置 | CLAUDE.md → AI 生成项目骨架 | `CLAUDE.md` + Prompt | 完整 Gradle 多模块项目 |
| 2 | §7.1 产品阶段 | PRD → AI 拆解 | `PRD.md` + Prompt | 6 份结构化分析文档 |
| 3 | §7.2 设计阶段 | 设计稿 → AI 转 Compose | `design-spec.md` + Prompt | 4 个 Screen Compose 文件 |
| 4 | §7.3 开发阶段 | spec → AI 编码实现 | spec 引用 + Prompt | 20 个源文件（domain/data/feature） |
| 5 | §7.4 测试阶段 | 代码 → AI 写测试 | 代码引用 + Prompt | FakeRepo + 7 个测试用例 |
| 6 | §7.5 上线回流 | checklist → AI 审计 | checklist 引用 + Prompt | 报告 + PR 模板 + CI 配置 |

## 数据流

```
demo1 AFTER ──→ demo2 引用 ──→ demo3 引用 ──→ demo4 AFTER ──→ demo5 AFTER
(项目骨架)     (PRD解析)       (设计稿)        (完整代码)      (测试)
                                                              ↓
                                                         demo6 AFTER
                                                         (发包检查)
```

## 使用方式

### 运行 demo1 项目
```bash
cd demos/demo1-project-startup/AFTER
echo "sdk.dir=$ANDROID_HOME" > local.properties
./gradlew :app:assembleDebug
```

### 复现 AI 工作流
每个 demo 的 `BEFORE/` 目录包含你可以直接复制给 AI 工具的素材：

1. 进入一个 demo 的 `BEFORE/`
2. 将 `PROMPT.md` + 其他文件发给你的 AI 工具
3. 对比 AI 输出与 `AFTER/` 的差异

## 目录结构

```
demos/
├── README.md                    ← 本文件
├── demo1-project-startup/       §7.0 前置配置
│   ├── BEFORE/{CLAUDE.md, PROMPT.md}
│   ├── AFTER/ (Gradle 项目)
│   └── README.md
├── demo2-prd-analysis/          §7.1 产品阶段
│   ├── BEFORE/{PRD.md, PROMPT.md}
│   ├── AFTER/ (6 份分析文档)
│   └── README.md
├── demo3-figma-to-compose/      §7.2 设计阶段
│   ├── BEFORE/{design-spec.md, PROMPT.md}
│   ├── AFTER/ (4 个 Compose 文件)
│   └── README.md
├── demo4-implementation/        §7.3 开发阶段
│   ├── BEFORE/{task-breakdown-ref.md, PROMPT.md}
│   ├── AFTER/ (20 个源文件)
│   └── README.md
├── demo5-testing/               §7.4 测试阶段
│   ├── BEFORE/{PROMPT.md}
│   ├── AFTER/ (FakeRepo + 测试)
│   └── README.md
└── demo6-release-check/         §7.5 上线回流
    ├── BEFORE/{PROMPT.md}
    ├── AFTER/ (报告 + PR模板 + CI)
    └── README.md
```
