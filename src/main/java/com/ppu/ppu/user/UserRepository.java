package com.ppu.ppu.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
    Optional<User> findById(String id);
//    Optional<User>

    @Modifying
    @Query("UPDATE User u SET u.nickname = :nickname WHERE u.id = :id")
    int updateNicknameById(@Param("id") String id, @Param("nickname") String nickname);

    @Modifying
    @Query("UPDATE User u SET u.profileImage = :profileImage WHERE u.id = :id")
    int updateProfileImageById(@Param("id") String id, @Param("profileImage") String filePath);
}
