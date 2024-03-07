package com.sparta.lecture.domain.lecture.repository;

import com.sparta.lecture.domain.lecture.entity.Lecture;
import com.sparta.lecture.domain.lecture.entity.Likes;
import com.sparta.lecture.global.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByLectureAndUser(Lecture lecture, User user);

    boolean existsByLectureAndUser(Lecture lecture, User user);
}
