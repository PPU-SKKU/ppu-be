package com.ppu.ppu.user.profile;

import com.ppu.ppu.exception.ErrorCode;
import com.ppu.ppu.exception.domain.UserException;
import com.ppu.ppu.user.User;
import com.ppu.ppu.user.UserRepository;
import com.ppu.ppu.user.UserService;
import com.ppu.ppu.utils.image.ImageService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserProfileService {
    private UserRepository userRepository;
    private UserService userService;
    private ImageService imageService;

    public UserProfileGetDto getUserProfile(UUID id) {
        User user = userService.findUserById(id)
                .orElseThrow(() -> new UserException(ErrorCode.USER_LOAD_FAILED));

        UUID profileImage = user.getProfileImage();
        String nickname = user.getNickname();
        if(profileImage != null) {
            return new UserProfileGetDto(imageService.getDownloadUrl(profileImage), nickname);
        } else {
            return new UserProfileGetDto(null, nickname);
        }
    }

    @Transactional
    public void updateNicknameById(UUID id, String newNickname) {
        userRepository.updateNicknameById(id, newNickname);
    }

    @Transactional
    public void updateProfileImageById(UUID id, MultipartFile newProfile) {
        User user = userService.findUserById(id)
                .orElseThrow(() -> new UserException(ErrorCode.USER_LOAD_FAILED));

        if(newProfile == null || newProfile.isEmpty()) return;

        List<UUID> imageUUID = imageService.uploadImages(id, "ppubucket", "oppu", imageService.toList(newProfile));
        List<UUID> oldImageUUID = imageService.toList(user.getProfileImage());

        imageService.deleteObjectsAfterCommit(oldImageUUID);
        imageService.deleteObjectsOnRollback(imageUUID);

        userRepository.updateProfileImageById(id, imageUUID.get(0));
    }
}
