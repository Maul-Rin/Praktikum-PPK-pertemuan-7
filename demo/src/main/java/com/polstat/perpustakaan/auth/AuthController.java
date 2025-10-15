package com.polstat.perpustakaan.auth;

// ... (omitted existing imports)
// IMPORTS BARU
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
// Import tambahan untuk requestBody
import io.swagger.v3.oas.annotations.parameters.RequestBody;
// ... (omitted existing imports)

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints untuk Registrasi dan Login Pengguna")
public class AuthController {

    // ... (omitted existing fields)

    // REGISTER
    @Operation(summary = "Mendaftarkan pengguna baru.")
    @RequestBody(
            description = "Data pendaftaran: email dan password",
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
        // ... (method body)
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
        // ... (method body)
    }
}