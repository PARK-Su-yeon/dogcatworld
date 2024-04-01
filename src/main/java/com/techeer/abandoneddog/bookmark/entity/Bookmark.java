package com.techeer.abandoneddog.bookmark.entity;

import com.techeer.abandoneddog.bookmark.dto.BookmarkRequestDto;
import com.techeer.abandoneddog.global.entity.BaseEntity;
import com.techeer.abandoneddog.pet_board.entity.PetBoard;
import com.techeer.abandoneddog.users.entity.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Builder
@NoArgsConstructor
@SQLDelete(sql = "UPDATE bookmark SET deleted = true WHERE bookmark_id = ?")
@Where(clause = "deleted = false")
@Table(name = "bookmark")
public class Bookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "petBoard_id")
    private PetBoard petBoard;



    public Bookmark(Long id, Users user, PetBoard petboard) {
        this.id = id;
        this.user = user;
        this.petBoard = petboard;
    }

    public void update(BookmarkRequestDto bookmarkRequestDto) {
    }
}
