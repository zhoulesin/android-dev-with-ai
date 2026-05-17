# 考卷 B：`api` vs `implementation`

## 现象

`:core:network` 的 **公开 API** 暴露了 `Retrofit` 类型（`UserApi` 接口返回类型依赖 Retrofit 注解），但模块对自己依赖用了 `implementation(io.github.retrofit2:retrofit)`。

`:feature:login` 只 `implementation(project(":core:network"))`，在自家代码里引用 `retrofit2.http.GET` → **编译失败**：Cannot access class Retrofit...

## 验收标准

| 维度 | 通过条件 |
|------|----------|
| 根因 | 指出「泄漏类型到 API 但未用 `api` 暴露依赖」 |
| 修复 | 对 retrofit 使用 `api(...)`，或重构 API 不暴露 Retrofit 类型 |
| 防复发 | 说明何时用 `api`、何时用 `implementation` |

## 常见不及格答案

- 在 `:feature:login` 再写一遍 `implementation(retrofit)` 却不解释（掩盖问题）
- 建议把 `UserApi` 改成 `internal` 导致 feature 无法调用
