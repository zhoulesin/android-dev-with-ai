# demo-app · AI Android 工作流工具箱

本工程是 [`android-dev-with-ai`](../README.md) 的**配套实操项目**：用多模块 Clean 架构演示文档中的 Rules、考卷（fixtures）、发包检查（release checklist），而不是重复造一套与 `templates/` 脱节的内容。

## 设计原则

1. **文档为源**：`templates/`、`fixtures/` 由同步脚本写入 `app` assets，App 只读 + 复制 + 勾选。
2. **MVP 优先**：首版不做真实 AI SDK 统计；`stats` 以「发布检查本地勾选」代替。
3. **教学友好**：模块边界对齐第 3 章场景二（feature 不依赖 data 实现）、考卷 B（api/implementation）。

## 模块结构

```
demo-app/
├── README.md
├── settings.gradle.kts
├── build.gradle.kts
├── gradle.properties
├── gradle/
│   └── libs.versions.toml          # 单一版本源（考卷 C 活教材）
│
├── app/                            # Application、Hilt、NavHost、深链入口
│
├── core/
│   ├── model/                      # 纯 Kotlin：RuleTemplate、BenchmarkScenario、ChecklistItem
│   ├── domain/                     # UseCase（GetXxx、CopyMarkdown、ToggleChecklist）
│   ├── data/                       # Asset / Room / DataStore 实现
│   ├── common/                     # Result、Dispatcher、字符串/时间工具
│   └── designsystem/               # Material3 主题、Scaffold、Markdown 预览组件
│
├── feature/
│   ├── hub/                        # 首页三入口：配置 · 考卷 · 发布
│   ├── playbook/                   # Tab：Rules | Skills | Prompts（配置中心）
│   ├── benchmark/                  # fixtures 考卷列表 → 详情 → 复制 PROMPT
│   └── release/                    # release-checklist 勾选（DataStore 持久化）
│
└── tooling/
    └── sync-content.sh             # ../templates + ../fixtures → app assets
```

## 模块依赖（禁止越界）

```
                    ┌─────────────┐
                    │    :app     │
                    └──────┬──────┘
           ┌───────────────┼───────────────┐
           ▼               ▼               ▼
    ┌────────────┐ ┌────────────┐ ┌────────────┐
    │ feature:hub│ │feature:    │ │feature:    │
    │            │ │ playbook   │ │ benchmark  │
    └─────┬──────┘ └─────┬──────┘ └─────┬──────┘
          │              │              │
          └──────────────┼──────────────┘
                         ▼
              ┌──────────────────────┐
              │ feature:release      │
              └──────────┬───────────┘
                         │
         ┌───────────────┼───────────────┐
         ▼               ▼               ▼
  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐
  │core:domain  │ │core:       │ │core:common  │
  └──────┬──────┘ │designsystem │ └─────────────┘
         │        └─────────────┘
         ▼
  ┌─────────────┐
  │ core:model  │
  └─────────────┘
         ▲
         │ implementation（feature 不得依赖）
  ┌─────────────┐
  │ core:data   │
  └─────────────┘
```

| 规则 | 说明 |
|------|------|
| `feature:*` → `core:domain` + `core:designsystem` + `core:common` | 允许 |
| `feature:*` → `core:data` | **禁止** |
| `feature:*` ↔ `feature:*` | **禁止** |
| `core:domain` → `core:model` only | 无 Android 依赖 |
| `core:data` → `core:domain` + `core:model` | Room / DataStore / assets |

## 与你原方案的对应关系

| 你的设想 | 本方案 | 说明 |
|----------|--------|------|
| `:feature:rules` | `playbook` 内 **Rules** Tab | 合并浏览层，减少四个平行 NavGraph |
| `:feature:skills` | `playbook` 内 **Skills** Tab | 首版展示结构 + 示例，不接 IDE API |
| `:feature:prompts` | `playbook` 内 **Prompts** Tab + `benchmark` | Prompt 模板与考卷 PROMPT 分开展示 |
| `:feature:stats` | `release` + 后续 `stats`（可选） | MVP 用发布检查清单代替假仪表盘 |
| `:core:domain` | `core:model` + `core:domain` | 模型与用例分离，利于单测 |
| — | `feature:hub` | 统一入口，对应文档「全流程导航」 |
| — | `feature:benchmark` | 直接服务第 2、3 章 fixtures |
| — | `tooling/sync-content.sh` | 避免 templates 双份维护 |

## 导航与路由（Compose Navigation）

```
hub (Home)
 ├── playbook/{tab?}     # rules | skills | prompts
 ├── benchmark/{id?}     # scenario-01 … 08
 └── release             # checklist
```

## Assets 布局（sync 后）

```
app/src/main/assets/
├── templates/
│   ├── rules/CLAUDE.md
│   └── release-checklist.md
└── fixtures/
    └── android-benchmark/
        ├── scenario-01-gradle-duplicate-class/
        │   ├── PROMPT.md
        │   └── README.md
        └── …
```

开发前执行：`./tooling/sync-content.sh`

> `app/src/main/assets/` 已加入 `.gitignore`（方案 A：不入库，开发前 sync），保证文档永远以根目录 `templates/`、`fixtures/` 为唯一事实源。

## 内容更新流程

```
根目录改 md → cd demo-app → ./tooling/sync-content.sh → 在 App 内验证
```

1. 修改仓库根目录 `templates/` 或 `fixtures/` 下的文档
2. 进入 `demo-app/`，执行 `./tooling/sync-content.sh`
3. 运行 App，在 playbook / benchmark 对应页面确认内容已更新

## 实施事项

具体任务、验收标准、排期与 MVP 完成定义见 **[`IMPLEMENTATION-CHECKLIST.md`](./IMPLEMENTATION-CHECKLIST.md)**（只列事项，不含实现代码）。

## 技术栈（与 templates/rules 一致）

- Kotlin 2.x · KSP · Compose · Material3 · Navigation Compose
- Hilt · Room（仅 checklist 历史可选）· DataStore
- Gradle KTS · Version Catalog · minSdk 26

## 本地打开

```bash
# 同步文档内容到 assets
./tooling/sync-content.sh

# 在 Android Studio 中 Open → demo-app/
./gradlew :app:assembleDebug
```

> 根目录 `settings.gradle.kts` 已包含全部模块声明；各模块 `build.gradle.kts` 需在 Android Studio 首次 Sync 时按 AGP 版本补全插件块（见各模块内 TODO）。
