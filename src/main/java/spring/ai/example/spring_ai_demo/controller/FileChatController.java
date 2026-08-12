package spring.ai.example.spring_ai_demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.function.Function;

/**
 * @author duoyian
 * @since 2026/8/12
 */
@RestController
@RequestMapping("/api/file/chat")
public class FileChatController {

    private final ChatClient chatClient;

    public FileChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping
    public String chat(@RequestBody Map<String, String> payload) {
        String userMessage = payload.get("message");
        if (userMessage == null) return "请输入消息";

        // 将用户消息发送给 AI，AI 会自动决定是聊天还是调用 FileOperations 中的函数
        return chatClient.prompt()
                .user(userMessage)
                .call()
                .content();
    }
}
