package com.epfcore.epfcore.security.exposition;

import com.epfcore.epfcore.security.domain.Roles;
import com.epfcore.epfcore.security.domain.User;
import com.epfcore.epfcore.security.infrastructure.UserJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candidats")
public class CandidatController {


    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public CandidatController(UserJpaRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User candidat) {
        if (userRepository.ofEmail(candidat.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        candidat.setPassword(passwordEncoder.encode(candidat.getPassword()));
        candidat.setRole(Roles.CANDIDAT);
        return ResponseEntity.ok(userRepository.save(candidat));
    }


}
