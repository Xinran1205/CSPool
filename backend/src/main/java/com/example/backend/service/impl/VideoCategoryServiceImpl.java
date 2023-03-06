package com.example.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.common.CustomException;
import com.example.backend.dao.VideoCategoryDao;
import com.example.backend.domain.Video;
import com.example.backend.domain.VideoCategory;
import com.example.backend.service.VideoCategoryService;
import com.example.backend.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Xinran
 * @version 1.0
 * @description TODO
 * @date 2023/3/3 15:54
 */

@Service
public class VideoCategoryServiceImpl extends ServiceImpl<VideoCategoryDao, VideoCategory> implements VideoCategoryService {

    @Autowired
    private VideoService videoService;

    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param id
     */
    //重写mp的remove方法，自己增加一些附加操作
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Video> videoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        videoLambdaQueryWrapper.eq(Video::getCategoryId,id);
        int count1 = videoService.count(videoLambdaQueryWrapper);

        //查询当前分类是否关联了视频，如果已经关联，抛出一个业务异常
        if(count1 > 0){
            //已经关联视频，抛出一个业务异常
            throw new CustomException("当前分类下关联了视频，不能删除");
        }

//        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
//        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        //添加查询条件，根据分类id进行查询
//        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
//        int count2 = setmealService.count();
//        if(count2 > 0){
//            //已经关联套餐，抛出一个业务异常
//            throw new CustomException("当前分类下关联了套餐，不能删除");
//        }

        //正常删除分类
        super.removeById(id);
    }
}