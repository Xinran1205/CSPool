package com.example.CSPool.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class video {
    private Integer id;
    private String Name;
    private String Link;
    private String Comment;
}
