# Demo 6：发包检查 —— Release Checklist + CI

> 对应文章：[第 7 章 §7.5 上线回流](../docs/07-接入项目全流程.md)

## 演示目标

展示 AI 如何根据 release-checklist 模板对待发布应用做全量审计，生成 PR 描述和 CI 配置。

## 输入（BEFORE）

| 文件 | 说明 |
|------|------|
| `PROMPT.md` | Prompt：要求 AI 对照 checklist 审计 |

## 产出（AFTER）

- `release-report.md` — 逐项检查结果表 + PR 描述模板
- `ci-workflow.yml` — GitHub Actions 配置（ktlint + assemble + test）

## 验证要点

- [x] Gradle 依赖项全部通过
- [x] Manifest 安全检查通过
- [x] AI 变更项（密钥、Diff、when穷尽）通过
- [x] CI 配置包含静态检查 + 编译 + 单元测试

## 关键实践

1. **Checklist 即审计标准**：AI 逐条对照，不遗漏
2. **AI 可生成 PR 模板**：含完整 checklist markdown
3. **CI 配置自动化**：AI 生成的 workflow 符合标准 CI 实践
4. **人工确认不可替代**：AI 标注结果后，Release 负责人仍需人工复核
