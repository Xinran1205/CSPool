package com.example.backend.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Xinran
 * @version 1.0
 * @description 视频收藏夹
 * @date 2023/3/6 10:21
 */
@Data
public class Favorites implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long user_id;

    private Long video_id;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
