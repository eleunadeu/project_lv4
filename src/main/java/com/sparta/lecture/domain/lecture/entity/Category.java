package com.sparta.lecture.domain.lecture.entity;

import lombok.Getter;

@Getter
public enum Category {
    SPRING("SPRING"), REACT("REACT"), NODE("NODE");
    private final String category;

    Category(String category) {
        this.category = category;
    }
}
