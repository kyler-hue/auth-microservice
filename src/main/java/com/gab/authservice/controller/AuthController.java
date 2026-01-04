package com.gab.authservice.controller;

import com.gab.authservice.dto.AuthenticationResponse;
import com.gab.authservice.dto.LoginRequest;
import com.gab.authservice.dto.SignupRequest;
import com.gab.authservice.entity.RefreshToken;
import com.gab.authservice.entity.User;
import com.gab.authservice.service.AuthService;
import com.gab.authservice.service.JwtService;
import com.gab.authservice.service.RefreshTokenService;
import com.gab.authservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.ok("User registered successfully");
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> postMethodName(@RequestBody LoginRequest request) {
//        String token = authService.login(request);
//        return ResponseEntity.ok(token);
//    }

    @GetMapping("/public-key")
    public ResponseEntity<String> getPublicKey() {
        return ResponseEntity.ok(jwtService.getPublicKeyPEM());
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest request) {

        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        String userName = auth.getName();
        User user = userService.getUserByUserName(userName);
        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        return new AuthenticationResponse(accessToken, refreshToken.getToken());
    }

    @PostMapping("/refresh")
    public AuthenticationResponse refreshToken(@RequestBody Map<String, String> request) {

        String refreshToken = request.get("refreshToken");

        RefreshToken token = refreshTokenService
                .verifyExpiration(refreshToken);

        User user = token.getUser();
        String newAccessToken = jwtService.generateToken(user);

        return new AuthenticationResponse(
                newAccessToken,
                refreshToken
        );
    }

    @PostMapping("/logout")
    public void logout(Authentication authentication) {
        String email = authentication.getName(); //
        User user = userService.getUserByUserName(email);
        refreshTokenService.deleteByUser(user);
    }


}
