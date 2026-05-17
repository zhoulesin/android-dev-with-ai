# Prompt（坑十一 · Manifest merge）

```
Debug 构建失败：Manifest merger failed，MainActivity 的 android:exported 冲突。
附件含 app main/debug manifest 与 sdk-push library manifest。

请给出：
1. 合并冲突根因（哪两个文件哪条属性）
2. 推荐的 debug / release 分别如何处理 exported
3. 修改后的 XML（使用 tools:replace 或 flavor 拆分，不要删除 SDK 权限）

约束：Release 构建 MainActivity 必须 exported=false；Debug 需 adb 可启动。
```
