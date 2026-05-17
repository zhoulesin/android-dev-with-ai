# Manifest merge 考题（第 8 章 · 坑十一）

用于说明 AI 在 **AndroidManifest 合并** 上的典型错误，不做第 3 章工具打分（高度依赖 flavor / buildType）。

## 现象

Debug 构建在 `app/src/debug/AndroidManifest.xml` 为 `MainActivity` 设置 `android:exported="true"`，与 main manifest 或 library manifest 冲突，合并失败或 Release 误带 debug 配置。

## 验收（人工 Review）

- [ ] 使用 `tools:node="merge"` / `tools:replace` 时**点名替换属性**，而非整文件覆盖
- [ ] 区分 **debug 仅调试** 与 **release 上架** 的 exported 策略
- [ ] 不删除 library 必需的 `permission` / `provider` 节点

## 关联文档

[`docs/08-常见陷阱与解决方案.md`](../../../docs/08-常见陷阱与解决方案.md) · 坑十一
