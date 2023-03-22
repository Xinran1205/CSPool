package com.example.backend.service;

import java.io.IOException;

/**
 * @author Xinran
 * @version 1.0
 * @description TODO
 * @date 2023/3/22 22:13
 */
public interface ChatGPT3Service {
    public String getResponse(String prompt) throws IOException;
}
