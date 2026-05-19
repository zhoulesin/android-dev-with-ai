# Project: BestPracticeApp

> 本文件为 AI 每次对话自动加载的「项目宪法」。下面每条附 **Why**（为何这样写）和 **Bug if violated**（不这样会出什么坑）。

## 技术栈（Technology Stack）

- Language: Kotlin 2.x + KSP（不用 kapt 除非第三方强制）
- UI: Jetpack Compose + Material3
- Architecture: MVVM + Clean（presentation / domain / data）
- DI: Hilt
- Build: Gradle Kotlin DSL + Version Catalog (`gradle/libs.versions.toml`)
- Database: Room + `Flow` 暴露
- Navigation: Navigation Compose（类型安全路由）
- Preferences: DataStore Preferences（不用 SharedPreferences 写新代码）
- Async: Coroutines + Flow
- Testing: JUnit + MockK + Turbine + Compose UI Test
- Data Source: FakeRemoteDataSource（模拟网络 API，从 assets 加载 JSON）

**Why**：统一栈后 AI 不会随机引入 Gson、LiveData、手动单例等「第二套体系」。  
**Bug if violated**：同一 Feature 里混用两套 JSON/状态方案，Review 和重构成本翻倍。

---

## 模块结构（Module Layout）

```
:app
:feature:feed         # 首页文章列表
:feature:article      # 文章详情
:feature:bookmark     # 收藏管理
:feature:search       # 搜索
:core:domain          # UseCase + 领域模型 + Repository 接口（无 Android 依赖）
:core:data            # Repository 实现 + Room + FakeRemoteDataSource
:core:designsystem    # 主题、通用 Composable
```

- `feature:*` → 依赖 `core:domain` + `core:designsystem`，**禁止直接依赖 `core:data`**
- `core:domain` 不得依赖 `data`、`app`、Compose、Android
- `core:data` → 依赖 `core:domain`，实现 domain 接口

**Why**：给 AI 划清「改文章列表」时该打开哪些目录。  
**Bug if violated**：AI 在 `feature/feed` 里直接 `import RoomDatabase`，编译通过但架构被破坏；循环依赖编译失败。

---

## Gradle 与依赖（Gradle Rules）

- 模块间暴露类型用 `api`；仅内部实现用 `implementation`。
- 版本只写在 `libs.versions.toml`，禁止在子模块硬编码版本号。
- 新增依赖后必须能说明「属于哪个 catalog 别名」。

**Why**：AI 常把 `implementation` 和 `api` 搞反，导致下游模块编译找不到符号。  
**Bug if violated**：`feature:feed` 用了 `implementation(project(":core:domain"))` 却在 ViewModel 里暴露 domain 类型 → 编译错误。

---

## Compose（UI Rules）

- Composable 默认 **无状态**：状态由 ViewModel / 调用方传入。
- `Modifier` 顺序（由外到内语义）：`size` → `padding` → `background` → `clickable` → `semantics`（ripple 在 clickable 内侧）。
- 不在 Composable 体内：网络请求、数据库、`remember { mutableStateOf(外部可变数据) }` 且无 key。
- 列表必须 `items(..., key = { it.id })`。
- 图片加载：Coil；文本用 `stringResource(R.string.xxx)`。

**Why**：Modifier 顺序错误是 AI 最高频翻车点；无 key 的 LazyColumn 导致错行、闪烁。  
**Bug if violated**：点击区域错位、UI 显示旧数据。

---

## Hilt（DI Rules）

- ViewModel：`@HiltViewModel` + 构造函数 `@Inject`。
- Repository：接口在 `domain`，`@Singleton` 实现在 `data`。
- 不在 Composable 里 `hiltViewModel()` 之外再手写 `Repository()`。

**Why**：AI 爱生成 `object XxxRepository` 或 `lateinit var`，与 Hilt 图冲突。  
**Bug if violated**：运行时 `Hilt Activity must be attached` 或重复实例、测试无法替换 Fake。

---

## Room（Data Rules）

- Entity / DAO / Database 分包清晰；DAO 返回 `Flow` 或 `suspend`，UI 层不直接拿 DAO。
- 迁移必须写 `Migration`，禁止 `fallbackToDestructiveMigration()`。

**Why**：AI 常图省事开 destructive migration。  
**Bug if violated**：发版后用户本地数据库被清空。

---

## Navigation（Navigation Rules）

- 使用 Navigation Compose 类型安全路由。
- 深层链接参数在 ViewModel 内解析，Composable 只收已解析的 `UiState`。
- 返回栈：单 Activity，全 Compose 屏。

**Why**：字符串 route 拼错编译期不报错。  
**Bug if violated**：运行时导航崩溃。

---

## DataStore（Preferences Rules）

- 使用 `DataStore<Preferences>` + 单一 Repository。
- 读写只在 Repository / UseCase；禁止在 Composable 里直接操作。

**Why**：DataStore 是单例 + 协程安全。  
**Bug if violated**：偏好值互相覆盖。

---

## 命名（Naming）

- Composable / 文件：`FeedScreen.kt` → `FeedScreen()`
- ViewModel：`FeedViewModel`
- UiState：`FeedUiState`；事件：`FeedAction`（sealed interface）
- Repository：`ArticleRepository` / `ArticleRepositoryImpl`

**Why**：统一命名后 AI 的 grep 和「找 Feed 相关」才准确。  
**Bug if violated**：命名混用 → 引用改漏。

---

## 状态与错误（State & Errors）

- ViewModel 对外：`StateFlow<XxxUiState>`；一次性事件用 `Channel`。
- 业务结果：sealed `Success | Error`，禁止裸 `Throwable` 传到 UI。

**Why**：AI 喜欢在 Composable 里 try/catch。  
**Bug if violated**：配置变更后重复弹错。

---

## sealed class / when（Exhaustive）

- 领域事件、UI Action 一律 `sealed interface`。
- `when` 必须穷尽分支；新增子类后编译失败是预期行为。

**Why**：模型常漏 else 或漏新分支，隐藏 Bug 温床。  
**Bug if violated**：新增 Action 未处理 → 点击无反应。

---

## 测试（Testing）

- ViewModel：MockK + `runTest` + Turbine 测 `StateFlow`。
- 主线程：`StandardTestDispatcher`；禁止在测试中 `Thread.sleep`。
- Compose：`createComposeRule()` + `onNodeWithTag`（测试需加 `testTag`）。

---

## Manifest（合并约束）

- 不得为消合并冲突删除第三方 SDK manifest 中的 `permission` / `provider` / `service`
- Debug 与 Release 的 `android:exported` 分 flavor 处理

---

## 禁止项（Hard No）

- 禁止新代码使用 SharedPreferences、AsyncTask、Gson
- 禁止在 `domain` 引用 `android.*`
- 禁止 AI 直接修改 `gradle/libs.versions.toml` 以外文件的依赖版本
- 禁止提交：`local.properties`、`google-services.json`、任何密钥

---

## 与 Skills 的分工

- **本文件（Rules）**：项目是什么、不能做什么。
- **Skills**（`.cursor/skills/` 或 `.claude/skills/`）：遇到某类任务时的检查清单（如 Compose Review、Gradle 排错）。

生成 UI 前可提示 AI：先读本文件，再触发 `android-compose-review` Skill（若已配置）。
