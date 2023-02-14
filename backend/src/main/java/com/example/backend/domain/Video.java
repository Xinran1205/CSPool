package com.example.backend.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Video {
    private Integer id;
    private String Name;
    private String Link;
    private String Comment;
}
