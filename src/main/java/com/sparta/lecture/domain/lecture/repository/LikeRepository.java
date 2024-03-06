package com.sparta.lecture.domain.lecture.repository;

import com.sparta.lecture.domain.lecture.entity.Lecture;
import com.sparta.lecture.domain.lecture.entity.Like;
import com.sparta.lecture.global.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Object> findByLectureAndUser(Lecture lecture, User user);

    boolean existsByLectureAndUser(Lecture lecture, User user);
}
