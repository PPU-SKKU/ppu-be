package com.ppu.ppu.oppu.tags.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OppuTagCreateDto {
    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    private String color;
}
