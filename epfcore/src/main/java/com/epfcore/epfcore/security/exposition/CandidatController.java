package com.epfcore.epfcore.security.exposition;

import com.epfcore.epfcore.security.domain.Roles;
import com.epfcore.epfcore.security.domain.User;
import com.epfcore.epfcore.security.infrastructure.UserJpaRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
