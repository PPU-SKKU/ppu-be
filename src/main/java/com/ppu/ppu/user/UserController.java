package com.ppu.ppu.user;

import com.ppu.ppu.user.dto.UserPerfumeHaveDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

//    @GetMapping("/perfume/have")
//    public ResponseEntity<UserPerfumeHaveDto> havePerfume(HttpServletRequest request) {
//
//    }
}
