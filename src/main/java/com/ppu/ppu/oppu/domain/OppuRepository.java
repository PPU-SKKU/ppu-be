package com.ppu.ppu.oppu.domain;

import com.ppu.ppu.oppu.dto.OppuMonthlyResponseDto;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface OppuRepository extends JpaRepository<Oppu, UUID> {
    @Modifying
    @Query(value = """
        DELETE FROM daily_record d
        WHERE d.user_id = :userId
        AND EXISTS(
            SELECT 1
            FROM jsonb_array_elements_text(d:tags) AS t(tag)
            WHERE t.tag::uuid = :tagId
        )
        """, nativeQuery = true)
    void deleteAllByUserIdAndTags(@Param("user_id") UUID userId, @Param("tagId") UUID tagId);

    @Query(value = """
            SELECT *
            FROM daily_record d
            WHERE d.user_id = :userId
                AND d.date >= :startDate
                AND d.date < :endDate
            ORDER BY d.date
        """, nativeQuery = true)
    List<Oppu> findIntervalPosts(
            @Param("userId") UUID userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
