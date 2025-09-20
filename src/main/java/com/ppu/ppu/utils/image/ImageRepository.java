package com.ppu.ppu.utils.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<ImageEntity, UUID> {
    @Query("SELECT i.ownerId FROM ImageEntity i WHERE i.id = :id")
    Optional<UUID> findOwnerId(UUID id);
}
