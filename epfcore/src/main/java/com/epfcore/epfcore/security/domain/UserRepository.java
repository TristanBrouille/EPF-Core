package com.epfcore.epfcore.security.domain;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    User ofEmail(String email);
    Optional<User> ofId(Long id);
    User save(User user);
    Collection<User> ofAll();
}
