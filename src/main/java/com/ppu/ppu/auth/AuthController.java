package com.ppu.ppu.auth;


import com.ppu.ppu.user.User;
import com.ppu.ppu.user.UserService;
import com.ppu.ppu.user.dto.UserCreateDto;
import com.ppu.ppu.user.dto.UserPwLoginDto;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpHeaders;
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

    public AuthController(UserService userService) {
        this.userService = userService;
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
    public ResponseEntity<Void> login(@RequestBody UserPwLoginDto user) {
        Optional<User> existingUser = this.userService.getUserByEmail(user.getEmail());
        System.out.println(BCrypt.checkpw(user.getPassword(), existingUser.get().getPassword()));
        if (!existingUser.isPresent() || !BCrypt.checkpw(user.getPassword(), existingUser.get().getPassword())) {
            return ResponseEntity.badRequest().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Bearer", "token");
        return ResponseEntity.ok().headers(headers).build();
    }
}
