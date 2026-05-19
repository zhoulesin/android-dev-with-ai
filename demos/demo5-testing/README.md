# Demo 5：测试 —— AI 生成测试用例

> 对应文章：[第 7 章 §7.4 测试阶段](../docs/07-接入项目全流程.md)

## 演示目标

展示如何将业务代码交给 AI，让它按 CLAUDE.md 中的测试规范自动生成测试用例。

## 输入（BEFORE）

| 文件 | 说明 |
|------|------|
| `PROMPT.md` | Prompt：指定测试范围和规范 |

## 产出（AFTER）

- `FakeArticleRepository.kt` — Fake 实现（MockK 替代方案）
- `UseCaseTests.kt` — 3 个 UseCase + 4 个测试用例

## 验证要点

- [x] 使用 `runTest` 替代 `Thread.sleep()`
- [x] 使用 Turbine 验证 Flow 发射
- [x] Fake Repository 覆盖 CRUD + 收藏切换
- [x] 测试覆盖：正常路径 + 边界条件（未知 id）

## 关键实践

1. **Fake 优于 Mock**：Fake Repository 比 MockK 更直观、可复用
2. **Turbine 测试 Flow**：`flow.test { awaitItem() }` 模式清晰
3. **AI 生成测试效率极高**：给定 spec，AI 可生成 90% 测试代码
4. **边界测试需人工补充**：异常场景、空数据等需手动添加
