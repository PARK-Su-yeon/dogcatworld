package com.techeer.abandoneddog.pet_board.entity;

import com.techeer.abandoneddog.animal.entity.PetInfo;
import com.techeer.abandoneddog.global.entity.BaseEntity;
import com.techeer.abandoneddog.pet_board.dto.PetBoardRequestDto;
import com.techeer.abandoneddog.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE pet_board SET deleted = true WHERE pet_board_id = ?")
@Where(clause = "deleted = false")
@Table(name = "pet_board")
public class PetBoard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_board_id", updatable = false)
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

    @OneToOne
    @JoinColumn(name = "pet_info_id")
    private PetInfo petInfo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Users users;



    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Builder
    public PetBoard(Long petBoardId, int petId, int memberId, String title, String description, PetInfo petInfo, Status status) {
        this.petBoardId = petBoardId;
//        this.petId = petId;
//        this.memberId = memberId;
        this.title = title;
        this.description = description;
        this.petInfo = petInfo;
        this.status = status;
    }

    public void update(PetBoardRequestDto requestDto) {
//        this.petId = requestDto.getPetId();
//        this.memberId = requestDto.getMemberId();
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
        this.petInfo = requestDto.getPetInfo();
        this.status = requestDto.getStatus();
    }

}
