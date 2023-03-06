package com.example.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.domain.VideoCategory;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Xinran
 * @version 1.0
 * @description TODO
 * @date 2023/3/3 15:50
 */
@Mapper
public interface VideoCategoryDao extends BaseMapper<VideoCategory> {
}
