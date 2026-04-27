# 前端代码梳理

## 概述

前端是一个单页面的聊天界面，使用纯 HTML、CSS 和 JavaScript 实现，无需任何前端框架。该界面通过 REST API 与后端 Spring Boot 应用通信，实现 AI 智能助手的对话功能。

## 文件位置

```
src/main/resources/static/index.html
```

## 技术栈

- **HTML5**: 页面结构
- **CSS3**: 样式设计（包含渐变、动画、响应式布局）
- **JavaScript (ES6+)**: 交互逻辑
- **Fetch API**: 与后端通信

## 页面结构

### 1. 头部区域 (`.chat-header`)

```html
<div class="chat-header">
    <h1>🤖 AI 智能助手</h1>
    <div class="status-bar">
        <span class="status-indicator" id="status-indicator"></span>
        <span id="status-text">已就绪</span>
    </div>
</div>
```

**功能**：
- 显示标题和 AI 助手图标
- 显示连接状态指示器（绿色/橙色/红色）
- 实时更新状态文本

### 2. 聊天消息区域 (`.chat-messages`)

```html
<div class="chat-messages" id="chat-messages">
    <div class="message system">
        <div class="message-content">
            你好！我是 AI 智能助手...
        </div>
    </div>
</div>
```

**功能**：
- 显示所有聊天消息
- 支持三种消息类型：用户、AI 助手、系统
- 自动滚动到底部
- 支持简单 Markdown 样式（粗体、换行）

### 3. 输入区域 (`.chat-input-container`)

```html
<div class="chat-input-container">
    <textarea id="message-input" placeholder="输入你的问题..."></textarea>
    <div class="button-group">
        <button id="send-btn" class="btn-primary">发送 ✨</button>
        <button id="clear-btn" class="btn-secondary">清空对话</button>
    </div>
</div>
```

**功能**：
- 文本输入框（支持多行输入）
- 发送按钮（主按钮）
- 清空对话按钮（次要按钮）
- 快捷键支持（Enter 发送，Shift+Enter 换行）

## 样式设计

### 颜色主题

- **主色调**：渐变紫色 (#667eea → #764ba2)
- **头部渐变**：绿色到蓝色 (#4CAF50 → #2196F3)
- **用户消息**：紫色渐变背景，白色文字
- **AI 消息**：白色背景，深色文字
- **系统消息**：浅蓝色背景

### 动画效果

1. **状态指示器脉冲动画**：
   ```css
   @keyframes pulse {
       0%, 100% { opacity: 1; }
       50% { opacity: 0.4; }
   }
   ```

2. **打字指示器动画**：
   ```css
   @keyframes typing {
       0%, 80%, 100% { transform: scale(0); }
       40% { transform: scale(1); }
   }
   ```

3. **按钮悬停效果**：
   - 上移 2px
   - 添加阴影

## JavaScript 功能

### 1. API 配置

```javascript
const API_URL = '/api/chat';
```

前端通过 POST 请求与后端 `/api/chat` 接口通信。

### 2. DOM 元素获取

```javascript
const messagesContainer = document.getElementById('chat-messages');
const messageInput = document.getElementById('message-input');
const sendButton = document.getElementById('send-btn');
const clearButton = document.getElementById('clear-btn');
const statusIndicator = document.getElementById('status-indicator');
const statusText = document.getElementById('status-text');
```

### 3. 状态管理

```javascript
let isWaitingForResponse = false;
```

使用布尔值跟踪是否正在等待后端响应，防止重复发送。

### 4. 核心函数

#### `updateStatus(status, text)`

更新状态指示器和状态文本。

**参数**：
- `status`: 'thinking' | 'error' | 'ready'
- `text`: 状态描述文本

#### `addMessage(content, type)`

添加消息到聊天区域。

**参数**：
- `content`: 消息内容
- `type`: 'user' | 'assistant' | 'system'

**功能**：
- 支持简单 Markdown 样式（`**粗体**` → `<strong>粗体</strong>`）
- 自动换行处理（`\n` → `<br>`）
- 自动滚动到底部

#### `addLoadingMessage()` / `removeLoadingMessage()`

显示/隐藏加载动画（打字指示器）。

#### `sendMessage()`

核心发送逻辑：

1. 获取输入框内容并清空
2. 添加用户消息到聊天区域
3. 设置等待状态，禁用发送按钮
4. 显示加载动画
5. 发送 POST 请求到 `/api/chat`
6. 处理响应：
   - 成功：添加 AI 回复，更新状态
   - 失败：显示错误信息，3秒后恢复状态
7. 恢复发送按钮状态

#### `clearChat()`

清空聊天记录（保留系统消息）。

### 5. 事件绑定

```javascript
// 发送按钮点击
sendButton.addEventListener('click', sendMessage);

// 清空按钮点击
clearButton.addEventListener('click', clearChat);

// 键盘事件（Enter 发送，Shift+Enter 换行）
messageInput.addEventListener('keydown', (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault();
        sendMessage();
    }
});
```

## 通信流程

```
用户输入 → 点击发送/按 Enter
    ↓
前端发送 POST 请求到 /api/chat
    ↓
后端处理请求 → 调用 AI 模型 → 执行工具
    ↓
后端返回 JSON 响应 { reply: "..." }
    ↓
前端显示 AI 回复
```

## 响应格式

### 请求

```json
{
    "message": "查询订单ORD-001的状态"
}
```

### 响应

```json
{
    "reply": "订单 ORD-001 的状态是：已发货..."
}
```

## 功能特性

1. **实时状态显示**：连接状态、AI 思考状态、错误状态
2. **加载动画**：发送请求时显示打字指示器
3. **消息样式**：区分用户、AI、系统消息
4. **快捷键支持**：Enter 发送，Shift+Enter 换行
5. **自动滚动**：新消息自动滚动到可见区域
6. **错误处理**：网络错误时显示友好提示
7. **对话清空**：一键清空聊天记录

## 使用示例

### 1. 查询订单

```
输入：查询订单ORD-001的状态
AI回复：订单 ORD-001 的状态是：已发货...
```

### 2. 获取时间

```
输入：现在几点？
AI回复：当前服务器时间是：2026-04-27 10:30:00
```

### 3. 计算器

```
输入：计算 123 + 456
AI回复：123 + 456 = 579
```

## 浏览器兼容性

- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+

## 部署说明

前端文件位于 `src/main/resources/static/` 目录下，Spring Boot 会自动提供静态资源服务。

访问地址：`http://localhost:8086/`

## 扩展建议

如需扩展前端功能，可以考虑：

1. **添加 Markdown 渲染**：使用 marked.js 库支持完整 Markdown
2. **消息历史**：保存聊天记录到 localStorage
3. **文件上传**：支持上传图片、文档等文件
4. **语音输入**：使用 Web Speech API 实现语音输入
5. **主题切换**：支持深色/浅色主题
6. **响应式优化**：适配移动端设备
