package cn.aopmin.controller;

import cn.aopmin.service.NumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/num")
public class NumController {

    @Autowired
    private NumService numService;

    @GetMapping("/judge/{num}")
    public String judgeIfOddJava(@PathVariable Integer num) {
        return numService.judgeIfOddJava(num);
    }
}