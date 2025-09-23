package com.ppu.ppu.oppu;

import com.ppu.ppu.exception.ErrorCode;
import com.ppu.ppu.exception.domain.OppuException;
import com.ppu.ppu.exception.domain.UserException;
import com.ppu.ppu.user.UserRepository;
import com.ppu.ppu.utils.image.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OppuService {
    private final UserRepository userRepository;
    OppuRepository oppuRepository;
    ImageService imageService;

    @Transactional
    public void postArticle(UUID userId, OppuPostArticleDto post, List<MultipartFile> images) {
        validateUser(userId);
        List<UUID> imageUUIDs = imageService.uploadImages(userId, "ppubucket", "oppu", images);
        imageService.deleteObjectsOnRollback(imageUUIDs);

        Oppu oppu = OppuPostArticleDto.fromDto(userId, imageUUIDs, post);
        oppuRepository.save(oppu);
    }

    @Transactional(readOnly = true)
    public OppuGetArticleDto getArticle(UUID userId, UUID oppuId) {
        validateUser(userId);
        Oppu oppu = oppuRepository.findById(oppuId).orElseThrow(() -> new OppuException(ErrorCode.OPPU_NO_ARTICLE));

        List<URL> imageUrls = imageService.getDownloadUrls(oppu.getImages());
        return OppuGetArticleDto.fromEntity(oppu, imageUrls);
    }

    @Transactional
    public void putArticle(UUID userId, UUID oppuId, OppuPostArticleDto post, List<MultipartFile> images) {
        validateUser(userId);
        Oppu oppu = oppuRepository.findById(oppuId).orElseThrow(() -> new OppuException(ErrorCode.OPPU_NO_ARTICLE));

        if(!Objects.equals(oppu.getUserId(), userId)) {
            throw new OppuException(ErrorCode.OPPU_UNAUTHORIZED);
        }

        List<UUID> imageUUIDs = imageService.uploadImages(userId, "ppubucket", "oppu", images);
        List<UUID> oldImageUUIDs = oppu.getImages();

        imageService.deleteObjectsOnRollback(imageUUIDs);
        imageService.deleteObjectsAfterCommit(oldImageUUIDs);

        oppu.setDate(post.getDate());
        oppu.setPerfumes(post.getPerfumeIds());
        oppu.setImages(imageUUIDs);
        oppu.setTags(post.getTags());
        oppu.setComment(post.getComment());
        oppu.setFeedback(post.isFeedback());
    }

    @Transactional
    public void deleteArticle(UUID userId, UUID oppuId) {
        validateUser(userId);
        Oppu oppu = oppuRepository.findById(oppuId).orElseThrow(() -> new OppuException(ErrorCode.OPPU_NO_ARTICLE));

        List<UUID> oldImageUUIDs = oppu.getImages();
        if(!Objects.equals(oppu.getUserId(), userId)) {
            throw new OppuException(ErrorCode.OPPU_UNAUTHORIZED);
        }

        oppuRepository.delete(oppu);
        imageService.deleteObjectsAfterCommit(oldImageUUIDs);
    }

    private void validateUser(UUID userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserException(ErrorCode.USER_LOAD_FAILED));
    }
}
