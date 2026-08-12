package spring.ai.example.spring_ai_demo.config;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import spring.ai.example.spring_ai_demo.tools.FileTools;


/**
 * @author duoyian
 * @since 2026/8/12
 */
@Component
public class AiConfig {

    @Resource
    private FileTools fileTools;

    @Bean
    public ChatClient chatClient(OpenAiChatModel model) {

        return ChatClient.builder(model)
                .defaultTools(fileTools)
                .build();
    }
}
