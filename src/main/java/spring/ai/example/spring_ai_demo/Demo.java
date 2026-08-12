package spring.ai.example.spring_ai_demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author duoyian
 * @since 2026/8/11
 */
@Component
public class Demo {
    private final ChatClient chatClient;

    // 构造器注入 OpenAiChatModel
    public Demo(OpenAiChatModel chatModel) {
        // ChatClient 是 Spring AI 推荐的高级流式 API
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    @GetMapping("/ai/chat")
    public String chat(@RequestParam String message) {
        // 这就是最简单的调用方式
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    // 如果你想像 Gradio 那样支持流式输出
    @GetMapping("/ai/stream")
    public String stream(@RequestParam String message) {
        StringBuilder fullResponse = new StringBuilder();

//        chatClient.prompt()
//                .user(message)
//                .stream() // 开启流式
//                .content() // 订阅流
//                .forEach(fullResponse::append);

        return fullResponse.toString();
    }
}
