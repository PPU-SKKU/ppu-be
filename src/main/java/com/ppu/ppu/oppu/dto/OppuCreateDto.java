package com.ppu.ppu.oppu.dto;

import com.ppu.ppu.oppu.OppuPerfumes;
import com.ppu.ppu.oppu.domain.Oppu;
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
public class OppuCreateDto {
    @NotNull
    private LocalDate date;

    @NotNull
    private List<OppuPerfumes> perfumeIds;

    private List<UUID> tagIds;
    private String comment;
    private boolean feedback;

    public static Oppu fromDto(UUID userId, List<UUID> images, OppuCreateDto dto) {
        return Oppu.builder()
                .userId(userId)
                .date(dto.getDate())
                .perfumes(dto.getPerfumeIds())
                .images(images)
                .tags(dto.getTagIds())
                .comment(dto.getComment())
                .feedback(dto.isFeedback())
                .build();
    }
}
