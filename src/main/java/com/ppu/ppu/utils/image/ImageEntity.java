package com.ppu.ppu.utils.image;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name="images")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ImageEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "owner_id")
    private UUID ownerId;

    @Column(name = "bucket", nullable = false, length = 255)
    private String bucket;

    @Column(name = "object_key", nullable = false, length = 255)
    private String objectKey;

    @Column(name = "content_type", nullable = false, length = 50)
    private String contentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ImageUploadStatus status;

    @Column(name = "width", nullable = false)
    private int width;

    @Column(name = "height", nullable = false)
    private int height;

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
        if(this.status == null) {
            this.status = ImageUploadStatus.NONE;
        }
    }
}
