package com.ppu.ppu.oppu;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OppuPerfumes {
    private int perfumeId;
    private int perfumeNum;
}
