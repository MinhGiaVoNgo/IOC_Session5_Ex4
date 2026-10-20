package com.example.coursemanagement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "instructors")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String phone;
}
