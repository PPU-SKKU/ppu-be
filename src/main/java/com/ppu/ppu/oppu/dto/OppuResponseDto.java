package com.ppu.ppu.oppu.dto;

import com.ppu.ppu.oppu.OppuPerfumes;
import com.ppu.ppu.oppu.domain.Oppu;
import lombok.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OppuResponseDto {
    private UUID id;
    private UUID userId;
    private LocalDate date;
    private List<OppuPerfumes> perfume;
    private List<URL> image;
    private List<UUID> tagId;
    private String comment;
    private boolean feedback;

    public static OppuResponseDto fromEntity(Oppu oppu, List<URL> imageUrls) {
        return OppuResponseDto.builder()
                .id(oppu.getId())
                .userId(oppu.getUserId())
                .date(oppu.getDate())
                .perfume(oppu.getPerfumes())
                .image(imageUrls)
                .tagId(oppu.getTags())
                .comment(oppu.getComment())
                .feedback(oppu.isFeedback())
                .build();
    }
}
