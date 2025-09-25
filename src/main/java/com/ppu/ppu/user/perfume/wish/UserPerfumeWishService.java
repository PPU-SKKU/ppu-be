package com.ppu.ppu.user.perfume.wish;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserPerfumeWishService {
    private UserPerfumeWishRepository userPerfumeWishRepository;

    @Transactional
    public void addPerfumeIdsByUserIdAndPerfumeIds(UUID userId, List<Integer> perfumeIds) {
        List<UserPerfumeWish> wish = perfumeIds
                .stream()
                .map(pid -> new UserPerfumeWish(new UserPerfumeWishId(userId, pid)))
                .toList();

        userPerfumeWishRepository.saveAll(wish);
    }

    public Optional<List<Integer>> findPerfumeIdsByUserId(UUID userId) {
        return userPerfumeWishRepository.findPerfumeIdsByUserId(userId);
    }

    @Transactional
    public int deleteByUserIdAndPerfumeIdIn(UUID userId, List<Integer> perfumeIds) {
        return userPerfumeWishRepository.deleteByUserIdAndPerfumeIdIn(userId, perfumeIds);
    }
}
