package com.sparta.lecture.domain.lecture.entity;

import com.sparta.lecture.global.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment")
public class Comment {

    // 유효성 검사(size, notnull)
    // 댓글 생성 및 수정 시간 추가 고려
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    private Lecture lecture;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Comment(String content, Lecture lecture, User user) {
        this.content = content;
        this.lecture = lecture;
        this.user = user;
    }

    public void updateComment(String content) {
        this.content = content;
    }
}
