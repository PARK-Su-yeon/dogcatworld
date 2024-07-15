package com.techeer.abandoneddog.image.entity;

import com.techeer.abandoneddog.animal.entity.PetInfo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
		url = imageUrl;
	}

	public void updateImg(String imageUrl) {
		this.url = imageUrl;
	}
}
