package com.ppu.ppu.user;

import com.ppu.ppu.user.dto.UserNicknameUpdateDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/nickname")
    public ResponseEntity<Void> updateNickname(HttpServletRequest request, @RequestBody UserNicknameUpdateDto updateDto) {
        String id = request.getAttribute("id").toString();
        userService.updateUserNickname(id, updateDto.getNickname());
        return ResponseEntity.noContent().build();
    }



}
