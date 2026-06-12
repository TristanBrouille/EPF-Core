package com.epfcore.epfcore.security.exposition;

import com.epfcore.epfcore.security.domain.Roles;
import com.epfcore.epfcore.security.domain.User;
import com.epfcore.epfcore.security.infrastructure.UserJpaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class CandidatService {

    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${recaptcha.secret}")
    private String recaptchaSecret;

    public CandidatService(UserJpaRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(UserWithCaptcha candidat) {
        if (!verifyCaptcha(candidat.captchaToken())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Captcha invalide");
        }

        if (userRepository.ofEmail(candidat.email()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        User user = new User();
        user.setFirstname(candidat.firstname());
        user.setLastname(candidat.lastname());
        user.setEmail(candidat.email());
        user.setPassword(passwordEncoder.encode(candidat.password()));
        user.setBirthDate(candidat.birthDate());
        user.setRole(Roles.CANDIDAT);

        return userRepository.save(user);
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