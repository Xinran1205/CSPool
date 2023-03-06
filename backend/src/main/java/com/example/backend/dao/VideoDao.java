package com.example.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.domain.Video;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VideoDao extends BaseMapper<Video> {
//    @Select("select * from cspool.video where Name Like \"%\"#{name}\"%\"")
//    public Video getByName(String name);
}
