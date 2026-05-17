# Project: [项目名]

> 本文件为 AI 每次对话自动加载的「项目宪法」。下面每条附 **Why**（为何这样写）和 **Bug if violated**（不这样会出什么坑）。

## 技术栈（Technology Stack）

- Language: Kotlin 2.x + KSP（不用 kapt 除非第三方强制）
- UI: Jetpack Compose + Material3
- Architecture: MVVM + Clean（presentation / domain / data）
- DI: Hilt
- Build: Gradle Kotlin DSL + Version Catalog (`gradle/libs.versions.toml`)
- Network: Retrofit + Moshi
- Database: Room + `Flow` 暴露
- Navigation: Navigation Compose（类型安全路由）
- Preferences: DataStore Preferences（不用 SharedPreferences 写新代码）
- Async: Coroutines + Flow
- Testing: JUnit5 + MockK + Turbine + Compose UI Test

**Why**：统一栈后 AI 不会随机引入 Gson、LiveData、手动单例等「第二套体系」。  
**Bug if violated**：同一 Feature 里混用两套 JSON/状态方案，Review 和重构成本翻倍。

---

## 模块结构（Module Layout）

```
:app
:feature:*          # UI + ViewModel，依赖 domain
:core:domain        # UseCase、领域模型，无 Android 依赖
:core:data          # Repository 实现、Room、Retrofit
:core:designsystem  # 主题、通用 Composable
```

- Feature 不得依赖其他 Feature 的实现模块（可依赖 `feature:api` 若存在）。
- `domain` 不得依赖 `data`、`app`、Compose。

**Why**：给 AI 划清「改登录页」时该打开哪些目录。  
**Bug if violated**：AI 在 `feature/home` 里直接 `import feature.profile.internal.*`，循环依赖或编译失败。

---

## Gradle 与依赖（Gradle Rules）

- 模块间暴露类型用 `api`；仅内部实现用 `implementation`。
- 版本只写在 `libs.versions.toml`，禁止在子模块硬编码版本号（`// 例外: 注释说明`）。
- 新增依赖后必须能说明「属于哪个 catalog 别名」。

**Why**：AI 常把 `implementation` 和 `api` 搞反，导致下游模块编译找不到符号，或把不该传递的依赖泄露出去。  
**Bug if violated**：`feature:cart` 用了 `implementation(project(":core:network"))` 却在公开 API 里暴露 `ApiService` → 下游编译错误 `Cannot access class ...`。

---

## Compose（UI Rules）

- Composable 默认 **无状态**：状态由 ViewModel / 调用方传入。
- `Modifier` 顺序（由外到内语义）：`size` → `padding` → `background` → `clickable` → `semantics`（ripple 在 clickable 内侧）。
- 不在 Composable 体内：网络请求、数据库、`remember { mutableStateOf(外部可变数据) }` 且无 key。
- 列表必须 `items(..., key = { it.id })`。
- 图片加载：Coil；文本用 `stringResource(R.string.xxx)`。

**Why**：Modifier 顺序错误是 2023–2024 年 AI 最高频翻车点；无 key 的 LazyColumn 导致错行、闪烁。  
**Bug if violated**：`Modifier.clickable().padding(16.dp)` → 点击区域只有内容大小，留白点不动；`remember(user.name)` 不随 props 更新 → UI 显示旧数据。

---

## Hilt（DI Rules）

- ViewModel：`@HiltViewModel` + 构造函数 `@Inject`。
- Repository：接口在 `domain`，`@Singleton` 实现在 `data`。
- 不在 Composable 里 `hiltViewModel()` 之外再手写 `Repository()`。

**Why**：AI 爱生成 `object XxxRepository` 或 `lateinit var`，与 Hilt 图冲突。  
**Bug if violated**：运行时 `IllegalStateException: Hilt Activity must be attached` 或重复实例、测试无法替换 Fake。

---

## Room（Data Rules）

- Entity / DAO / Database 分包清晰；DAO 返回 `Flow` 或 `suspend`，UI 层不直接拿 DAO。
- 迁移必须写 `Migration`，禁止 `fallbackToDestructiveMigration()` 除非 `debug` buildType 且注释说明。

**Why**：AI 常图省事开 destructive migration。  
**Bug if violated**：发版后用户本地数据库被清空 → 登录态丢失、线上事故。

---

## Navigation（Navigation Rules）

