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
public class OppuGetArticleDto {
    private LocalDate date;
    private List<OppuPerfumes> perfumeIds;
    private List<URL> images;
    private List<UUID> tags;
    private String comment;
    private boolean feedback;

    public static OppuGetArticleDto fromEntity(Oppu oppu, List<URL> imageUrls) {
        return OppuGetArticleDto.builder()
                .date(oppu.getDate())
                .perfumeIds(oppu.getPerfumes())
                .images(imageUrls)
                .tags(oppu.getTags())
                .comment(oppu.getComment())
                .feedback(oppu.isFeedback())
                .build();
    }
}
