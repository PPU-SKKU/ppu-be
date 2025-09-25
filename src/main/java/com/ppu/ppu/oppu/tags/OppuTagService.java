package com.ppu.ppu.oppu.tags;

import com.ppu.ppu.exception.ErrorCode;
import com.ppu.ppu.exception.domain.OppuException;
import com.ppu.ppu.exception.domain.UserException;
import com.ppu.ppu.oppu.domain.OppuRepository;
import com.ppu.ppu.oppu.tags.domain.OppuTag;
import com.ppu.ppu.oppu.tags.domain.OppuTagRepository;
import com.ppu.ppu.oppu.tags.dto.OppuTagCreateDto;
import com.ppu.ppu.oppu.tags.dto.OppuTagsResponseDto;
import com.ppu.ppu.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OppuTagService {
    private final UserRepository userRepository;
    private final OppuRepository oppuRepository;
    private final OppuTagRepository oppuTagRepository;

    public void createTag(UUID userId, OppuTagCreateDto dto) {
        validateUser(userId);

        long tagCount = oppuTagRepository.countByUserId(userId);
        if(tagCount >= 5) {
            throw new OppuException(ErrorCode.OPPU_TAG_MAX_LIMIT);
        }

        OppuTag oppuTag = OppuTag.builder()
                .userId(userId)
                .name(dto.getName())
                .color(dto.getColor())
                .build();
        oppuTagRepository.save(oppuTag);
    }

    @Transactional
    public void replaceTag(UUID userId, UUID tagId, OppuTagCreateDto dto) {
        validateUser(userId);

        OppuTag oppuTag = oppuTagRepository.findById(tagId).orElseThrow(() -> new OppuException(ErrorCode.OPPU_TAG_NOT_FIND));

        if(!oppuTag.getUserId().equals(userId)) {
            throw new OppuException(ErrorCode.OPPU_TAG_UNAUTHORIZED);
        }

        oppuTag.setName(dto.getName());
        oppuTag.setColor(dto.getColor());
    }

    @Transactional
    public void deleteTag(UUID userId, UUID tagId) {
        validateUser(userId);

        OppuTag oppuTag = oppuTagRepository.findById(tagId).orElseThrow(() -> new OppuException(ErrorCode.OPPU_TAG_NOT_FIND));
        if(!oppuTag.getUserId().equals(userId)) {
            throw new OppuException(ErrorCode.OPPU_TAG_UNAUTHORIZED);
        }

        oppuRepository.deleteAllByUserIdAndTags(userId, tagId);
        oppuTagRepository.deleteById(tagId);
    }

    public OppuTagsResponseDto getTags(UUID userId) {
        validateUser(userId);

        List<OppuTag> oppuTags = oppuTagRepository.findAllByUserId(userId);
        List<OppuTagsResponseDto.Tag> dtoList = oppuTags.stream()
                .map(tag -> new OppuTagsResponseDto.Tag(
                        tag.getId(),
                        tag.getName(),
                        tag.getColorHex()
                ))
                .toList();

        return new OppuTagsResponseDto(dtoList);
    }

    private void validateUser(UUID userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserException(ErrorCode.USER_LOAD_FAILED));
    }
}
