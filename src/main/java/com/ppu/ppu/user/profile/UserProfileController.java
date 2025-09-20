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

import java.util.UUID;

@CrossOrigin("*")
@Tag(name = "users profile", description = "회원 프로필 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserService userService;
    private final UserProfileService userProfileService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileGetDto> getUserProfile(HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));

        return ResponseEntity.ok(userProfileService.getUserProfile(userId));
    }

    @PostMapping("/profile")
    public ResponseEntity<Void> updateUserProfile(@ModelAttribute UserProfileUpdateDto dto, HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        User user = userService.findUserById(userId).orElseThrow(() -> new UserException(ErrorCode.USER_LOAD_FAILED));

        if(dto.getProfileImage() != null)
            userProfileService.updateProfileImageById(userId, dto.getProfileImage());

        if(dto.getNickname() != null)
            userProfileService.updateNicknameById(userId, dto.getNickname());

        return ResponseEntity.ok().build();
    }
}
