package com.edentech.firstserverapi.controller;

import com.edentech.firstserverapi.dto.AppUserDTO;
import com.edentech.firstserverapi.dto.AuthRegisterResponse;
import com.edentech.firstserverapi.dto.AuthRequest;
import com.edentech.firstserverapi.dto.AuthResponse;
import com.edentech.firstserverapi.entity.AppUserEntity;
import com.edentech.firstserverapi.mapper.AppUserMapper;
import com.edentech.firstserverapi.repository.AppUserRepository;
import com.edentech.firstserverapi.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://virtualgamer.co.uk",
        "https://www.virtualgamer.co.uk"
})
public class AuthController {

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public AuthRegisterResponse register(@RequestBody AuthRequest request) {
        AppUserEntity user = new AppUserEntity(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword())
        );
        AppUserEntity savedUser = userRepository.save(user);
        AppUserDTO userDTO = AppUserMapper.toDTO(savedUser);
        return new AuthRegisterResponse("User registered", userDTO.getId());
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        AppUserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponse(token);
    }
}
