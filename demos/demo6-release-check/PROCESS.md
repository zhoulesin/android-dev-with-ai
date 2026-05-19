# Demo 6 实操过程

> AI 辅助 Release Checklist 审计

---

## Round 1：AI 直接审计

### Human Prompt

```
对照 release-checklist.md 审计当前项目
```

### AI Response（初版）

```markdown
- [x] 版本号在 libs.versions.toml ✅
- [x] 编译通过 ✅
- [x] 无密钥泄露 ✅
(过于简单，缺少具体证据)
```

### Human Review

| 问题 | 说明 |
|------|------|
| 无证据 | 只说"通过"，没说"怎么验证的" |
| 无分类 | 未按 Gradle/Manifest/AI 分组 |
| 缺 Manifest 检查 | 完全没提 merged manifest |

---

## Round 2：补充要求

### Human Feedback

```
每一项检查都要给证据：
- Gradle 依赖：贴具体命令和结果
- Manifest：检查 merged manifest 是否有 Debug 泄漏
- AI 变更：列改动的文件，逐项说明审查结果
- 输出 PR 模板
```

### AI Response（终版）

AI 补全了分组、证据、PR 模板，详见 `AFTER/release-report.md`。

---

## 总结

AI 在审计类任务的优势和劣势：

| 优势 | 劣势 |
|------|------|
| 不遗漏检查项（按 list 逐条） | 容易"粉饰太平"（默认全部通过） |
| 生成 PR 模板格式精确 | 缺少具体证据 |
| CI 配置语法准确 | 需要人指定 CI 平台 |

**使用建议**：AI 做初版审计 → 人逐条复核并提供证据 → AI 生成最终报告
