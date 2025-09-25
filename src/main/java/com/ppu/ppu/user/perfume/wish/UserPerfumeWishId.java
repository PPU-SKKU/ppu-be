package com.ppu.ppu.user.perfume.wish;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserPerfumeWishId implements Serializable {
    @Column(name="user_id", nullable = false)
    private UUID userId;

    @Column(name="perfume_id", nullable = false)
    private Integer perfumeId;
}
