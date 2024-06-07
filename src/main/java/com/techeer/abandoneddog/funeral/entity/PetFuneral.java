package com.techeer.abandoneddog.funeral.entity;

import com.techeer.abandoneddog.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE pet_funeral SET deleted = true WHERE pet_funeral_id = ?")
@Where(clause = "deleted = false")
@Entity
@Table(name = "pet_funeral")
public class PetFuneral extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_funeral_id", updatable = false)
    private Long id;

    @Column(name = "funeral_name")
    private String funeralName;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNum;

    @Column(name = "homepage")
    private String homepage;

    @Column(name = "region")
    @Enumerated(EnumType.STRING)
    private Region region;

    @Column(name = "image", columnDefinition = "LONGTEXT")
    private String image;

}
