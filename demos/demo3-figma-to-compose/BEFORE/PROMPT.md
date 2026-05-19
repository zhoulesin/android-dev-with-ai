# Demo 3 Prompt：将设计稿转为 Compose 布局

你是一位 Android UI 开发工程师，需要根据设计稿描述还原为 Jetpack Compose 代码。

## 设计稿描述

### 页面：文章列表首页（FeedScreen）

**顶部导航栏**
- 标题："BestPractice"，左对齐
- 右侧两个图标按钮：搜索（Search 图标）、收藏（Bookmark 图标）

**文章卡片**
- 白色圆角卡片（Card），间距 12dp，内边距 16dp
- 标题：粗体 18sp，最多 2 行，尾部省略号
- 右侧收藏按钮：已收藏显示实心 bookmark 图标，未收藏显示空心
- 摘要：灰色文字 14sp，最多 2 行，省略号
- 底部：作者（12sp） + 空格 + 日期（12sp 灰色）

**加载态**
- 页面居中显示圆形 loading 进度条

**错误态**
- 页面居中显示 "加载失败: xxx" 文字

**空态**
- 列表为空时的占位（本阶段可忽略）

## 技术要求
- 使用 Material3 组件
- 列表使用 LazyColumn + `items(key = { it.id })`
- ViewModel 通过 hiltViewModel() 注入
- UiState 通过 collectAsStateWithLifecycle() 订阅
- 收藏点击调用 `viewModel.toggleBookmark(id)`
- 文章点击回调 `onArticleClick(id)` 给外部

## 输入
- 项目已有 core:domain 中的 Article 数据类
- 项目已有 FeedViewModel（待对接）

## 产出
- 完整的 `FeedScreen.kt` Composable 代码
