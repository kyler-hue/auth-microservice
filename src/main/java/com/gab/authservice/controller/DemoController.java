package com.gab.authservice.controller;

import com.gab.authservice.dto.SignupRequest;
import com.gab.authservice.service.CustomUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    private final CustomUserDetailsService customUserDetailsService;

    public DemoController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @GetMapping("/api/demo/hello_user")
    @PreAuthorize("hasRole('USER')")
    public String securedHello() {
        return "You accessed a USER endpoint!";
    }

    @GetMapping("/api/demo/hello_admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String securedAdmin() {
        return "You accessed a ADMIN endpoint!";
    }

    @GetMapping("/api/demo/hello_public")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String securedPublic() {
        return "You accessed a PUBLIC endpoint!";
    }

    @GetMapping("/api/demo/getUserDetails")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public UserDetails loadUserByUsername(@RequestBody @Valid SignupRequest request) {
        return customUserDetailsService.loadUserByUsername(request.getEmail());
    }

}
