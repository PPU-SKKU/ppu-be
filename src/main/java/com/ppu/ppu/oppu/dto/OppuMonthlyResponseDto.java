package com.ppu.ppu.oppu.dto;

import lombok.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OppuMonthlyResponseDto {
    private List<DateEntry> dates;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DateEntry {
        private int day;
        private List<TagEntry> tagIds;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class TagEntry {
            private UUID tagId;
            private int num;
        }
    }
}
