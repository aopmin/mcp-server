package cn.aopmin.config;

import cn.aopmin.service.NumService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 让 MCP Server 知道有哪些工具可用，需要将工具类注册到 ToolCallbackProvider。
 */
@Configuration
public class ToolCallbackProviderRegister {

    @Bean
    public ToolCallbackProvider numTools(NumService numService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(numService) // 注册工具类实例
                .build();
    }
}