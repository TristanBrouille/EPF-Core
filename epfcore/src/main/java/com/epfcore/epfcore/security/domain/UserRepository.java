package com.epfcore.epfcore.security.domain;

public interface UserRepository {
    User ofEmail(String email);
}
