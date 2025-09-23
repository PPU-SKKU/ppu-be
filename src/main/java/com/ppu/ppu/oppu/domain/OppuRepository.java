package com.ppu.ppu.oppu.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OppuRepository extends JpaRepository<Oppu, UUID> {

}
