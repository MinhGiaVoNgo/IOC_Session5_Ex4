package com.example.coursemanagement.dto;

import com.example.coursemanagement.enums.CourseStatus;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CourseResponse {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private CourseStatus status;
    private CourseInstructorResponse instructor;
}
