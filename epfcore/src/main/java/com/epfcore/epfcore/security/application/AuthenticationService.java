package com.epfcore.epfcore.security.application;

import com.epfcore.epfcore.security.domain.User;
import com.epfcore.epfcore.security.domain.UserRepository;
import com.epfcore.epfcore.security.exposition.Me;
import com.epfcore.epfcore.security.exposition.UserExpose;
import com.epfcore.epfcore.security.exposition.UserLog;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public Me auth(UserLog req,
                    HttpServletRequest request,
                    HttpServletResponse response){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextRepository.saveContext(context, request, response);
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        List<String> authority = principal
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return new Me(principal.getUsername(), authority);
    }

    public UserExpose getAuthenticatedUser(Authentication authentication){
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                throw new SecurityException("Not authenticated");
            }

            String email = authentication.getName();

            User user = userRepository.ofEmail(email);
            if (user == null) {
                throw new IllegalArgumentException("User not found");
            }

            return new UserExpose(
                    user.getFirstname(),
                    user.getLastname(),
                    user.getEmail(),
                    user.getBirthDate(),
                    user.getRole()
            );

        } catch (SecurityException e) {
            throw e;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while fetching user data", e);
        }
    }

    @Transactional
    public User registerUser(User user) {
        try {
            if (userRepository.ofEmail(user.getEmail()) != null) {
                throw new IllegalArgumentException("Email already exists");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while creating the user account", e);
        }
    }


}
