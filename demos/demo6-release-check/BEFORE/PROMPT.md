# Demo 6 Prompt：发布前检查

你是一位 Release Manager，需要对待发布的应用做发包前检查。

## 输入
- demo1～demo5 全部产出
- `templates/release-checklist.md`（仓库根目录）
- `CLAUDE.md`

## 任务

1. **对照 release-checklist 逐项检查**
   - Gradle / 依赖：版本号统一、api/implementation、编译通过、无 Duplicate class
   - Manifest：合并检查、Debug 配置隔离
   - AI 参与变更：build.gradle 人工已审、sealed when 穷尽检查、无密钥泄露

2. **生成 PR 描述**（包含 checklist markdown）

3. **生成 CI 配置建议**（ktlint + assembleDebug + 密钥扫描）

4. **输出检查结果**：通过项 / 需改进项 / 阻塞项

## 输入
- `../templates/release-checklist.md`
