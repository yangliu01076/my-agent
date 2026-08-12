package spring.ai.example.spring_ai_demo.config;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import spring.ai.example.spring_ai_demo.tools.ChatTools;
import spring.ai.example.spring_ai_demo.tools.FileTools;

import java.util.List;


/**
 * @author duoyian
 * @since 2026/8/12
 */
@Component
public class AiConfig {

    @Resource
    private FileTools fileTools;

    @Resource
    private List<ChatTools> allTools;

    @Bean
    public ChatClient chatClient(OpenAiChatModel model) {

        return ChatClient.builder(model)
                .defaultTools(toolCallbackProvider())
                .build();
    }

    private ToolCallbackProvider toolCallbackProvider() {
        return MethodToolCallbackProvider.builder()
                .toolObjects(allTools.toArray())
                .build();
    }
}
