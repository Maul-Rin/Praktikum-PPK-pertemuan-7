package com.polstat.perpustakaan.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String message;
    private String email;
    private String token; // JWT token, null saat register
}
