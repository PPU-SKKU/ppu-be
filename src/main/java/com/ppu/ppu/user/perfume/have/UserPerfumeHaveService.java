package com.ppu.ppu.user.perfume.have;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserPerfumeHaveService {
    private UserPerfumeHaveRepository userPerfumeHaveRepository;

    @Transactional
    public void addPerfumeIdsByUserIdAndPerfumeIds(UUID userId, List<UUID> perfumeIds) {
        List<UserPerfumeHave> have = perfumeIds
                .stream()
                .map(pid -> new UserPerfumeHave(new UserPerfumeHaveId(userId, pid)))
                .toList();

        userPerfumeHaveRepository.saveAll(have);
    }

    public Optional<List<UUID>> findPerfumeIdsByUserId(UUID userId) {
        return userPerfumeHaveRepository.findPerfumeIdsByUserId(userId);
    }

    @Transactional
    public int deleteByUserIdAndPerfumeIdIn(UUID userId, List<UUID> perfumeIds) {
        return userPerfumeHaveRepository.deleteByUserIdAndPerfumeIdIn(userId, perfumeIds);
    }
}
