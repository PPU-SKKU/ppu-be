package com.ppu.ppu.oppu;

import com.ppu.ppu.perfume.domain.Perfume;
import com.ppu.ppu.review.domain.Review;
import com.ppu.ppu.review.dto.ReviewCreateDto;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OppuPostArticleDto {
    @NotNull
    private LocalDate date;

    @NotNull
    private List<OppuPerfumes> perfumeIds;

    private List<MultipartFile> images;
    private List<UUID> tags;
    private String comment;

    @NotNull
    private boolean feedback;

    public static Oppu fromDto(UUID userId, OppuPostArticleDto dto) {
        return new Oppu().builder()
                .userId(userId)
                .date(dto.getDate())
                .perfumes(dto.getPerfumeIds())
                .images(dto.getImages())
                .tags(dto.getTags())
                .comment(dto.getComment())
                .feedback(dto.getFeedback())
                .build();
    }
}
