package com.example.backend.controller;

import com.example.backend.domain.Video;
import com.example.backend.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;

@RestController
@RequestMapping("/video")
public class VideoController {
    @Autowired
    private VideoService videoService;

    @GetMapping("/{Name}")
    public Result getByName(@PathVariable String Name) {
        Video video = videoService.getByName(Name);
        Integer code = video != null ? Code.GET_OK : Code.GET_ERR;
        String msg = video != null ? "" : "数据查询失败，请重试！";
        return new Result(code,video,msg);
    }
}
