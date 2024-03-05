package com.sparta.lecture.domain.lecture.repository;

import com.sparta.lecture.domain.lecture.entity.Comment;
import com.sparta.lecture.domain.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByLecture(Lecture lecture);
}
