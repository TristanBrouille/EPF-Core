package com.epfcore.epfcore.user.controller;

import com.epfcore.epfcore.security.exposition.Me;
import com.epfcore.epfcore.user.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class ImpersonateController {

    private final AdminService adminService;

    public ImpersonateController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/impersonate/{userId}")
    public ResponseEntity<?> impersonate(
            @PathVariable Long userId,
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session
    ) {
        try {
            Me targetUserIdentity = adminService.impersonate(userId, request, response, session);
            return ResponseEntity.ok(targetUserIdentity);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred during impersonation");
        }
    }

    @PostMapping("/impersonate/revert")
    public ResponseEntity<?> revert(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpSession session
    ) {
        try {
            Me adminIdentity = adminService.revertImpersonation(request, response, session);
            return ResponseEntity.ok(adminIdentity);

        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while reverting identity");
        }
    }
}