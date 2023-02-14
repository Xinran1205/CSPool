package com.example.backend.service;

import com.example.backend.domain.Video;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface VideoService {
    public Video getByName(String name);
}
