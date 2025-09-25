package com.ppu.ppu.oppu.tags.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OppuTagRepository extends JpaRepository<OppuTag, UUID> {
    List<OppuTag> findAllByUserId(UUID userId);

    long countByUserId(UUID userId);
}
