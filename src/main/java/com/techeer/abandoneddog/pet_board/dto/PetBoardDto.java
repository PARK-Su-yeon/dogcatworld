package com.techeer.abandoneddog.pet_board.dto;

import com.techeer.abandoneddog.pet_board.entity.PetBoard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PetBoardDto {
	private Long petBoardId;
	private String title;
	private String popfile;

	public static PetBoardDto fromEntity(PetBoard petBoard) {
		return new PetBoardDto(
			petBoard.getPetBoardId(),
			petBoard.getTitle(),
			petBoard.getPetInfo() != null ? petBoard.getPetInfo().getPopfile() : null
		);
	}
}