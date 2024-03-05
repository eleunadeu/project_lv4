package com.sparta.lecture.domain.lecture.dto;

import com.sparta.lecture.domain.lecture.entity.Comment;
import com.sparta.lecture.domain.lecture.entity.Lecture;
import com.sparta.lecture.global.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentDto {
    private Long commentId;
    private String content;
    private Lecture lecture;
    private User user;

    @Builder
    public CommentDto(Long commentId, String content, Lecture lecture, User user) {
        this.commentId = commentId;
        this.content = content;
        this.lecture = lecture;
        this.user = user;
    }


    @Getter
    @NoArgsConstructor
    public static class GetCommentResponseDto {
        private Long commentId;
        private String content;
        private Lecture lecture;

        public GetCommentResponseDto(Comment comment) {
            this.commentId = comment.getCommentId();
            this.content = comment.getContent();
            this.lecture = comment.getLecture();
        }
    }
}
