package com.epfcore.epfcore.user.service;

import com.epfcore.epfcore.security.domain.Roles;
import com.epfcore.epfcore.security.domain.User;
import com.epfcore.epfcore.security.domain.UserRepository;
import com.epfcore.epfcore.user.dto.UserAdmin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserAdminService userAdminService;

    private User user;
    private UserAdmin userAdmin;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstname("Test");
        user.setLastname("User");
        user.setEmail("test@test.com");
        user.setBirthDate(LocalDate.now());
        user.setRole(Roles.USER);
        user.setIdRfid("12345");

        userAdmin = new UserAdmin(1L, "Test", "User", "test@test.com", "password", LocalDate.now(), Roles.USER, "12345");
    }

    @Test
    void getUserById() {
        when(userRepository.ofId(1L)).thenReturn(Optional.of(user));
        UserAdmin result = userAdminService.getUserById(1L);
        assertNotNull(result);
        assertEquals(user.getFirstname(), result.firstname());
        verify(userRepository, times(1)).ofId(1L);
    }

    @Test
    void updateUser() {
        when(userRepository.ofId(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        UserAdmin result = userAdminService.updateUser(1L, userAdmin);

        assertNotNull(result);
        assertEquals(userAdmin.firstname(), result.firstname());
        verify(userRepository, times(1)).ofId(1L);
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("password");
    }
}