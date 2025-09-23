package com.ppu.ppu.oppu;

import com.ppu.ppu.exception.ErrorCode;
import com.ppu.ppu.exception.domain.UserException;
import com.ppu.ppu.user.User;
import com.ppu.ppu.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OppuService {
    private final UserRepository userRepository;
    OppuRepository oppuRepository;


    public void postArticle(UUID userId, OppuPostArticleDto post) {
        validateUser(userId);

//        List<UUID> imageUUIDs = imageService.
        Oppu oppu = OppuPostArticleDto.fromDto(userId, post);
    }

    private void validateUser(UUID userId) {
        User User = userRepository.findById(userId).orElseThrow(() -> new UserException(ErrorCode.USER_LOAD_FAILED));
    }
}
