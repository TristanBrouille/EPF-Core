package com.epfcore.epfcore.user.service;

import com.epfcore.epfcore.security.domain.User;
import com.epfcore.epfcore.security.domain.UserRepository;
import com.epfcore.epfcore.user.dto.UserAdmin;
import com.epfcore.epfcore.user.dto.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserAdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAdminService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public UserAdmin getUserById(Long id) {
        User user = userRepository.ofId(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToUserAdmin(user);
    }

    @Transactional
    public UserAdmin updateUser(Long id, UserAdmin dto) {
        User user = userRepository.ofId(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (dto.firstname() != null) user.setFirstname(dto.firstname());
        if (dto.lastname() != null) user.setLastname(dto.lastname());
        if (dto.email() != null) user.setEmail(dto.email());
        if (dto.birthDate() != null) user.setBirthDate(dto.birthDate());
        if (dto.role() != null) user.setRole(dto.role());
        if (dto.idRfid() != null) user.setIdRfid(dto.idRfid());

        if (dto.password() != null && !dto.password().trim().isEmpty()) {
            String encryptedPassword = passwordEncoder.encode(dto.password());
            user.setPassword(encryptedPassword);
        }

        User updatedUser = userRepository.save(user);
        return mapToUserAdmin(updatedUser);
    }

    private UserAdmin mapToUserAdmin(User user) {
        return new UserAdmin(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                null,
                user.getBirthDate(),
                user.getRole(),
                user.getIdRfid()
        );
    }
}
