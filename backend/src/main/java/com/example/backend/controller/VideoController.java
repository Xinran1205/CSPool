package com.example.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.RestResult;
import com.example.backend.domain.Video;
import com.example.backend.domain.VideoCategory;
import com.example.backend.dto.VideoDto;
import com.example.backend.service.VideoCategoryService;
import com.example.backend.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Xinran
 * @version 1.0
 * @description 视频分类
 * @date 2023/3/4 14:30
 */

@RestController
@CrossOrigin
@RequestMapping("/video")
@Slf4j
//我们就分页用到了dto，用来扩展分类的名字
public class VideoController {
    @Autowired
    private VideoService videoService;

    @Autowired
    private VideoCategoryService videoCategoryService;

    /**
     * 管理员新增视频
     *
     * @param video
     * @return
     */
    //因为我们没有其他表，所以数据只添加到视频表
    //@RequestBody用来接收前端json格式对象
    @PostMapping
    public RestResult<String> save(@RequestBody Video video) {
        log.info(video.toString());

        videoService.save(video);

        return RestResult.success("新增视频成功","成功");
    }

    /**
    * @description 用户上传视频
    * @param video
    * @return
    * @author
    * @date
    */
    @PostMapping("/userUpload")
    public RestResult<String> Upload(@RequestBody Video video) {
        log.info(video.toString());
        //用户上传的视频status一定得是2，2代表审核中
        video.setStatus(2);
        videoService.save(video);

        return RestResult.success("上传视频成功，等待审核","成功");
    }

    /**
     * 后台视频信息分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    //这里有个重要的，我们后台视频管理分页展示出来是需要带视频分类的
    @GetMapping("/page")
    public RestResult<Page> page(int page, int pageSize, String name, Long categoryId) {

        //构造分页构造器对象
        Page<Video> pageInfo = new Page<>(page, pageSize);
        Page<VideoDto> videoDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        //没在搜索栏选，没选过滤分类就默认为空
        //那么一开始我们点进去视频管理或用户界面，查询栏它的name是null，然后左上角我们可以在查询栏给name赋值，点击查询按钮再再重新调用整个方法
        queryWrapper.like(name != null, Video::getName, name);
        //那么一开始我们点进去视频管理或用户界面，过滤栏categoryId是null，我们可以选择我们要查的分类，点击查询按钮再再重新调用整个方法
        queryWrapper.eq(categoryId!=null,Video::getCategoryId,categoryId);
        //添加排序条件
        queryWrapper.orderByDesc(Video::getUpdateTime);

        //执行分页查询
        videoService.page(pageInfo, queryWrapper);

        //对象拷贝
        //把pageInfo中的值拷到DtoPage，除了records这个属性其他都拷贝
        //因为records我们下面要重新组装
        //records就是页面每条数据总的List集合
        BeanUtils.copyProperties(pageInfo, videoDtoPage, "records");

        List<Video> records = pageInfo.getRecords();

        //这里就是遍历这个新的集合，重新组装这个集合
        List<VideoDto> list = records.stream().map((item) -> {
            VideoDto videoDto = new VideoDto();

            //这里是把本来原有的属性值从item拷到Dto，就是除了categoryName的属性的值
            BeanUtils.copyProperties(item, videoDto);

            Long categoryId2 = item.getCategoryId();//分类id
            //根据id查询分类对象
            VideoCategory category = videoCategoryService.getById(categoryId2);

            if (category != null) {
                String categoryName = category.getName();
                videoDto.setCategoryName(categoryName);
            }
            return videoDto;
        }).collect(Collectors.toList());

        videoDtoPage.setRecords(list);

        return RestResult.success(videoDtoPage,"成功");
    }

    /***
    * @description 前台视频信息分页查询
    * @param page
     * @param pageSize
     * @param name
     * @param categoryId
    * @return
    * @author
    * @date
    */
    @GetMapping("/frontpage")
    //对于展示给用户的视频页面，我们只展示status为1的也就是审核通过的视频
    public RestResult<Page> UserPage(int page, int pageSize, String name, Long categoryId) {

        Page<Video> pageInfo = new Page<>(page, pageSize);
        Page<VideoDto> videoDtoPage = new Page<>();

        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Video::getName, name);
        queryWrapper.eq(categoryId!=null,Video::getCategoryId,categoryId);

        //这里非常重要，对于用户我们只展示status为1的视频，除了多了这一句其余和上面完全一样
        queryWrapper.eq(Video::getStatus,1);

        queryWrapper.orderByDesc(Video::getUpdateTime);

        videoService.page(pageInfo, queryWrapper);

        //对象拷贝
        //把pageInfo中的值拷到DtoPage，除了records这个属性其他都拷贝
        //因为records我们下面要重新组装
        //records就是页面每条数据总的List集合
        BeanUtils.copyProperties(pageInfo, videoDtoPage, "records");

        List<Video> records = pageInfo.getRecords();

        //这里就是遍历这个新的集合，重新组装这个集合
        List<VideoDto> list = records.stream().map((item) -> {
            VideoDto videoDto = new VideoDto();

            //这里是把本来原有的属性值从item拷到Dto，就是除了categoryName的属性的值
            BeanUtils.copyProperties(item, videoDto);

            Long categoryId2 = item.getCategoryId();//分类id
            //根据id查询分类对象
            VideoCategory category = videoCategoryService.getById(categoryId2);

            if (category != null) {
                String categoryName = category.getName();
                videoDto.setCategoryName(categoryName);
            }
            return videoDto;
        }).collect(Collectors.toList());

        videoDtoPage.setRecords(list);

        return RestResult.success(videoDtoPage,"成功");
    }

    /**
     * 管理员修改视频时根据id查询视频信息
     *
     * @param id
     * @return
     */
    //这个是当我们要修改视频，需要回显的信息，就是点修改视频，它页面本身是有数据的
    //分类回显应该是通过前端绑定id和汉字
    //如果没有回显，可以通过dto，再去分类表中把分类名字查出来吧
    @GetMapping("/{id}")
    public RestResult<Video> get(@PathVariable Long id) {

        Video video = videoService.getById(id);

        return RestResult.success(video,"成功");
    }

    /**
     * 管理员修改视频，同时审核视频，审核视频就是将status改为1
     *
     * @param video
     * @return
     */
    @PutMapping
    public RestResult<String> update(@RequestBody Video video) {
        log.info(video.toString());

        videoService.updateById(video);

        return RestResult.success("修改视频成功","成功");
    }

    /**
     * 管理员删除视频
     *
     * @param video
     * @return
     */
    //这个不确定对不对，应该是对的
    @DeleteMapping
    public RestResult<String> delete(@RequestBody Video video) {
        log.info(video.toString());

        videoService.removeById(video);

        return RestResult.success("删除视频成功","成功");
    }
}
