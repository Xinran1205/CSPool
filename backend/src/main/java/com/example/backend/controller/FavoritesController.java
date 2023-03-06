package com.example.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.BaseContext;
import com.example.backend.common.RestResult;
import com.example.backend.domain.Favorites;
import com.example.backend.domain.Video;
import com.example.backend.domain.VideoCategory;
import com.example.backend.dto.VideoDto;
import com.example.backend.service.FavoritesService;
import com.example.backend.service.VideoCategoryService;
import com.example.backend.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Xinran
 * @version 1.0
 * @description 关于收藏夹的操作
 * @date 2023/3/6 10:30
 */
@RestController
@CrossOrigin
@RequestMapping("/favorites")
@Slf4j
public class FavoritesController {
    @Autowired
    private FavoritesService favoritesService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private VideoCategoryService videoCategoryService;

    /**
     * 添加视频到用户收藏夹
     *
     * @param video
     * @return
     */
    //前端每个视频后面的收藏按钮应该数据都得提前封装好
    //思考当前登陆的用户的id怎么传进来
    @PostMapping
    public RestResult<String> saveFavorites(ServletRequest servletRequest,@RequestBody Video video) {
        log.info(video.toString());
        //如果用户未登录则不能收藏
        // 两种方法获取用户id都可以
        //方法1.BaseContext.getCurrentId中获取用户id，
        //方法2.request.getSession().getAttribute("user"),因为看用户login的代码，他用了session.setAttribute("user",user.getId());
        //HttpServletRequest request = (HttpServletRequest) servletRequest;
//        if(request.getSession().getAttribute("user")==null){
//            return RestResult.error("请先登录再收藏",0);
//        }
        if(BaseContext.getCurrentId()==null||BaseContext.getCurrentId()==0){
            return RestResult.error("请先登录再收藏",0);
        }
        //先判定，如果用户收藏的视频已经被用户收藏那么不能收藏
        LambdaQueryWrapper<Favorites> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Favorites::getUser_id,BaseContext.getCurrentId());
        queryWrapper.eq(Favorites::getVideo_id,video.getId());
        if(favoritesService.getOne(queryWrapper)!=null){
            return RestResult.error("视频已收藏",0);
        }

        //我不是很确定这里id是不是为空的时候，下面save方法是自动雪花算法生成id的
        Favorites favorites = new Favorites();
        favorites.setUser_id(BaseContext.getCurrentId());
        favorites.setVideo_id(video.getId());
        favoritesService.save(favorites);

        return RestResult.success("视频成功添加到收藏夹","成功");
    }



    /***
    * @description 展示收藏夹
    * @param servletRequest
     * @param page
     * @param pageSize
    * @return
    * @author
    * @date
    */
    @GetMapping("/page")
    public RestResult<Page> page(ServletRequest servletRequest, int page, int pageSize) {

//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        if(request.getSession().getAttribute("user")==null){
//            return RestResult.error("请先登录再查看收藏",0);
//        }
        if(BaseContext.getCurrentId()==null||BaseContext.getCurrentId()==0){
            return RestResult.error("请先登录再查看收藏",0);
        }
        LambdaQueryWrapper<Favorites> queryWrapperForFav = new LambdaQueryWrapper<>();
        queryWrapperForFav.eq(Favorites::getUser_id,BaseContext.getCurrentId());

        //从收藏表查出这个用户的所有数据
        List<Favorites> favoritesList = favoritesService.list(queryWrapperForFav);

        //取出这个用户的数据中的视频id，封装成字符串或者list
        //这里不确定写的对不对！！！！！！！！！！！！！！！！！！！！！！！
//        String a = "";
//        for (int i=0;i<favoritesList.size();i++){
//            a = ""+favoritesList.get(i).getVideo_id()+",";
//        }
        List<Long> b = new ArrayList<>();
        for (int i=0;i<favoritesList.size();i++){
            b.add(favoritesList.get(i).getVideo_id());
        }

        //构造分页构造器对象
        Page<Video> pageInfo = new Page<>(page, pageSize);
        Page<VideoDto> videoDtoPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件

        //查出的视频得在上面从收藏夹查出的list或string中
        //select * from video where s.id in (a);
        //这里写的不知道对不对啊！！！！！！！！！！！！！！！！！！！！！！！！
        //queryWrapper.inSql(video -> video.getId(),a);
        //不确定用in还是insql
        queryWrapper.in(Video::getId,b);

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
    * @description 删除收藏夹中的视频
    * @param video
    * @return
    * @author
    * @date
    */
    //不确定对不对
    @DeleteMapping
    public RestResult<String> delete(ServletRequest servletRequest,@RequestBody Video video) {
        log.info(video.toString());
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        LambdaQueryWrapper<Favorites> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Favorites::getUser_id,request.getSession().getAttribute("user"));
        queryWrapper.eq(Favorites::getVideo_id,video.getId());
        favoritesService.remove(queryWrapper);

        return RestResult.success("删除视频成功","成功");
    }
}
