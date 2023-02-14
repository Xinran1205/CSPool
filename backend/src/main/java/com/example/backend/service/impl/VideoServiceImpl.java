package com.example.backend.service.impl;

import com.example.backend.dao.VideoDao;
import com.example.backend.domain.Video;
import com.example.backend.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VideoDao videoDao;

    public Video getByName(String name) {
        return videoDao.getByName(name);
    }

}
