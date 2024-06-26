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

    @OneToOne
    @JoinColumn(name = "pet_info_id")
    private PetInfo petInfo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Users users;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "isLiked", nullable = false, columnDefinition = "bit(1) default 0")
    private boolean isLiked = false;

    @Builder
    public PetBoard(Long petBoardId, String title, String description, String petType, PetInfo petInfo, Status status, Users users, boolean isLiked) {
        this.petBoardId = petBoardId;
        this.title = title;
        this.description = description;
        this.petType = petType;
        this.petInfo = petInfo;
        this.status = status;
        this.users = users;
        this.isLiked = isLiked;
    }

    public void update(PetBoardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
        this.petInfo = requestDto.getPetInfo();
        this.petType = requestDto.getPetInfo().getPetType();
        this.status = Status.fromProcessState(requestDto.getPetInfo().getProcessState());
    }

    public void updateLike(boolean isLiked) {
        this.isLiked = isLiked;
    }
    
}
