package com.edentech.firstserverapi.auth;

import com.edentech.firstserverapi.model.AppUser;
import com.edentech.firstserverapi.repository.AppUserRepository;
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
        private PasswordEncoder passwordEncoder;

        @PostMapping("/register")
        public String register(@RequestBody AuthRequest request) {

            AppUser user = new AppUser(
                    request.getUsername(),
                    passwordEncoder.encode(request.getPassword())
            );

            userRepository.save(user);

            return "User registered";
        }

    }
