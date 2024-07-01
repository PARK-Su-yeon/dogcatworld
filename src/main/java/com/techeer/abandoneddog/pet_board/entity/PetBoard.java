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

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "pet_type")
    private String petType;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "pet_info_id")
    private PetInfo petInfo;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "like_count", nullable = false, columnDefinition = "int default 0")
    private Integer likeCount = 0;


    @Builder
    public PetBoard(Long petBoardId, String title, String description, String petType, PetInfo petInfo, Status status, Users users) {
        this.petBoardId = petBoardId;
        this.title = title;
        this.description = description;
        this.petType = petType;
        this.petInfo = petInfo;
        this.status = status;
        this.users = users;
    }

    public void update(PetBoardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
        this.petInfo = requestDto.getPetInfo();
        this.petType = requestDto.getPetInfo().getPetType();
        this.status = Status.fromProcessState(requestDto.getPetInfo().getProcessState());
    }

    public void incleaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        } else {
            this.likeCount = 0;
        }
    }
}
