package com.example.coursemanagement.controller;

import com.example.coursemanagement.dto.*;
import com.example.coursemanagement.enums.CourseStatus;
import com.example.coursemanagement.service.CourseService;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * Endpoint chính đã refactor: sử dụng DTO Projection (CourseResponseV2)
     * Trả về: id, title, status (loại bỏ instructor) -> KHÔNG còn N+1 Query!
     * GET /courses?page=0&size=10&sortBy=title&direction=ASC&status=ACTIVE
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CourseResponseV2>>> getCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @RequestParam(required = false) CourseStatus status
    ) {
        PageResponse<CourseResponseV2> pageResponse = courseService.getPagedCoursesV2(page, size, sortBy, direction, status);

        ApiResponse<PageResponse<CourseResponseV2>> response = ApiResponse.<PageResponse<CourseResponseV2>>builder()
                .success(true)
                .message("Ok")
                .data(pageResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint phụ dùng để tái hiện và so sánh lỗi N+1 Query:
     * Truy cập course.getInstructor() làm phát sinh N query select instructor
     * GET /courses/n-plus-one-demo?page=0&size=10&sortBy=title&direction=ASC&status=ACTIVE
     */
    @GetMapping("/n-plus-one-demo")
    public ResponseEntity<ApiResponse<PageResponse<CourseResponse>>> getCoursesWithNPlusOneDemo(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @RequestParam(defaultValue = "ACTIVE") CourseStatus status
    ) {
        PageResponse<CourseResponse> pageResponse = courseService.getPagedCoursesByStatus(page, size, sortBy, direction, status);

        ApiResponse<PageResponse<CourseResponse>> response = ApiResponse.<PageResponse<CourseResponse>>builder()
                .success(true)
                .message("Ok")
                .data(pageResponse)
                .build();

        return ResponseEntity.ok(response);
    }
}
