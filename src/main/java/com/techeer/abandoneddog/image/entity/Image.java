package com.techeer.abandoneddog.image.entity;

import com.techeer.abandoneddog.animal.entity.PetInfo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @ManyToOne
    @JoinColumn(name = "pet_info_id")
    private PetInfo petInfo;

    public Image(String imageUrl) {
        url=imageUrl;
    }
}
