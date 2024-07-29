package com.techeer.abandoneddog.pet_board.dto;

import com.techeer.abandoneddog.animal.entity.PetInfo;
import com.techeer.abandoneddog.pet_board.entity.PetBoard;
import com.techeer.abandoneddog.pet_board.entity.Status;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PetBoardRequestDto {
	private String title;
	private String description;
	private PetInfo petInfo;
	private String username;
	private Long userId;

	public PetBoard toEntity() {
		return PetBoard.builder()
			.title(title)
			.description(description)
			//    .image(imageUrl)
			.petInfo(petInfo)
			.status(Status.fromProcessState(petInfo.getProcessState()))
			.build();
	}
}
