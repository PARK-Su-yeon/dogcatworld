package com.techeer.abandoneddog.animal.entity;


import com.techeer.abandoneddog.animal.Dto.PetInfoRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PetInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private int shelterId;

    @Column(nullable = false)
    private String petName;

    @Column
    private PetType petType;

    private String breed;

    private Gender gender;

    private Float weight;


    public void update(PetInfoRequestDto dto) {

         shelterId=dto.getShelterId();
         petName=dto.getPetName();
         petType=dto.getPetType();
         breed=dto.getBreed();
        gender=dto.getGender();
        weight=dto.getWeight();

    }
}
