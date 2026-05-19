# Demo 3 实操过程

> 设计稿 → Compose 代码，AI 的 3 次"翻车"与修正

---

## Round 1：描述太口头

### Human Prompt

```
帮我做一个文章列表页面，和一般App差不多就行，卡片样式
```

### AI Response（初版）

```kotlin
@Composable
fun FeedScreen() {
    LazyColumn {
        items(articles) { article ->
            Card {
                Column {
                    Text(article.title)  // ❌ 无样式、无截断
                    Text(article.summary) // ❌ 无颜色区分
                }
            }
        }
    }
}
// ❌ 缺少 TopAppBar、加载态、Modifier 顺序错误
// ❌ 没有 key
// ❌ 没有 bookmark 交互
```

### Human Review

| 问题 | 严重度 |
|------|:---:|
| 无 TopAppBar | 🔴 |
| 无 Loading/Error 态 | 🔴 |
| `items(articles)` 无 key | 🔴 |
| 无收藏按钮 | 🟡 |
| 文字无样式 | 🟡 |

**教训**：口头描述 = 垃圾产出。必须给精确的设计稿。

---

## Round 2：给设计稿，但 AI 跳过细节

### Human Prompt

```
按照下面设计稿实现 FeedScreen：

[贴了 design-spec.md 的 ASCII 布局和标注]

要求：
- Material3
- items(key = { it.id })
- 三态 Loading/Error/Success
- hiltViewModel + collectAsStateWithLifecycle
```

### AI Response

```kotlin
@Composable
fun FeedScreen() {
    Scaffold {
        LazyColumn {
            items(state.articles, key = { it.id }) {  // ✅ key
                Card(Modifier.clickable { ... }) {
                    Column(Modifier.padding(16.dp)) {
                        Text(article.title, maxLines = 2)  // ✅
                        // ❌ 但收藏按钮放在 Card 外面了
                    }
                }
            }
        }
    }
}
// ❌ 缺少错误态处理
// ❌ 收藏按钮位置不符合设计稿（应该在 Card 内部右上角）
// ❌ Modifier.clickable().padding() 顺序会导致点击区域缩小
```

### Human Review

| 检查项 | 结果 | 问题 |
|--------|:---:|------|
| TopAppBar | ✅ | |
| key | ✅ | |
| 三态 | ❌ | 只有 Success，缺 Loading/Error |
| 收藏位置 | ❌ | 放到 Card 外面了 |
| Modifier 顺序 | ❌ | `clickable().padding()` 顺序反了 |
| 间距 | ⚠️ | 12dp 写成了 8dp |

---

## Round 3：精确修正

### Human Feedback

```
问题：
1. Modifier.clickable().padding(16.dp) — padding 在 clickable 后面，点击区域只有内容大小。
   改成 Modifier.padding(16.dp).clickable() 或者 Card 自带 onClick

2. 收藏按钮显示条件写反了，已收藏显示 Bookmark，未收藏显示 BookmarkBorder

3. 加上 Loading 和 Error 状态

4. 收藏 IconButton 放在 Card 内右上角，和标题同一行
```

### AI Response（终版）

AI 按反馈全部修正，代码见 `AFTER/feature/feed/FeedScreen.kt`。

### 验证

| 检查项 | 结果 |
|--------|:---:|
| 布局与设计稿一致 | ✅ |
| Modifier 顺序正确 | ✅ |
| 三态完整 | ✅ |
| 收藏按钮在 Card 内 | ✅ |
| `items(key = { it.id })` | ✅ |
| `collectAsStateWithLifecycle()` | ✅ |

---

## 总结

| 轮次 | AI 翻车 | 根因 | 修正方法 |
|:---:|------|------|---------|
| R1 | 完全不能用 | 需求太模糊 | 给精确设计稿 |
| R2 | 布局细节错 | AI 按"常见做法"而非"设计稿" | 指出具体位置和尺寸 |
| R3 | ✅ 可用 | — | Modifier 顺序是最常见的 AI 翻车点 |

**核心教训**：
1. Modifier 顺序是 AI 最高频翻车点——CLAUDE.md 约束有效但不是 100% 生效
2. AI 倾向"完成功能"而非"还原设计"——需要明确说"按设计稿，不要自由发挥"
3. ASCII 设计稿 + 标注足够 AI 理解，不需要真实 Figma 截图
