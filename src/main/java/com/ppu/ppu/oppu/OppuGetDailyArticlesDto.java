package com.ppu.ppu.oppu;

import lombok.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OppuGetDailyArticlesDto {
    private LocalDate date;
    private List<OppuPerfumes> perfumeIds;
    private List<URI> images;
    private List<UUID> tags;
    private String comment;
}
