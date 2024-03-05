package com.sparta.lecture.global.repository;

import com.sparta.lecture.global.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
