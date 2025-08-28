package com.ppu.ppu.review.domain;

import com.ppu.ppu.perfume.domain.Perfume;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="review")
@ToString
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "perfume_id", referencedColumnName = "id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Perfume perfume;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "is_liked")
    private boolean isLiked;

    @Column(name = "score")
    private int score;

    @Column(name = "wear_tested")
    private boolean wearTested;

    @Column(name = "tested_date")
    private LocalDateTime testedDate;

    @Column(name = "content", length = 2048)
    private String content;

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
