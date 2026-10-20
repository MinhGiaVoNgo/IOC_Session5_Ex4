package com.example.coursemanagement.repository;

import com.example.coursemanagement.dto.CourseResponseV2;
import com.example.coursemanagement.entity.Course;
import com.example.coursemanagement.enums.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // JPQL lọc theo status
    @Query("SELECT c FROM Course c WHERE c.status = :status")
    Page<Course> findAllByStatus(@Param("status") CourseStatus status, Pageable pageable);

    // DTO Projection không kèm status
    @Query("SELECT new com.example.coursemanagement.dto.CourseResponseV2(c.id, c.title, c.status) FROM Course c")
    Page<CourseResponseV2> findAllProjected(Pageable pageable);

    // DTO Projection kèm lọc status
    @Query("SELECT new com.example.coursemanagement.dto.CourseResponseV2(c.id, c.title, c.status) FROM Course c WHERE c.status = :status")
    Page<CourseResponseV2> findAllProjectedByStatus(@Param("status") CourseStatus status, Pageable pageable);
}
