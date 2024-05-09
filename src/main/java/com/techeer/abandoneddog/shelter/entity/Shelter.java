package com.techeer.abandoneddog.shelter.entity;

import com.techeer.abandoneddog.animal.entity.PetInfo;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;



@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 다른 속성들 생략

    @OneToMany(mappedBy = "shelter")
    private List<PetInfo> petInfoList; // PetInfo 엔티티와의 관계

    private String careNm;
    private String careTel;
    private String careAddr;
}