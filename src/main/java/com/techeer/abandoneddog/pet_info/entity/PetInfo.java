package com.techeer.abandoneddog.pet_info.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "pet_info")
public class PetInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petInfoId;

    @Column(name = "adoption", nullable = false)
    @Enumerated(EnumType.STRING)
    private Adoption adoption;

    @Column(name = "pet_name", nullable = false)
    private String petName;

    @Column(name = "pet_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PetType petType;

    @Column(name = "adopt_count", nullable = false)
    private int adoptCount;

    @Column(name = "breed", nullable = false)
    private String breed;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "pet_weight", nullable = false)
    private float weight;

    @Builder
    public PetInfo(Long petInfoId, Adoption adoption, String petName, PetType petType, int adoptCount, String breed, Gender gender, float weight) {
        this.petInfoId = petInfoId;
        this.adoption = adoption;
        this.petName = petName;
        this.petType = petType;
        this.adoptCount = adoptCount;
        this.breed = breed;
        this.gender = gender;
        this.weight = weight;
    }
}
