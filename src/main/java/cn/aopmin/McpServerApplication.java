package cn.aopmin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootApplication
public class McpServerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx =SpringApplication.run(McpServerApplication.class, args);
        log.info("=== MCP Server 启动成功 ===");
        Environment environment=ctx.getBean(Environment.class);
        String port=environment.getProperty("server.port");
        log.info("服务地址: http://localhost:{}", port);
        log.info("测试命令（订单查询）:\ncurl -X POST http://localhost:{}/api/chat \\\n  -H \"Content-Type: application/json\" \\\n  -d '{\"message\":\"查询订单ORD-001的状态\"}'", port);
        log.info("测试命令  (查询时间)  :\ncurl -X POST http://localhost:{}/api/chat \\\n  -H \"Content-Type: application/json\" \\\n  -d '{\"message\":\"现在几点？\"}'", port);
        log.info("测试命令  (计算器)  :\ncurl -X POST http://localhost:{}/api/chat \\\n  -H \"Content-Type: application/json\" \\\n  -d '{\"message\":\"计算 123 + 456\"}'", port);

    }
}