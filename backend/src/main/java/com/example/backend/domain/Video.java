package com.example.backend.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Xinran
 * @version 1.0
 * @description 视频
 * @date 2023/3/2 16:00
 */

@Data
public class Video implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;
    //视频分类id
    private Long categoryId;
    //图片
    private String image;
    //顺序
    private Integer sort;

    private String link;

    private String comment;

    private Integer level;

    //新加字段 0审核失败，2审核中，1审核成功，0和-1状态的视频前台是看不了的
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}

