package com.example.coursemanagement.dto;

import com.example.coursemanagement.enums.CourseStatus;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CourseResponseV2 {
    private Long id;
    private String title;
    private CourseStatus status;
}
