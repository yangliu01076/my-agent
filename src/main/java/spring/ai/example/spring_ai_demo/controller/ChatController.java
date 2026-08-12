package spring.ai.example.spring_ai_demo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.chat.memory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import spring.ai.example.spring_ai_demo.tools.GetCurrentTimeTool;
import spring.ai.example.spring_ai_demo.tools.XJPTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author duoyian
 * @since 2026/8/11
 */
@RestController
public class ChatController {

    // 使用 ChatClient 而不是直接用 OpenAiChatModel
    private final ChatClient chatClient;

    public ChatController(OpenAiChatModel chatModel, GetCurrentTimeTool timeTool, XJPTool xjpTool) {
        this.chatClient = ChatClient.builder(chatModel)// 1. 设置人设
                .defaultSystem("你是一个智能助手。当用户提到习近平时，请务必使用可用的工具来获取习近平的回答。")
                // 2. 注册工具 - 核心步骤！
                .defaultTools(timeTool, xjpTool)
                .build();
    }

    /**
     * 流式对话接口
     * produces = MediaType.TEXT_EVENT_STREAM_VALUE 是关键，开启了 SSE 协议
     */
    @GetMapping(value = "/ai/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public org.reactivestreams.Publisher<String> chat(
            @RequestParam String message,
            // 接收前端传来的历史记录 JSON 字符串
            @RequestParam(required = false) String history) {

        // 如果你能把 history 解析为 List<Message>，在这里调用 chatClient.prompt().messages(messages)...
        // 为了演示简洁，这里先只做单轮流式输出，你可以结合上一条回答加上历史记录逻辑

        return this.chatClient.prompt()
                .user(message)
                .stream()
                .content(); // 自动把流转换成 String 发送给前端
    }
}
