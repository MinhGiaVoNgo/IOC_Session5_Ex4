package com.example.coursemanagement.service;

import com.example.coursemanagement.dto.*;
import com.example.coursemanagement.entity.Course;
import com.example.coursemanagement.enums.CourseStatus;
import com.example.coursemanagement.repository.CourseRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * V1: Gây N+1 Query vì truy cập course.getInstructor()
     */
    public PageResponse<CourseResponse> getPagedCoursesByStatus(int page, int size, String sortBy, Sort.Direction direction, CourseStatus status) {
        if (page < 0) page = 0;
        if (sortBy == null || sortBy.trim().isEmpty()) sortBy = "id";

        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Course> coursePage = courseRepository.findAllByStatus(status, pageable);

        Page<CourseResponse> courseResponsePage = coursePage.map(course -> {
            CourseInstructorResponse instructorResponse = null;
            if (course.getInstructor() != null) {
                // Đây là nơi gây ra N+1 Query!
                // Mỗi lần gọi course.getInstructor() sẽ phát sinh 1 query SELECT từ bảng instructors
                instructorResponse = CourseInstructorResponse.builder()
                        .id(course.getInstructor().getId())
                        .name(course.getInstructor().getName())
                        .email(course.getInstructor().getEmail())
                        .build();
            }

            return CourseResponse.builder()
                    .id(course.getId())
                    .title(course.getTitle())
                    .description(course.getDescription())
                    .price(course.getPrice())
                    .status(course.getStatus())
                    .instructor(instructorResponse)
                    .build();
        });

        return PageResponse.<CourseResponse>builder()
                .items(courseResponsePage.getContent())
                .page(courseResponsePage.getNumber())
                .size(courseResponsePage.getSize())
                .totalItems(courseResponsePage.getTotalElements())
                .totalPages(courseResponsePage.getTotalPages())
                .last(courseResponsePage.isLast())
                .build();
    }

    /**
     * V2: Sử dụng DTO Projection → KHÔNG gây N+1 Query
     * Chỉ select id, title, status → không truy cập instructor
     */
    public PageResponse<CourseResponseV2> getPagedCoursesV2(int page, int size, String sortBy, Sort.Direction direction, CourseStatus status) {
        if (page < 0) page = 0;
        if (sortBy == null || sortBy.trim().isEmpty()) sortBy = "id";

        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // Sử dụng DTO Projection trực tiếp từ Repository (có hoặc không có status)
        Page<CourseResponseV2> projectedPage = (status != null)
                ? courseRepository.findAllProjectedByStatus(status, pageable)
                : courseRepository.findAllProjected(pageable);

        return PageResponse.<CourseResponseV2>builder()
                .items(projectedPage.getContent())
                .page(projectedPage.getNumber())
                .size(projectedPage.getSize())
                .totalItems(projectedPage.getTotalElements())
                .totalPages(projectedPage.getTotalPages())
                .last(projectedPage.isLast())
                .build();
    }
}
