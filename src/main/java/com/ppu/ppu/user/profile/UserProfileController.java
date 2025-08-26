package com.ppu.ppu.user.profile;

import com.ppu.ppu.exception.ErrorCode;
import com.ppu.ppu.exception.domain.UserException;
import com.ppu.ppu.user.User;
import com.ppu.ppu.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Tag(name = "users profile", description = "회원 프로필 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> havePerfume(HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));

        Optional<User> user = userService.findUserById(userId);

        if(!user.isPresent()) {
            throw new UserException(ErrorCode.USER_LOAD_FAILED);
        }

        return ResponseEntity.ok(new UserProfileDto(user.get().getProfileImage(), user.get().getNickname()));
    }

    @PostMapping("/profile")
    public ResponseEntity<Void> havePerfume(@RequestBody UserProfileDto dto, HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        Optional<User> user = userService.findUserById(userId);

        if(!user.isPresent()) {
            throw new UserException(ErrorCode.USER_LOAD_FAILED);
        }

        System.out.println(dto.getNickname());
        System.out.println(dto.getProfileImage());
        if(dto.getProfileImage() != null)
            userService.updateProfileImageById(userId, dto.getProfileImage());

        if(dto.getNickname() != null)
            userService.updateNicknameById(userId, dto.getNickname());

        return ResponseEntity.ok().build();
    }
}
