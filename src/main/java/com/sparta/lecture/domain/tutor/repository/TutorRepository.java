package com.sparta.lecture.domain.tutor.repository;

import com.sparta.lecture.domain.tutor.entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
}
