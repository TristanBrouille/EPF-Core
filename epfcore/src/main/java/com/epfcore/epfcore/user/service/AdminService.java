package com.epfcore.epfcore.user.service;

import com.epfcore.epfcore.security.domain.User;
import com.epfcore.epfcore.security.domain.UserRepository;
import com.epfcore.epfcore.security.exposition.Me;
import com.epfcore.epfcore.user.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
public class AdminService {
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    public AdminService(UserRepository userRepository, UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }


    @Transactional(readOnly = true)
    public Collection<UserDto> getAllUsers() {
        try {
            return userRepository.ofAll().stream()
                    .map(user -> new UserDto(user.getId(), user.getFirstname(), user.getLastname(), user.getRole()))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching all users", e);
        }
    }

    public Me impersonate(Long userId, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        try {
            if (session.getAttribute("ORIGINAL_USER_CONTEXT") == null) {
                session.setAttribute("ORIGINAL_USER_CONTEXT", securityContextHolderStrategy.getContext());
            }

            User targetUser = userRepository.ofId(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

            UserDetails principal = userDetailsService.loadUserByUsername(targetUser.getEmail());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    principal.getAuthorities()
            );

            SecurityContext context = securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authentication);
            securityContextHolderStrategy.setContext(context);
            securityContextRepository.saveContext(context, request, response);

            // 5. Extraction des rôles pour le retour
            List<String> authorities = principal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            return new Me(principal.getUsername(), authorities);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred during impersonation process", e);
        }
    }


    public Me revertImpersonation(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        try {
            SecurityContext originalContext = (SecurityContext) session.getAttribute("ORIGINAL_USER_CONTEXT");

            if (originalContext == null || originalContext.getAuthentication() == null) {
                throw new IllegalStateException("No original admin context found in session");
            }

            securityContextHolderStrategy.setContext(originalContext);
            securityContextRepository.saveContext(originalContext, request, response);

            session.removeAttribute("ORIGINAL_USER_CONTEXT");

            UserDetails principal = (UserDetails) originalContext.getAuthentication().getPrincipal();
            List<String> authorities = principal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            return new Me(principal.getUsername(), authorities);

        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while reverting impersonation", e);
        }
    }
}
