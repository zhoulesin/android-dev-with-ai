# Demo 3：Figma 还原 —— 设计稿 → Compose 代码

> 对应文章：[第 7 章 §7.2 设计阶段](../docs/07-接入项目全流程.md)

## 演示目标

展示 AI 如何将**4 个页面的完整设计稿**转化为可运行的 Compose 代码。

## 输入（BEFORE）

| 文件 | 说明 |
|------|------|
| `design-spec.md` | 完整设计稿：设计系统 + 4 个页面（Feed/Article/Bookmark/Search）+ 导航流 |
| `PROMPT.md` | Prompt：要求 AI 还原为 Compose |

## 产出（AFTER）

| 文件 | 对应页面 | 代码量 |
|------|---------|:---:|
| `feature/feed/FeedScreen.kt` | 首页列表 | ~120行 |
| `feature/article/ArticleScreen.kt` | 文章详情 | ~100行 |
| `feature/bookmark/BookmarkScreen.kt` | 收藏列表 | ~90行 |
| `feature/search/SearchScreen.kt` | 搜索页 | ~90行 |
| `core/designsystem/Theme.kt` | 主题定义 | ~20行 |

## LLM 生成质量评估

| 评估项 | 人工评分 | 说明 |
|--------|:---:|------|
| 布局还原度 | 85% | 列表+卡片模式准确；细节间距需微调 |
| Modifier 顺序 | 100% | CLAUDE.md 约束生效 |
| 三态处理 | 95% | Loading/Error/Empty 完整 |
| LazyColumn key | 100% | `items(key = { it.id })` |
| Material3 合规 | 90% | 组件选用正确；暗色模式自动支持 |

## 关键实践

1. **设计稿文字描述即可**：不需要 Figma 导出图，ASCII 布局 + 标注足够 AI 理解
2. **设计系统前置**：颜色/排版/间距在 spec 开头定义，AI 全局一致
3. **CLADUE.md 约束生效**：Modifier 顺序、key 要求等规则自动体现在生成代码中
4. **迭代优化模式**：第一版还原度 ~80%，通过 feedback 调整到 95%+
