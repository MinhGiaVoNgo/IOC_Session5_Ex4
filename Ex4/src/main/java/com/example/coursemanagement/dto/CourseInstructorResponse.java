package com.example.coursemanagement.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CourseInstructorResponse {
    private Long id;
    private String name;
    private String email;
}
