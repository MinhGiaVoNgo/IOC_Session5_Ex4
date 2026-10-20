package com.example.coursemanagement.config;

import com.example.coursemanagement.entity.Course;
import com.example.coursemanagement.entity.Instructor;
import com.example.coursemanagement.enums.CourseStatus;
import com.example.coursemanagement.repository.CourseRepository;
import com.example.coursemanagement.repository.InstructorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;

    public DataInitializer(CourseRepository courseRepository, InstructorRepository instructorRepository) {
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
    }

    @Override
    public void run(String... args) {
        if (instructorRepository.count() == 0 && courseRepository.count() == 0) {

            // Tạo 10 Instructor
            List<Instructor> instructors = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                instructors.add(Instructor.builder()
                        .name("Instructor " + i)
                        .email("instructor" + i + "@example.com")
                        .phone("090000000" + i)
                        .build());
            }
            instructorRepository.saveAll(instructors);

            // Tạo 10 Course, mỗi Course gắn với 1 Instructor
            String[] titles = {
                "Java Spring Boot", "Angular Frontend", "React JS",
                "Database MySQL", "Docker & Kubernetes", "Node.js Backend",
                "Python Django", "Vue.js Frontend", "AWS Cloud", "DevOps CI/CD"
            };
            String[] descriptions = {
                "Khóa học Spring Boot cơ bản", "Khóa học Angular nâng cao",
                "Khóa học React cho người mới", "Khóa học MySQL chuyên sâu",
                "Khóa học container hóa", "Khóa học Node.js từ đầu",
                "Khóa học Python Django", "Khóa học Vue.js thực chiến",
                "Khóa học AWS cơ bản", "Khóa học DevOps pipeline"
            };

            List<Course> courses = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                courses.add(Course.builder()
                        .title(titles[i])
                        .description(descriptions[i])
                        .price(400000.0 + (i * 50000))
                        .status(i % 2 == 0 ? CourseStatus.ACTIVE : CourseStatus.INACTIVE)
                        .instructor(instructors.get(i))
                        .build());
            }
            courseRepository.saveAll(courses);

            System.out.println("✅ Đã thêm 10 Instructor và 10 Course vào database.");
            System.out.println("📌 Gọi GET /courses để thấy N+1 Query trong console log.");
            System.out.println("📌 Gọi GET /courses/v2 để thấy KHÔNG còn N+1 Query.");
        }
    }
}
