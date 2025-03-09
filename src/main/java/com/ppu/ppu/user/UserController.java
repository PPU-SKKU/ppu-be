package com.ppu.ppu.user;

import com.ppu.ppu.user.dto.UserNicknameUpdateDto;
import com.ppu.ppu.utils.FileUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final FileUtil fileUtil;

    public UserController(UserService userService, FileUtil fileUtil) {
        this.userService = userService;
        this.fileUtil = fileUtil;
    }

    @PatchMapping("/nickname")
    public ResponseEntity<Void> updateNickname(HttpServletRequest request, @RequestBody UserNicknameUpdateDto updateDto) {
        String id = request.getAttribute("id").toString();
        userService.updateUserNickname(id, updateDto.getNickname());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/profile-img")
    public ResponseEntity<Void> updateProfileImage(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        String id = request.getAttribute("id").toString();
        try {
            Optional<String> filename = fileUtil.storeFile(file);
            if (!filename.isPresent()) {
                return ResponseEntity.badRequest().build();
            }
            userService.updateUserProfileImage(id, filename.get());
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