- 使用类型安全路由（Kotlin serialization 或 Navigation 2.8+ 的 route class）。
- 深层链接参数在 ViewModel 内解析，Composable 只收已解析的 `UiState`。
- 返回栈：单 Activity；Fragment 项目迁移期才允许混合，新屏一律 Compose。

**Why**：字符串 route `"profile/{id}"` 拼错编译期不报错。  
**Bug if violated**：`navigate("proflie/1")` 运行时崩溃；重复 `popBackStack` 逻辑散落多处。

---

## DataStore（Preferences Rules）

- 使用 `DataStore<Preferences>` + 单一 `UserPreferencesRepository`。
- 读写只在 Repository / UseCase；禁止在 Composable 里 `context.dataStore.edit`。

**Why**：DataStore 是单例 + 协程安全，到处 edit 会竞态。  
**Bug if violated**：ANR（主线程误用旧 Preferences API）或偏好值互相覆盖。

---

## 命名（Naming）

- Composable / 文件：`LoginScreen.kt` → `LoginScreen()`
- ViewModel：`LoginViewModel`
- UiState：`LoginUiState`；事件：`LoginAction`（sealed interface）
- Repository：`LoginRepository` / `LoginRepositoryImpl`

**Why**：统一命名后 AI 的 `grep` 和「找 Login 相关」才准确。  
**Bug if violated**：`LoginVM.kt`、`login_view_model.kt` 混用 → 引用改漏。

---

## 状态与错误（State & Errors）

- ViewModel 对外：`StateFlow<LoginUiState>`；一次性事件用 `Channel` 或 `SharedFlow`（如 Snackbar、导航）。
- 业务结果：`Result<T>` 或 sealed `Success | Error | Loading`，禁止裸 `Throwable` 传到 UI。

**Why**：AI 喜欢在 Composable 里 `try/catch` 网络异常。  
**Bug if violated**：配置变更后重复弹错；旋转屏丢事件。

---

## sealed class / when（Exhaustive）

- 领域事件、UI Action、网络结果一律 `sealed interface` / `sealed class`。
- `when` 必须穷尽分支；新增子类后编译失败是预期行为。

**Why**：模型常漏 `else` 或漏新分支，是隐藏 Bug 温床。  
**Bug if violated**：新增 `LoginAction.ShowCaptcha` 未处理 → 点击无反应，且无编译警告（若用了 `else`）。

---

## 测试（Testing）

- ViewModel：MockK + `runTest` + Turbine 测 `StateFlow`。
- 主线程：`StandardTestDispatcher`；禁止在测试中 `Thread.sleep`。
- Compose：`createComposeRule()` + `onNodeWithTag`（测试需加 `testTag`）。

**Why**：AI 生成的测试常缺 `advanceUntilIdle` 或 mock 错 dispatcher。  
**Bug if violated**：测试偶现失败，CI 红绿不定。

---

## Manifest（合并约束）

- 不得为消合并冲突删除第三方 SDK manifest 中的 `permission` / `provider` / `service`
- Debug 与 Release 的 `android:exported` 分 flavor/manifest 处理；禁止在 `main` 为 Release 打开 `exported=true`
- 优先对**冲突属性**使用 `tools:replace`，禁止无故 `tools:node="replace"` 整段替换 `<application>`

**Bug if violated**：Merger failed 反复出现；上架安全扫描失败；SDK 运行时缺组件。

---

## 禁止项（Hard No）

- 禁止 `Log.d` → 用 Timber
- 禁止新代码使用 SharedPreferences、AsyncTask、Gson（若项目已统一 Moshi）
- 禁止在 `domain` 引用 `android.*`（`androidx` 纯 Kotlin 扩展需评审）
- 禁止 AI 直接修改 `gradle/libs.versions.toml` 以外文件的依赖版本（可提议，人审后改）
- 禁止提交：`local.properties`、`google-services.json`、任何密钥

**Bug if violated**：安全扫描不通过；依赖漂移导致 Release 与 Debug 行为不一致。

---

## 与 Skills 的分工

- **本文件（Rules）**：项目是什么、不能做什么。
- **Skills**（`.cursor/skills/` 或 `.claude/skills/`）：遇到某类任务时的检查清单（如 Compose Review、Gradle 排错）。

生成 UI 前可提示 AI：先读本文件，再触发 `android-compose-review` Skill（若已配置）。
