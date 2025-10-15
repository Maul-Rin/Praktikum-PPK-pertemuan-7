package com.polstat.perpustakaan.mapper;

import com.polstat.perpustakaan.dto.UserDto;
import com.polstat.perpustakaan.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // Dari DTO ke Entity
    public User toEntity(UserDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return user;
    }

    // Dari Entity ke DTO
    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setEmail(user.getEmail());
        // password tidak dikirim ke DTO untuk keamanan
        return dto;
    }
}
