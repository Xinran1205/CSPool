package com.example.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.domain.Video;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface VideoService extends IService<Video> {
}
