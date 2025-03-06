package com.ppu.ppu.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "password")
    private String password;

    @Column(name = "name", length = 50)
    private String name;
    
    @Column(name = "nickname", length = 10)
    private String nickname;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "email")
    private String email;

    @Column(name = "birth")
    private LocalDate birth;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public void printEntity(){
        System.out.println("email: " + this.email);
        System.out.println("id: " + this.id);
        System.out.println("password: " + this.password);
        System.out.println("name: " + this.name);
        System.out.println("nickname: " + this.nickname);
        System.out.println("profileImage: " + this.profileImage);
        System.out.println("createdAt: " + this.createdAt);
    }

    // gender, type
}
