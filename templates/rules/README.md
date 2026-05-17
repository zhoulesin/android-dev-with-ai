# Android 项目 Rules 模板

复制到项目根目录，按工具重命名即可使用：

| 本仓库文件 | Cursor | Claude Code | Copilot / 通用 |
|-----------|--------|-------------|----------------|
| [`CLAUDE.md`](./CLAUDE.md) | 内容合并进 `.cursor/rules` 或 `AGENTS.md` | 原样 `CLAUDE.md` | `AGENTS.md` 或 `.github/copilot-instructions.md` |

## 使用步骤

1. 复制 `CLAUDE.md` 到项目根目录。
2. 把 `[项目名]`、`[包名]`、模块列表改成你的工程。
3. **只保留与你项目一致的禁止项**（例如仍用 Gson 就删掉「禁止 Gson」）。
4. 控制总行数在 **150 行以内**（见第 4 章「分层管理」）；细节放到 `.cursor/skills/` 或 `references/`。

## 维护

- 模板中的库版本写在「原则」层，具体版本号以项目的 `libs.versions.toml` 为准。
- 建议每季度对照 [第 11 章](../../docs/11-持续更新.md) 复核一次。
