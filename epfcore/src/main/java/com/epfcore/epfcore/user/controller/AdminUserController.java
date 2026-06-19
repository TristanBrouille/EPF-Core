package com.epfcore.epfcore.user.controller;

import com.epfcore.epfcore.security.domain.Roles;
import com.epfcore.epfcore.user.dto.UserAdmin;
import com.epfcore.epfcore.user.dto.UserDto;
import com.epfcore.epfcore.user.service.AdminService;
import com.epfcore.epfcore.user.service.UserAdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminUserController {

    private final AdminService adminService;
    private final UserAdminService userAdminService;

    public AdminUserController(AdminService adminService, UserAdminService userAdminService) {
        this.adminService = adminService;
        this.userAdminService = userAdminService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            Collection<UserDto> users = adminService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve users list");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            UserAdmin userAdmin = userAdminService.getUserById(id);
            return ResponseEntity.ok(userAdmin);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with id: " + id);
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<Collection<String>> getRoles() {
        return ResponseEntity.ok(Arrays.stream(Roles.values())
                .map(Roles::name)
                .collect(Collectors.toList()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserAdmin userAdminData) {
        try {
            UserAdmin updatedUser = userAdminService.updateUser(id, userAdminData);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to update user: " + e.getMessage());
        }
    }


}
