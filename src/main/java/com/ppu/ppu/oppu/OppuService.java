package com.ppu.ppu.oppu;

import com.ppu.ppu.exception.ErrorCode;
import com.ppu.ppu.exception.domain.OppuException;
import com.ppu.ppu.exception.domain.UserException;
import com.ppu.ppu.oppu.domain.Oppu;
import com.ppu.ppu.oppu.domain.OppuRepository;
import com.ppu.ppu.oppu.dto.OppuDailyResponseDto;
import com.ppu.ppu.oppu.dto.OppuResponseDto;
import com.ppu.ppu.oppu.dto.OppuMonthlyResponseDto;
import com.ppu.ppu.oppu.dto.OppuCreateDto;
import com.ppu.ppu.oppu.tags.domain.OppuTagRepository;
import com.ppu.ppu.oppu.utils.DateParser;
import com.ppu.ppu.oppu.utils.MonthParser;
import com.ppu.ppu.perfume.PerfumeService;
import com.ppu.ppu.perfume.dto.PerfumeResponseDto;
import com.ppu.ppu.user.UserRepository;
import com.ppu.ppu.utils.image.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OppuService {
    private final UserRepository userRepository;
    private final OppuTagRepository oppuTagRepository;
    OppuRepository oppuRepository;
    ImageService imageService;
    PerfumeService perfumeService;

    @Transactional
    public void postArticle(UUID userId, OppuCreateDto post, List<MultipartFile> images) {
        validateUser(userId);

        validatePerfumeIds(post.getPerfumeIds());
        validateTags(post.getTagIds());

        List<UUID> imageUUIDs = imageService.uploadImages(userId, "ppubucket", "oppu", images);
        imageService.deleteObjectsOnRollback(imageUUIDs);

        Oppu oppu = OppuCreateDto.fromDto(userId, imageUUIDs, post);
        oppuRepository.save(oppu);
    }

    @Transactional(readOnly = true)
    public OppuResponseDto getArticle(UUID userId, UUID oppuId) {
        validateUser(userId);
        Oppu oppu = oppuRepository.findById(oppuId).orElseThrow(() -> new OppuException(ErrorCode.OPPU_NO_ARTICLE));

        List<URL> imageUrls = imageService.getDownloadUrls(oppu.getImages());
        return OppuResponseDto.fromEntity(oppu, imageUrls);
    }

    @Transactional
    public void putArticle(UUID userId, UUID oppuId, OppuCreateDto post, List<MultipartFile> images) {
        validateUser(userId);
        Oppu oppu = oppuRepository.findById(oppuId).orElseThrow(() -> new OppuException(ErrorCode.OPPU_NO_ARTICLE));

        if(!Objects.equals(oppu.getUserId(), userId)) {
            throw new OppuException(ErrorCode.OPPU_UNAUTHORIZED);
        }

        validatePerfumeIds(post.getPerfumeIds());
        validateTags(post.getTagIds());

        List<UUID> imageUUIDs = imageService.uploadImages(userId, "ppubucket", "oppu", images);
        List<UUID> oldImageUUIDs = oppu.getImages();

        imageService.deleteObjectsOnRollback(imageUUIDs);
        imageService.deleteObjectsAfterCommit(oldImageUUIDs);

        oppu.setDate(post.getDate());
        oppu.setPerfumes(post.getPerfumeIds());
        oppu.setImages(imageUUIDs);
        oppu.setTags(post.getTagIds());
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

    public OppuMonthlyResponseDto getMonthlyTags(UUID userId, String date) {
        validateUser(userId);
        YearMonth month = MonthParser.parseMonth(date);

        LocalDate start = month.atDay(1);
        LocalDate end = month.plusMonths(1).atDay(1);

        List<Oppu> rows = oppuRepository.findIntervalPosts(userId, start, end);

        Map<Integer, Map<UUID, Integer>> oppuMonthlyResponseMap = new TreeMap<>();
        for (Oppu oppu : rows) {
            int day = oppu.getDate().getDayOfMonth();

            Map<UUID, Integer> tagCountMap = oppuMonthlyResponseMap.computeIfAbsent(day, k -> new TreeMap<>());

            for(UUID tag : oppu.getTags()) {
                tagCountMap.merge(tag, 1, Integer::sum);
            }
        }

        List<OppuMonthlyResponseDto.DateEntry> dateEntries = new ArrayList<>();
        for(Map.Entry<Integer, Map<UUID, Integer>> entry : oppuMonthlyResponseMap.entrySet()) {
            int day = entry.getKey();
            Map<UUID, Integer> tagCountMap = entry.getValue();

            List<OppuMonthlyResponseDto.DateEntry.TagEntry> tagEntries = new ArrayList<>();
            for(Map.Entry<UUID, Integer> t : tagCountMap.entrySet()) {
                tagEntries.add(OppuMonthlyResponseDto.DateEntry.TagEntry.builder()
                        .tagId(t.getKey())
                        .num(t.getValue())
                        .build());
            }

            dateEntries.add(OppuMonthlyResponseDto.DateEntry.builder()
                    .day(day)
                    .tagIds(tagEntries)
                    .build());
        }

        return OppuMonthlyResponseDto.builder()
                .dates(dateEntries)
                .build();
    }

    public OppuDailyResponseDto getDailyArticles(UUID userId, String dateStr) {
        validateUser(userId);
        LocalDate date = DateParser.parseDate(dateStr);

        LocalDate start = date;
        LocalDate end = date.plusDays(1);

        List<Oppu> rows = oppuRepository.findIntervalPosts(userId, start, end);

        List<OppuDailyResponseDto.OppuEntity> oppuEntities = new ArrayList<>();
        for(Oppu row : rows) {
            List<URL> imageUrls = imageService.getDownloadUrls(row.getImages());
            List<OppuDailyResponseDto.OppuEntity.OppuPerfumeEntity> perfumes = row.getPerfumes().stream()
                    .map(perfume -> OppuDailyResponseDto.OppuEntity.OppuPerfumeEntity.builder()
                            .perfume(perfumeService.getPerfumeById(perfume.getPerfumeId()))
                            .count(perfume.getPerfumeNum())
                            .build()
                    )
                    .toList();

            oppuEntities.add(OppuDailyResponseDto.OppuEntity.fromEntity(row, imageUrls, perfumes));
        }

        return OppuDailyResponseDto.builder()
                .oppuEntities(oppuEntities)
                .build();
    }

    private void validatePerfumeIds(List<OppuPerfumes> perfumeIds) {
        if(perfumeIds == null) {
            throw new OppuException(ErrorCode.OPPU_NO_PERFUMES);
        }

        for(OppuPerfumes perfume : perfumeIds) {
            if(perfume.getPerfumeNum() <= 0) {
                throw new OppuException(ErrorCode.OPPU_PERFUME_COUNT_LEQ_ZERO);
            }

            // check is perfumeId valid
            perfumeService.getPerfumeById(perfume.getPerfumeId());
        }
    }

    private void validateTags(List<UUID> tagIds) {
        if(tagIds == null) {
            throw new OppuException(ErrorCode.OPPU_NO_TAGS);
        }

        for(UUID tagId : tagIds) {
            if(tagId == null) {
                throw new OppuException(ErrorCode.OPPU_NO_TAGS);
            }

            oppuTagRepository.findById(tagId)
                    .orElseThrow(() -> new OppuException(ErrorCode.OPPU_NO_TAGS));
        }
    }

    private void validateUser(UUID userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserException(ErrorCode.USER_LOAD_FAILED));
    }
}
