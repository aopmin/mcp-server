package cn.aopmin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class McpServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
        log.info("MCP Server 启动成功===>");
        log.info("验证MCP服务暴露的SSE端点：curl http://localhost:23990/sse");
    }
}
