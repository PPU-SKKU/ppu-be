package com.ppu.ppu.oppu.dto;

import com.ppu.ppu.oppu.domain.Oppu;
import com.ppu.ppu.perfume.dto.PerfumeResponseDto;
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
public class OppuDailyResponseDto {
    private List<OppuEntity> oppuEntities;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OppuEntity {
        private UUID id;
        private UUID userId;
        private LocalDate date;
        private List<OppuPerfumeEntity> perfumes;
        private String comment;
        private List<URL> images;
        private boolean feedback;
        private List<UUID> tagIds;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class OppuPerfumeEntity {
            private PerfumeResponseDto perfume;
            private int count;
        }

        public static OppuEntity fromEntity(Oppu oppu, List<URL> imageUrls, List<OppuPerfumeEntity> perfumes) {
            return OppuEntity.builder()
                    .id(oppu.getId())
                    .userId(oppu.getUserId())
                    .date(oppu.getDate())
                    .perfumes(perfumes)
                    .comment(oppu.getComment())
                    .images(imageUrls)
                    .feedback(oppu.isFeedback())
                    .tagIds(oppu.getTags())
                    .build();
        }
    }
}
