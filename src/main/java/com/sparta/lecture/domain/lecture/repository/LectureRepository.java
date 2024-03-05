package com.sparta.lecture.domain.lecture.repository;

import com.sparta.lecture.domain.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
}
