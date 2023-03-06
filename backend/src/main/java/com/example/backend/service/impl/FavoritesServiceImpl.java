package com.example.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.dao.FavoritesDao;
import com.example.backend.domain.Favorites;
import com.example.backend.service.FavoritesService;
import org.springframework.stereotype.Service;

/**
 * @author Xinran
 * @version 1.0
 * @description TODO
 * @date 2023/3/6 10:40
 */
@Service
public class FavoritesServiceImpl extends ServiceImpl<FavoritesDao, Favorites> implements FavoritesService {
}
