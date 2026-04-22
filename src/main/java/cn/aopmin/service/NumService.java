package cn.aopmin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NumService {

    /**
     * 判断一个数是否为双数（偶数）
     *
     * @param num 待判断的数字
     * @return 判断结果描述
     */
    @Tool(description = "判断是否是双数")
    public String judgeIfOddJava(@ToolParam(description = "待判断的数") Integer num) {
        String msg=num+(num % 2 == 0 ? "是双数" : "不是双数");
        log.info("用户输入:{} , 判断结果: {}", num, msg);
        return msg;
    }
}