package com.ppu.ppu.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmailAndLoginType(String email, LoginType loginType);
    Optional<User> findByEmailAndLoginType(String email, LoginType loginType);
    Optional<User> findByNickname(String nickname);
    Optional<User> findById(UUID id);

    @Modifying
    @Query("UPDATE User u SET u.nickname = :nickname WHERE u.id = :id")
    int updateNicknameById(@Param("id") UUID id, @Param("nickname") String nickname);

    @Modifying
    @Query("UPDATE User u SET u.profileImage = :profileImage WHERE u.id = :id")
    int updateProfileImageById(@Param("id") UUID id, @Param("profileImage") UUID filePath);

    @Modifying
    @Query("DELETE from User u WHERE u.id = :id")
    int deleteUserById(@Param("id") UUID id);
}
