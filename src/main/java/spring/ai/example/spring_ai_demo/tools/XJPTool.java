package spring.ai.example.spring_ai_demo.tools;

import jakarta.annotation.Resource;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.function.Function;

record XJPRequest(String question) {}

record XJPResponse(String answer) {}

/**
 * @author duoyian
 * @since 2026/8/11
 */
@Component
public class XJPTool{

    @Resource
    private GetCurrentTimeTool getCurrentTimeTool;

    @Tool(description = "获取习近平的身份信息")
    public XJPResponse test(XJPRequest xjpRequest) {
        System.out.println("---- Agent 调用了习近平工具 ----");
        System.out.println(getCurrentTimeTool);
        return new XJPResponse("习近平是中国国家主席");
    }
}
