package com.example.backend.controller;

import com.example.backend.common.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Xinran
 * @version 1.0
 * @description 文件上传与下载
 * @date 2023/3/3 21:09
 */
@RestController
@RequestMapping("/common")
@Slf4j
//这里说的文件，在我们项目中就是那个视频的图片（封面）
public class CommonController {

    @Value("${cspool.path}")
    private String basePath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    //这里要非常注意，这个参数起名字必须要叫file，要和前端content-disposition中的name一致，当然我们可以在前端改成其他的，但是要保持一致
    public RestResult<String> upload(MultipartFile file){
        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info(file.toString());

        //原始文件名
        String originalFilename = file.getOriginalFilename();//abc.jpg
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
        String fileName = UUID.randomUUID().toString() + suffix;//dfsdfdfd.jpg

        //创建一个目录对象,如果当前目录不存在就创建一个
        File dir = new File(basePath);
        //判断当前目录是否存在
        if(!dir.exists()){
            //目录不存在，需要创建
            dir.mkdirs();
        }

        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //因为我们这个图片存好了以后，我们要返回这个图片的名字，把名字存到数据库表中，这样才能把视频和图片关联起来
        return RestResult.success(fileName,"成功");
    }

    /**
     * 文件下载，这不算下载，他是展示文件
     * @param name
     * @param response
     */
    //这个作用是把文件读到我们的页面上
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            //输出流，通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();

            //这一句是固定的，代表我们返回的是图片文件
            response.setContentType("image/jpeg");

            //从输入流读，读到byte数组里面，然后再写到输出流
            //他这个循环就是边读边写
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}