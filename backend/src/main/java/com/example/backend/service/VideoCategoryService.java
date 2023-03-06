package com.example.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.domain.VideoCategory;

/**
 * @author Xinran
 * @version 1.0
 * @description TODO
 * @date 2023/3/3 15:53
 */
public interface VideoCategoryService extends IService<VideoCategory> {
    public void remove(Long id);
}
