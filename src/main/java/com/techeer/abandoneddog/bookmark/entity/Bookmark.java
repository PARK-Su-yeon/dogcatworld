package com.techeer.abandoneddog.bookmark.entity;

import com.techeer.abandoneddog.global.entity.BaseEntity;
import com.techeer.abandoneddog.pet_board.entity.PetBoard;
import com.techeer.abandoneddog.users.entity.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@Builder
@NoArgsConstructor
@SQLDelete(sql = "UPDATE bookmark SET deleted = true WHERE bookmark_id = ?")
@Table(name = "bookmark")
public class Bookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "petBoard_id")
    private PetBoard petBoard;

    public Bookmark(Long id, Users user, PetBoard petboard) {
        this.id = id;
        this.user = user;
        this.petBoard = petboard;
    }

}
