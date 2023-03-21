package com.example.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.BaseContext;
import com.example.backend.common.RestResult;
import com.example.backend.domain.User;
import com.example.backend.domain.Video;
import com.example.backend.domain.VideoCategory;
import com.example.backend.dto.VideoDto;
import com.example.backend.service.UserService;
import com.example.backend.service.VideoCategoryService;
import com.example.backend.service.VideoService;
import com.example.backend.utils.SMSUtils;
import com.example.backend.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Xinran
 * @version 1.0
 * @description 用户登录
 * @date 2023/3/4 17:22
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private UserService userService;

    @Autowired
    private VideoCategoryService videoCategoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发送手机短信验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public RestResult<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();

        if(StringUtils.isNotEmpty(phone)){
            //生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);

            //调用阿里云提供的短信服务API完成发送短信
            SMSUtils.sendMessage("申请的签名","模板",phone,code);
//
//            //需要将生成的验证码保存到Session，用来和用户填的验证码进行校验
//            session.setAttribute(phone,code);

            //将生成的验证码缓存到Redis中，并且设置有效期为5分钟
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);

            return RestResult.success("手机验证码短信发送成功","成功");
        }

        return RestResult.error("短信发送失败",0);
    }

    /**
     * 移动端用户登录
     * @param map
     * @param session
     * @return
     */
    //这个是当我们点击登录按钮做的操作
    //map封装了前端的数据，分别是两个键值对
    @PostMapping("/login")
    public RestResult<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());

        //获取手机号（用户输的手机号）
        //phone和code就是key
        String phone = map.get("phone").toString();

        //获取验证码（用户输的验证码）
        String code = map.get("code").toString();

//        //从Session中获取保存的验证码（我们发给用户的验证码）
//        Object codeInSession = session.getAttribute(phone);

        //从Redis中获取缓存的验证码
        Object codeInRedis = redisTemplate.opsForValue().get(phone);

        //进行验证码的比对（页面提交的验证码和Session中保存的验证码比对）
        if(codeInRedis != null && codeInRedis.equals(code)){
            //如果能够比对成功，说明登录成功

            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);

            User user = userService.getOne(queryWrapper);
            //如果从数据库没查到user那么我们就自动为用户创建一个
            if(user == null){
                //登录注册合二为一
                //判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            //如果用户登录成功，删除Redis中缓存的验证码
            redisTemplate.delete(phone);

            return RestResult.success(user,"成功");
        }
        return RestResult.error("登录失败",0);
    }

    /**
    * @description 用户上传的视频状态分页查询（我的上传）
    * @param page
     * @param pageSize
    * @return
    * @author
    * @date
    */
    @GetMapping("/page")
    //前端注意这里查出来的数据重点把status可视化一下1代表审核成功，0代表审核失败，2代表审核中
    public RestResult<Page> UploadList(int page, int pageSize) {

        //构造分页构造器对象
        Page<Video> pageInfo = new Page<>(page, pageSize);
        Page<VideoDto> videoDtoPage = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件

        //这里我们就是根据视频上传者查询这个用户上传的视频
        //只有这一句话和其他分页不一样
        queryWrapper.eq(Video::getCreateUser, BaseContext.getCurrentId());
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
}
