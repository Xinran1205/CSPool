package com.example.backend.controller;

import com.example.backend.service.ChatGPT3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author Xinran
 * @version 1.0
 * @description TODO
 * @date 2023/3/22 22:12
 */
@RestController
@RequestMapping("/api/chat")
public class ChatgptController {

    @Autowired
    private ChatGPT3Service chatGPT3Service;

    @GetMapping
    public String getResponse(@RequestParam String prompt) throws IOException {
        return chatGPT3Service.getResponse(prompt);
    }
}