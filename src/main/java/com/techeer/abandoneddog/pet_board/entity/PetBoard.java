package com.techeer.abandoneddog.pet_board.entity;

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
@Table(name = "pet_board")
public class PetBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petBoardId;

//    @Column(name = "pet_id", nullable = false)
//    private Long petId;
//
//    @Column(name = "member_id", nullable = false)
//    private Long memberId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "location")
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Builder
    public PetBoard(Long petBoardId, int petId, int memberId, String title, String description, String location, Status status) {
        this.petBoardId = petBoardId;
//        this.petId = petId;
//        this.memberId = memberId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.status = status;
    }

}
