package com.example.backend.dao;

import com.example.backend.domain.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface VideoDao {
    @Select("select * from cspool.video where Name = #{name}")
    public Video getByName(String name);
}
