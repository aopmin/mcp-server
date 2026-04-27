package cn.aopmin.controller;

import cn.aopmin.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ChatController {
    
    private final ChatService chatService;

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        log.info("chat message=> {}", request.message());
        String reply = chatService.chat(request.message());
        return new ChatResponse(reply);
    }
    
    public record ChatRequest(String message) {}
    public record ChatResponse(String reply) {}
}