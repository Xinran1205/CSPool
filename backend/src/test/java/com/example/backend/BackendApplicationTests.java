package com.example.backend;

import com.example.backend.service.VideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    private VideoService videoService;

    @Test
    public void testGetByName(){
        System.out.println("1233232");
    }
}
