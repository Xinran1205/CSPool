package com.example.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.RestResult;
import com.example.backend.domain.VideoCategory;
import com.example.backend.service.VideoCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Xinran
 * @version 1.0
 * @description TODO
 * @date 2023/3/3 15:52
 */
@RestController
@CrossOrigin
@RequestMapping("/category")
@Slf4j
public class VideoCategoryController {
    @Autowired
    private VideoCategoryService categoryService;

    /**
     * 新增分类（比如机器学习）
     * @param videocategory
     * @return
     */
    @PostMapping
    public RestResult<String> save(@RequestBody VideoCategory videocategory){
        log.info("category:{}",videocategory);
        categoryService.save(videocategory);
        return RestResult.success("新增分类成功","成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public RestResult<Page> page(int page, int pageSize){
        //分页构造器
        Page<VideoCategory> pageInfo = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<VideoCategory> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件，根据sort进行排序
        queryWrapper.orderByAsc(VideoCategory::getSort);

        //分页查询
        categoryService.page(pageInfo,queryWrapper);
        return RestResult.success(pageInfo,"成功");
    }

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public RestResult<String> delete(Long id){
        log.info("删除分类，id为：{}",id);

//        categoryService.removeById(id);
        categoryService.remove(id);

        return RestResult.success("分类信息删除成功","成功");
    }

    /**
     * 根据id修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public RestResult<String> update(@RequestBody VideoCategory category){
        log.info("修改分类信息：{}",category);

        categoryService.updateById(category);

        return RestResult.success("修改分类信息成功","成功");
    }

    /**
     * 根据条件查询分类数据，这个是视频添加页面中选择分类的下拉菜单
     * @param category
     * @return
     */
    //感觉不用参数，前端发送请求，我直接返回所有的分类
    @GetMapping("/list")
    public RestResult<List<VideoCategory>> list(VideoCategory category){
        //条件构造器
        LambdaQueryWrapper<VideoCategory> queryWrapper = new LambdaQueryWrapper<>();
        //感觉这里不需要条件，因为我们要把所有分类全部查出来
        //queryWrapper.eq(VideoCategory.getName() != null,VideoCategory::getName,category.getName());
        //添加排序条件
        queryWrapper.orderByAsc(VideoCategory::getSort).orderByDesc(VideoCategory::getUpdateTime);

        List<VideoCategory> list = categoryService.list(queryWrapper);
        return RestResult.success(list,"成功");
    }
}