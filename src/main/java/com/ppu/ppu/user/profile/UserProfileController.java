package com.ppu.ppu.user.profile;

import com.ppu.ppu.exception.ErrorCode;
import com.ppu.ppu.exception.domain.UserException;
import com.ppu.ppu.user.User;
import com.ppu.ppu.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@CrossOrigin("*")
@Tag(name = "users profile", description = "회원 프로필 API")
@RestController
@RequestMapping("/users/profile")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserService userService;
    private final UserProfileService userProfileService;

    @GetMapping("")
    public ResponseEntity<UserProfileGetDto> getUserProfile(HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));

        return ResponseEntity.ok(userProfileService.getUserProfile(userId));
    }

    @PutMapping("/image")
    public ResponseEntity<Void> updateUserProfileImage(
            @RequestParam("image") MultipartFile profileImage,
            HttpServletRequest request) {

        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        userProfileService.updateProfileImageById(userId, profileImage);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/nickname")
    public ResponseEntity<Void> updateUserNickname(
            @RequestBody UserProfileNicknameUpdateDto nickname,
            HttpServletRequest request) {

        UUID userId = UUID.fromString((String) request.getAttribute("id"));
        System.out.println(nickname);
        userProfileService.updateNicknameById(userId, nickname);

        return ResponseEntity.noContent().build();
    }
}
