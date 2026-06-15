package com.epfcore.epfcore.security.infrastructure;

import com.epfcore.epfcore.security.domain.Roles;
import com.epfcore.epfcore.security.domain.User;
import com.epfcore.epfcore.security.domain.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long>, UserRepository {


    @Query("""
    select user from User user where user.email= :email
    """)
    User findByEmail(String email);

    List<User> findByRoleIn(List<Roles> roles);

    @Override
    default User ofEmail(String email){
        return findByEmail(email);
    }
}

