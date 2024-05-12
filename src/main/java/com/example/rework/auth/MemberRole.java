package com.example.rework.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    MEMBER("ROLE_MEMBER"),MANAGER("ROLE_MANAGER"),ADMIN("ROLE_ADMIN");
    private final String key;
}
