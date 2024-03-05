package com.sparta.lecture.domain.lecture.repository;

import com.sparta.lecture.domain.lecture.entity.Category;
import com.sparta.lecture.domain.lecture.entity.Lecture;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    List<Lecture> findByCategory(Category category, Sort sort);
}
