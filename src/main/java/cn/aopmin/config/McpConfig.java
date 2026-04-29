package cn.aopmin.config;

import cn.aopmin.service.McpToolService;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

import java.util.Arrays;
import java.util.List;

/**
 * Spring AI 核心配置类
 *
 * 职责：
 * 1. 创建 ChatClient ，用于与 Ollama 模型交互
 * 2. 自动发现并注册所有通过 @Tool 注解声明的 MCP 工具
 */
@Slf4j
@Configuration
public class McpConfig {

    @Bean
    public RestTemplate restTemplate(@Value("${remote.limit.insecure-ssl:false}") boolean insecureSsl) throws Exception {
        if (!insecureSsl) {
            return new RestTemplate();
        }

        // Dev/UAT only: relax SSL verification when upstream certificate SAN does not match hostname.
        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial(new TrustAllStrategy())
                .build();

        HttpClient httpClient = HttpClients.custom()
                .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
                        .setSSLSocketFactory(SSLConnectionSocketFactoryBuilder.create()
                                .setSslContext(sslContext)
                                .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                                .build())
                        .build())
                .build();

        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    /**
     * 手动注册工具（方式一）
     * 将带有 @Tool 注解的服务类注册为 ToolCallbackProvider
     */
    @Bean
    public ToolCallbackProvider mcpToolProvider(McpToolService mcpToolService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(mcpToolService)
                .build();
    }


    /**
     * 创建 ChatClient 实例，并绑定所有 MCP 工具
     *
     * @param ollamaChatModel        Spring AI 自动配置的 Ollama 聊天模型
     * @param toolCallbackProviders  容器中所有的 ToolCallbackProvider Bean
     * @return 配置好的 ChatClient
     */
    @Bean
    public ChatClient chatClient(OllamaChatModel ollamaChatModel,
                                 List<ToolCallbackProvider> toolCallbackProviders) {

        // 从所有 ToolCallbackProvider 中提取工具回调函数（FunctionCallback[]）
        List<FunctionCallback> functionCallbacks = toolCallbackProviders.stream()
                .flatMap(provider -> Arrays.stream(provider.getToolCallbacks())).toList();

        if (!functionCallbacks.isEmpty()) {
            log.info("✅ 已成功加载 {} 个 MCP 工具:", functionCallbacks.size());
            functionCallbacks.forEach(fc ->
                    log.info("   🔧 {} - {}", fc.getName(), fc.getDescription())
            );
        } else {
            log.warn("⚠️ 未发现任何 @Tool 注解的方法，AI 将无法调用外部工具。");
        }

        // 构建 ChatClient，将工具注册进去（转换为数组）
        return ChatClient.builder(ollamaChatModel)
                .defaultFunctions(functionCallbacks.toArray(new FunctionCallback[0]))
                .build();
    }
}
