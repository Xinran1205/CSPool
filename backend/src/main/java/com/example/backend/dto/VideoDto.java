package com.example.backend.dto;

import com.example.backend.domain.Video;
import lombok.Data;

/**
 * @author Xinran
 * @version 1.0
 * @description 数据传输对象
 * @date 2023/3/4 14:30
 */
//为了拓展实体类的属性以用来关联其他实体类，也可以响应前端发的请求数据
@Data
public class VideoDto extends Video {
//    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
