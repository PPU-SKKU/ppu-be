package com.ppu.ppu.oppu;

import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    private List<UUID> tags;
    private String comment;
    private boolean feedback;

    public static Oppu fromDto(UUID userId, List<UUID> images, OppuPostArticleDto dto) {
        return Oppu.builder()
                .userId(userId)
                .date(dto.getDate())
                .perfumes(dto.getPerfumeIds())
                .images(images)
                .tags(dto.getTags())
                .comment(dto.getComment())
                .feedback(dto.isFeedback())
                .build();
    }
}
