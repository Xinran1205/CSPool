package com.example.backend;

import com.example.backend.domain.Video;
import com.example.backend.service.VideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    private VideoService videoService;

    @Test
    public void testGetByName(){
        Video video = videoService.getByName("吴恩达斯坦福机器学习");
        System.out.println(video);
    }
}
