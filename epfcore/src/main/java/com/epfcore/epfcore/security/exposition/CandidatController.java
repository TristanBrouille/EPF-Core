package com.epfcore.epfcore.security.exposition;

import com.epfcore.epfcore.security.domain.Roles;
import com.epfcore.epfcore.security.domain.User;
import com.epfcore.epfcore.security.infrastructure.UserJpaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/candidats")
public class CandidatController {

    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${recaptcha.secret}")
    private String recaptchaSecret;

    public CandidatController(UserJpaRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserWithCaptcha candidat) {
        if (!verifyCaptcha(candidat.captchaToken())) {
            return ResponseEntity.badRequest().body("Captcha invalide");
        }

        if (userRepository.ofEmail(candidat.email()) != null) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User user = new User();
        user.setFirstname(candidat.firstname());
        user.setLastname(candidat.lastname());
        user.setEmail(candidat.email());
        user.setPassword(passwordEncoder.encode(candidat.password()));
        user.setBirthDate(candidat.birthDate());
        user.setRole(Roles.CANDIDAT);

        return ResponseEntity.ok(userRepository.save(user));
    }

    private boolean verifyCaptcha(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.google.com/recaptcha/api/siteverify?secret=" + recaptchaSecret + "&response=" + token;
        Map response = restTemplate.postForObject(url, null, Map.class);
        return response != null && Boolean.TRUE.equals(response.get("success"));
    }
}