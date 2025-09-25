package com.ppu.ppu.oppu.tags.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "daily_record_tags")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OppuTag {
    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "color", nullable = false, columnDefinition = "char(6)")
    @Pattern(regexp = "^[0-9A-F]{6}$", message = "color should be 6 digit HEX(RRGGBB)")
    private String color;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Transient
    public String getColorHex() {
        return color == null ? null : "#" + color;
    }

    @PrePersist
    public void generateUUID() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }
}
