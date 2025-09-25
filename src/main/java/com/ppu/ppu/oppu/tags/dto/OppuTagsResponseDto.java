package com.ppu.ppu.oppu.tags.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OppuTagsResponseDto {
    private List<Tag> tags;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Tag {
        private UUID id;
        private String name;
        private String color;
    }
}
