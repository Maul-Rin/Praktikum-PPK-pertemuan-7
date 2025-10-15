package com.polstat.perpustakaan.auth;

import com.polstat.perpustakaan.entity.User;
import com.polstat.perpustakaan.repository.UserRepository;
import com.polstat.perpustakaan.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // <-- PASTIKAN INI ADA
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException; // <-- PASTIKAN INI ADA untuk menangani error
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*; // <-- PASTIKAN INI ADA UNTUK RESTCONTROLLER, MAPPING, DLL.

// IMPORTS SPRINGDOC (PASTIKAN SEMUA INI ADA)
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody; // <-- PASTIKAN INI ADA

@RestController // <-- Pastikan ini ada
@RequestMapping("/auth") // <-- Pastikan ini ada
@Tag(name = "Authentication", description = "Endpoints untuk Registrasi dan Login Pengguna")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    // REGISTER
    @Operation(summary = "Mendaftarkan pengguna baru.")
    @RequestBody(
            description = "Data pendaftaran: email, password, dan fullName",
            required = true,
            content = @Content(schema = @Schema(implementation = AuthRequest.Register.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User berhasil didaftarkan!",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Email sudah terdaftar!")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@org.springframework.web.bind.annotation.RequestBody AuthRequest.Register request) {
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

    // LOGIN
    @Operation(summary = "Login pengguna untuk mendapatkan token akses (JWT).")
    @RequestBody(
            description = "Kredensial login: email dan password",
            required = true,
            content = @Content(schema = @Schema(implementation = AuthRequest.Login.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login berhasil, mengembalikan JWT token.",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Email atau password salah. Coba lagi!")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@org.springframework.web.bind.annotation.RequestBody AuthRequest.Login request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User tidak ditemukan!"));

            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles("USER")
                    .build();

            String token = jwtService.generateToken(userDetails);

            return ResponseEntity.ok(
                    AuthResponse.builder()
                            .message("Login berhasil")
                            .email(user.getEmail())
                            .token(token)
                            .build()
            );

        } catch (Exception e) {
            // FIX: Menggunakan HttpStatus.UNAUTHORIZED untuk 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.builder()
                            .message("Email atau password salah. Coba lagi!")
                            .email(request.getEmail())
                            .token(null)
                            .build());
        }
    }
}