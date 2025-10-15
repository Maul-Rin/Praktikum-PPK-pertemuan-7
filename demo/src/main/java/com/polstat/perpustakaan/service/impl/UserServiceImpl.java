package com.polstat.perpustakaan.service.impl;

import com.polstat.perpustakaan.dto.UserDto;
import com.polstat.perpustakaan.entity.User;
import com.polstat.perpustakaan.mapper.UserMapper;
import com.polstat.perpustakaan.repository.UserRepository;
import com.polstat.perpustakaan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        // Encode password
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Convert DTO ke Entity
        User user = userMapper.toEntity(userDto);

        // Simpan ke database
        user = userRepository.save(user);

        // Convert kembali ke DTO
        return userMapper.toDto(user);
    }
}
