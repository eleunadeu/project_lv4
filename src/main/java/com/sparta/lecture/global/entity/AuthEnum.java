package com.sparta.lecture.global.entity;

import lombok.Getter;

@Getter
public enum AuthEnum {
    USER(Authority.USER),
    ADMIN(Authority.ADMIN);

    private final String authority;

    AuthEnum(String authority) {
        this.authority = authority;
    }

    public static class Authority {
        public static final String USER = "AUTH_USER";
        public static final String ADMIN = "AUTH_ADMIN";
    }
}
