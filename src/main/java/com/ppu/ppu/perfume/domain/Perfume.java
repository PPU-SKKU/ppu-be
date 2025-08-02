package com.ppu.ppu.perfume.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name="perfume")
@ToString
public class Perfume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "brand_id")
    private int brandId;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "korean_name")
    private String koreanName;

    @Column(name = "image")
    private String image;
}
