package com.ppu.ppu.perfume.domain;

import jakarta.persistence.*;
import java.util.UUID;
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

    @OneToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Brand brand;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "korean_name")
    private String koreanName;

    @Column(name = "image")
    private UUID image;

    public Perfume() {
    }

    public Perfume(int id) {
        this.id = id;
    }
}
