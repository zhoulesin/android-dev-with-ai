# 考卷 A：Gradle 多模块 Duplicate class（Room）

## 现象

`:feature:home` 引入第三方 SDK，传递依赖 **Room 2.4.0**；`:core:database` 显式使用 **Room 2.6.1** → 编译报 `Duplicate class androidx.room...`。

## 验收标准（满分 3×10）

| 维度 | 通过条件 |
|------|----------|
| 编译通过 | 给出可执行的 Gradle 修改，逻辑上能消除冲突 |
| 最佳实践 | 除 `exclude` 外，提到 `resolutionStrategy` 或 BOM/catalog 统一版本 |
| 异常覆盖 | 说明注解处理器 / `_Impl` 跨版本不可混用（不能仅「升版本试试」） |

## 常见不及格答案

- 只删 `:core:database` 的 Room 依赖
- 建议 `implementation` 改 `api` 却不改传递依赖
- 手写版本号到子模块而不动 `libs.versions.toml`
