package com.polstat.perpustakaan.auth;

import com.polstat.perpustakaan.entity.User;
import com.polstat.perpustakaan.repository.UserRepository;
import com.polstat.perpustakaan.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Import yang benar
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
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
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest.Register request) {
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
        // KOREKSI DARURAT: Menyimpan password sebagai plaintext
        user.setPassword(request.getPassword()); // <--- Hapus enkripsi
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
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest.Login request) {
        try {
            // PERBAIKAN TYPO: Menggunakan UsernamePasswordAuthenticationToken
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User tidak ditemukan!"));

            // Membuat objek UserDetails
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
            // Blok ini sekarang harus menangkap kesalahan jika sandi plaintext salah.
            return ResponseEntity.status(401)
                    .body(AuthResponse.builder()
                            .message("Email atau password salah. Coba lagi!")
                            .email(request.getEmail())
                            .token(null)
                            .build());
        }
    }
}