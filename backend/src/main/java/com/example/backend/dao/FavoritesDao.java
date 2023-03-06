package com.example.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.domain.Favorites;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Xinran
 * @version 1.0
 * @description TODO
 * @date 2023/3/6 10:37
 */
@Mapper
public interface FavoritesDao extends BaseMapper<Favorites> {
}
