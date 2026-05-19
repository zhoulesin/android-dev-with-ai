# Demo 1：项目启动 —— CLAUDE.md 搭建项目架构

> 对应文章：[第 7 章 §7.0 前置配置](../docs/07-接入项目全流程.md)

## 演示目标

展示如何用一份 **CLAUDE.md（项目宪法）** 让 AI 自动生成符合最佳实践的多模块 Kotlin Compose 项目骨架。

## 输入（BEFORE）

| 文件 | 说明 |
|------|------|
| `CLAUDE.md` | 项目宪法：技术栈、模块结构、命名规范、禁止项 |
| `PROMPT.md` | 给人看的 Prompt：具体任务描述和验收标准 |

## 产出（AFTER）

一个完整的 Android 多模块项目：

```
AFTER/
├── CLAUDE.md                    # AI 放置的项目宪法副本
├── build.gradle.kts             # 根构建（插件声明 + ktlint）
├── settings.gradle.kts          # 模块注册
├── gradle/libs.versions.toml    # 唯一版本源
├── .editorconfig
│
├── app/                         # @HiltAndroidApp + NavHost
├── core/domain/                 # 纯 JVM：模型 + Repository 接口 + UseCase
├── core/data/                   # Room + FakeRemoteDataSource + @Module
├── core/designsystem/           # Material3 主题
│
├── feature/feed/                # 文章列表（Loading/Error/Success 三态）
├── feature/article/             # 文章详情
├── feature/bookmark/            # 收藏管理
└── feature/search/              # 搜索
```

## 验证要点

- [x] `feature:feed/build.gradle.kts` 无 `project(":core:data")` 依赖
- [x] `core:domain/build.gradle.kts` 无 `android-library` 插件
- [x] 所有版本号仅在 `libs.versions.toml`
- [x] `./gradlew :app:assembleDebug` 编译通过

## 关键实践

1. **CLAUDE.md 越详细，AI 生成越准确**：技术栈、模块结构、命名、禁止项全部写明
2. **Version Catalog 单一版本源**：防止 AI 在不同模块散落硬编码版本
3. **依赖门禁前置声明**：`feature:*` 不得依赖 `core:data`，在 CLAUDE.md 中就写清楚
4. **编译通过即验收**：AI 生成的第一个里程碑就是可编译
