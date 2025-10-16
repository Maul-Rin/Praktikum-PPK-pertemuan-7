package com.polstat.perpustakaan.auth;

import com.polstat.perpustakaan.entity.User;
import com.polstat.perpustakaan.repository.UserRepository;
import com.polstat.perpustakaan.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

// IMPORTS SPRINGDOC
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints untuk Registrasi dan Login Pengguna")
public class AuthController {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // Constructor Injection
    public AuthController(UserRepository userRepository,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // ============= REGISTER =============
    @Operation(summary = "Mendaftarkan pengguna baru.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User berhasil didaftarkan!",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Email sudah terdaftar!",
                    content = @Content
            )
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody AuthRequest.Register request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(
                    AuthResponse.builder()
                            .message("Email sudah terdaftar!")
                            .email(request.getEmail())
                            .token(null)
                            .build()
            );
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        userRepository.save(user);

        return ResponseEntity.ok(
                AuthResponse.builder()
                        .message("User berhasil didaftarkan!")
                        .email(user.getEmail())
                        .token(null)
                        .build()
        );
    }

    // ============= LOGIN =============
    @Operation(summary = "User login to get access token.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Email and access token",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content
            )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody AuthRequest.Login request) {

        try {
            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Find user
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User tidak ditemukan!"));

            // Create UserDetails
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles("USER")
                    .build();

            // Generate JWT token
            String token = jwtService.generateToken(userDetails);

            // Return success response
            return ResponseEntity.ok(
                    AuthResponse.builder()
                            .message("Login berhasil")
                            .email(user.getEmail())
                            .token(token)
                            .build()
            );

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.builder()
                            .message("Invalid credentials")
                            .email(request.getEmail())
                            .token(null)
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.builder()
                            .message("Email atau password salah. Coba lagi!")
                            .email(request.getEmail())
                            .token(null)
                            .build());
        }
    }
}