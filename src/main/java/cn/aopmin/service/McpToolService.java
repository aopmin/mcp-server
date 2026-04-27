package cn.aopmin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * MCP 工具服务
 * 通过 @Tool 注解暴露方法，供 AI 模型调用
 * Spring AI 会自动生成 JSON Schema 描述参数格式[reference:8]
 */
@Slf4j
@Service
public class McpToolService {
    
    // 模拟数据存储
    private final Map<String, Map<String, Object>> orders = new HashMap<>();
    
    public McpToolService() {
        // 初始化模拟订单
        Map<String, Object> order1 = new HashMap<>();
        order1.put("orderId", "ORD-001");
        order1.put("status", "已发货");
        order1.put("estimatedDelivery", "2026-05-01");
        order1.put("items", 3);
        orders.put("ORD-001", order1);
        
        Map<String, Object> order2 = new HashMap<>();
        order2.put("orderId", "ORD-002");
        order2.put("status", "处理中");
        order2.put("estimatedDelivery", "2026-05-03");
        order2.put("items", 1);
        orders.put("ORD-002", order2);
    }
    
    /**
     * 获取订单状态
     * @Tool 注解使该方法成为 MCP 工具
     */
    @Tool(description = "根据订单ID查询订单的当前状态和详细信息")
    public Map<String, Object> getOrderStatus(@ToolParam(description = "订单唯一标识符，如 ORD-001") String orderId) {
        log.info("查询订单状态工具被调用=>，orderId: {}", orderId);

        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("订单ID不能为空");
        }
        Map<String, Object> order = orders.get(orderId);
        if (order == null) {
            return Map.of("error", "未找到订单: " + orderId, "orderId", orderId);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("orderId", orderId);
        result.put("status", order.get("status"));
        result.put("estimatedDelivery", order.get("estimatedDelivery"));
        result.put("items", order.get("items"));
        result.put("queryTime", LocalDateTime.now().toString());
        return result;
    }
    
    /**
     * 获取服务器时间
     */
    @Tool(description = "获取当前服务器时间和日期")
    public Map<String, String> getServerTime() {
        log.info("获取服务器时间工具被调用=>");
        return Map.of(
            "time", LocalDateTime.now().toString(),
            "timezone", "Asia/Shanghai"
        );
    }
    
    /**
     * 计算器工具
     */
    @Tool(description = "执行基本的数学运算，支持加法、减法、乘法、除法")
    public Map<String, Object> calculate(
            @ToolParam(description = "第一个数字") double a,
            @ToolParam(description = "运算符，可选值: add, subtract, multiply, divide") String operation,
            @ToolParam(description = "第二个数字") double b) {

        log.info("计算器工具被调用=>，a: {}, operation: {}, b: {}", a, operation, b);
        
        double result;
        switch (operation.toLowerCase()) {
            case "add":
                result = a + b;
                break;
            case "subtract":
                result = a - b;
                break;
            case "multiply":
                result = a * b;
                break;
            case "divide":
                if (b == 0) {
                    return Map.of("error", "除数不能为零");
                }
                result = a / b;
                break;
            default:
                return Map.of("error", "不支持的运算符: " + operation);
        }
        
        return Map.of(
            "expression", a + " " + operation + " " + b,
            "result", result
        );
    }
}