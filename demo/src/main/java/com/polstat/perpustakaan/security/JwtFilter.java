package com.polstat.perpustakaan.security;

import com.polstat.perpustakaan.auth.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // KOREKSI FINAL KRITIS: Lewati filter JWT secara eksplisit untuk semua endpoint di bawah /auth/
        if (path.startsWith("/auth/") || path.equals("/graphql") || path.equals("/graphiql")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Ambil header Authorization
        String authHeader = request.getHeader("Authorization");

        // Periksa apakah header ada dan dimulai dengan "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Jika token tidak ada, jatuhkan ke AuthEntryPoint yang akan memberikan 401 custom.
            // Biarkan Spring Security melanjutkan filter chain-nya.
            filterChain.doFilter(request, response);
            return;
        }

        // Validasi JWT untuk semua request lainnya
        try {
            String token = authHeader.substring(7);
            String email = jwtService.extractEmail(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    // Set otentikasi
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    // Token tidak valid (misal, expired)
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
        } catch (Exception e) {
            // Tangani error token (misal: signature invalid)
            // LAKUKAN INI JIKA ANDA INGIN MENGEMBALIKAN 401 DARI FILTER INI
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}