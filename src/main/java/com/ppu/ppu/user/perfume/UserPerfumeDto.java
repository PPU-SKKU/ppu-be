package com.ppu.ppu.user.perfume;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UserPerfumeDto {
    private List<Integer> perfumeIds;
}
