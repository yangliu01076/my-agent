package spring.ai.example.spring_ai_demo.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;


// 1. 请求参数
record TimeRequest(String format) {}

// 2. 响应结果
record TimeResponse(String time) {}

/**
 * @author duoyian
 * @since 2026/8/11
 */
@Component
public class GetCurrentTimeTool implements Function<TimeRequest, TimeResponse> {

    @Override
    @Tool(description = "获取当前的时间，支持自定义时间格式")
    public TimeResponse apply(TimeRequest request) {
        // 如果 AI 没传格式，默认一个
        String pattern = (request.format() != null && !request.format().isEmpty())
                ? request.format()
                : "yyyy-MM-dd HH:mm:ss";

        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
        System.out.println("---- Agent 调用了工具获取时间 ----");
        return new TimeResponse(currentTime);
    }
}

