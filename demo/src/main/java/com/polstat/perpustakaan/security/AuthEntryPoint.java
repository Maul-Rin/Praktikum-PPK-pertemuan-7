package com.polstat.perpustakaan.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // Mengembalikan status 401 dan pesan JSON yang jelas
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // Pesan akan muncul jika akses endpoint terlindungi tanpa token (sehingga AuthFilter menangkapnya)
        response.getOutputStream().println("{ \"error\": \"Unauthorized\", \"message\": \"Akses ditolak: " + authException.getMessage() + "\" }");
    }
}