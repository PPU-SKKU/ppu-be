package com.ppu.ppu.oppu;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class OppuPerfumes {
    @Column(name = "perfume_id")
    private int perfumeId;

    @Column(name = "perfume_num")
    private int perfumeNum;
}
