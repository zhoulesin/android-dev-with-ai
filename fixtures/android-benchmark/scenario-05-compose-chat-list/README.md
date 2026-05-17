# 考卷 E：Compose 聊天列表（需求题）

无预置代码。与第 2 章原文一致，用于 UI 广度对比。

## 需求

实现 `ChatScreen`：

- `LazyColumn` 消息列表，左右气泡（自己/对方）
- Coil 加载头像
- 时间戳：今天 `HH:mm`，昨天 `昨天 HH:mm`，更早 `yyyy/MM/dd`
- 新消息到达时滚到底部；**用户手动上滑看历史时不强制拉回**
- `items` 必须带稳定 `key`

## 验收清单

- [ ] 编译通过（给定 BOM + Coil 版本）
- [ ] Modifier 顺序合理（clickable / padding）
- [ ] 状态提升，Composable 无网络/DB 副作用
- [ ] 滚动逻辑区分「新消息」与「用户正在浏览历史」

## Prompt

见 [`PROMPT.md`](./PROMPT.md)。
