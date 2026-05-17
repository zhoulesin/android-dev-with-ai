# 全局图示目录

该目录用于存放跨章节的全局图示素材。

## README 导航图（推荐在 GitHub 直接看）

- 主展示方式：在 [`README.md`](../../README.md) 内嵌 **Mermaid**（GitHub 原生渲染，无需额外插件）
- 源文件：[`ai-coding-全流程章节导航图.mmd`](./ai-coding-全流程章节导航图.mmd)

## 为什么不默认用 SVG 做 README 配图？

- GitHub 对 README 中的 SVG 内嵌预览有限制（安全策略），部分环境只能下载不能预览
- 中文 SVG 在部分编辑器/转换链路下容易出现编码问题
- 需要“在 GitHub 上直接可见”时，优先：**Mermaid（文内）** 或 **PNG（图片链接）**

## 可选导出

若需要对外分享静态图，可从 `.mmd` 导出 PNG 后放到本目录，并在 README 中改为引用 `.png`。
