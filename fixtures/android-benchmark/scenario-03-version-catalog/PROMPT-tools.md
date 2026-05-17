# Prompt（工具横向评测 · 与第 3 章场景五对齐）

同一考题 [`files/`](./files/)，用于对比 Cursor / Copilot / Claude Code 等，而非对比大模型。

```
项目刚把 gradle/libs.versions.toml 的 compose-bom 从 2024.10.00 升到 2025.02.00。
:feature:feed 编译失败，FeedScreen.kt 里 pullRefresh 相关 API 无法解析。

请：
1. 先说明应运行哪条 Gradle 任务验证（模块级 compile）
2. 给出 toml / build.gradle.kts / FeedScreen.kt 的最小修改
3. 说明为何不能在子模块单独硬编码 BOM 版本

【附上 files/ 下全部内容】
```

## 记录字段（建议）

| 工具 | 定位耗时 | 是否坚持 catalog 单一数据源 | 是否给出模块级 compile 验证 | 修复可用 |
