package com.ppu.ppu.user.perfume.wish;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name="perfume_wish")
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserPerfumeWish {
    @EmbeddedId
    private UserPerfumeWishId id;
}
