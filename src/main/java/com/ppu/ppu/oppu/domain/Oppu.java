package com.ppu.ppu.oppu.domain;

import com.ppu.ppu.oppu.OppuPerfumes;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="daily_record")
@Builder
@Setter
public class Oppu {
    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "perfumes", columnDefinition = "jsonb NOT NULL DEFAULT '[]'")
    private List<OppuPerfumes> perfumes = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "images", columnDefinition = "jsonb NOT NULL DEFAULT '[]'")
    private List<UUID> images = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "tags", columnDefinition = "jsonb NOT NULL DEFAULT '[]'")
    private List<UUID> tags = new ArrayList<>();

    @Column(name = "comment", length = 2048)
    private String comment;

    @Column(name = "feedback")
    private boolean feedback;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void generateUUID() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }
}
