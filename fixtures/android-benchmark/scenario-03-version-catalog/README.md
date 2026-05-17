# 考卷 C：Version Catalog 连锁升级

## 现象

产品要求把 Compose BOM 从 `2024.10.00` 升到 `2025.02.00`（只改 `libs.versions.toml` 一处）。

升级后 `:feature:feed` 编译失败：`Unresolved reference: pullRefresh`（Material3 API 变更 / 依赖未对齐）。

## 验收标准

| 维度 | 通过条件 |
|------|----------|
| 定位 | 指出 BOM 升级后需同步检查哪些模块、是否缺 `material` 或 API 迁移 |
| 修复 | 给出 toml + 必要的 `build.gradle.kts` 或代码迁移（如 `pullRefresh` → 新 API） |
| 流程 | 建议 `./gradlew :feature:feed:compileDebugKotlin` 验证 |

## 常见不及格答案

- 只在 feature 模块硬编码新版本号，破坏 catalog 单一数据源
- 删除 `pullRefresh` 功能而不迁移到新 API
