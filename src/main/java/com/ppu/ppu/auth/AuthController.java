package com.ppu.ppu.auth;


import com.ppu.ppu.user.User;
import com.ppu.ppu.user.UserService;
import com.ppu.ppu.user.dto.UserCreateDto;
import com.ppu.ppu.user.dto.UserLoginReponseDto;
import com.ppu.ppu.user.dto.UserPwLoginDto;
import com.ppu.ppu.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody UserCreateDto user) {
        Optional<User> existingUser = this.userService.getUserByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            // 가입 불가
            return ResponseEntity.badRequest().build();
        }
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        userService.createUser(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginReponseDto> login(@RequestBody UserPwLoginDto user, HttpServletRequest request) {
        Optional<User> existingUser = this.userService.getUserByEmail(user.getEmail());

        if (!existingUser.isPresent() || !BCrypt.checkpw(user.getPassword(), existingUser.get().getPassword())) {
            return ResponseEntity.badRequest().build();
        }

        String accessToken = jwtUtil.generateToken(existingUser.get().getId().toString());
        return ResponseEntity.status(200).body(new UserLoginReponseDto(accessToken));
    }
}
