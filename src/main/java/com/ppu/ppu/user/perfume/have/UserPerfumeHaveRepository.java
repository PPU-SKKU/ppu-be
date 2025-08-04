package com.ppu.ppu.user.perfume.have;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserPerfumeHaveRepository extends JpaRepository<UserPerfumeHave, UserPerfumeHaveId> {
    @Query("SELECT u.id.perfumeId FROM UserPerfumeHave u WHERE u.id.userId = :userId")
    Optional<List<UUID>> findPerfumeIdsByUserId(@Param("userId") UUID userId);

    @Modifying
    @Query("DELETE FROM UserPerfumeHave u WHERE u.id.userId = :userId and u.id.perfumeId in :perfumeIds")
    int deleteByUserIdAndPerfumeIdIn(@Param("userId") UUID userId, @Param("perfumeIds") List<UUID> perfumeIds);
}
