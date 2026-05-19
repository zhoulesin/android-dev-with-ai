# Demo 4 Prompt：根据 spec 完成编码实现

你是一位 Android 开发工程师，需要根据已拆解的任务清单完成编码。

## 上下文
- 项目骨架已就绪（demo1 产出）
- PRD 已拆解为任务清单（demo2 产出 `task-breakdown.md`）
- Feed 页面 Compose 布局已完成（demo3 产出 `FeedScreen.kt`）

## 任务

请完成以下模块的代码实现：

### 1. core:domain（纯 JVM）
- [ ] `Article.kt` — 数据类定义
- [ ] `ArticleRepository.kt` — Repository 接口
- [ ] `GetArticlesUseCase.kt`
- [ ] `GetArticleUseCase.kt`
- [ ] `SearchArticlesUseCase.kt`
- [ ] `ToggleBookmarkUseCase.kt`
- [ ] `ObserveArticlesUseCase.kt`
- [ ] `ObserveBookmarksUseCase.kt`

### 2. core:data（Android Library）
- [ ] `ArticleEntity.kt` — Room Entity
- [ ] `ArticleDao.kt` — 查询（Flow）、更新、插入
- [ ] `ArticleDatabase.kt` — Room Database
- [ ] `FakeRemoteDataSource.kt` — 从 assets 读 JSON，模拟网络延迟
- [ ] `ArticleRepositoryImpl.kt` — 组合 Remote + Local
- [ ] `DataModule.kt` — Hilt @Module（绑定接口 → 实现、提供 UseCase）

### 3. feature 模块
- [ ] `FeedViewModel.kt` — 订阅 ObserveArticles，处理 loading/error
- [ ] `ArticleViewModel.kt` — 通过 SavedStateHandle 获取 articleId，加载详情
- [ ] `BookmarkViewModel.kt` — 订阅 ObserveBookmarks
- [ ] `SearchViewModel.kt` — 输入即搜

### 4. app 集成
- [ ] `MainActivity.kt` — NavHost 注册 4 个路由
- [ ] `AndroidManifest.xml` — Application + Hilt

## 约束（参照 CLAUDE.md）
- feature 模块不直接依赖 core:data
- domain 模块无 Android 依赖
- Room DAO 返回 Flow 或 suspend
- ViewModel 使用 @HiltViewModel + @Inject
- 版本号仅在 libs.versions.toml

## 输入
- `task-breakdown.md`（demo2 产出）
- `FeedScreen.kt`（demo3 产出）
