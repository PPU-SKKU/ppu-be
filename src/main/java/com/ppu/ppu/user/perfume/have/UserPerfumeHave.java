package com.ppu.ppu.user.perfume.have;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name="perfume_have")
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserPerfumeHave {
    @EmbeddedId
    private UserPerfumeHaveId id;
}
