package com.techeer.abandoneddog.pet_board.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.techeer.abandoneddog.animal.entity.PetInfo;
import com.techeer.abandoneddog.animal.repository.PetInfoRepository;
import com.techeer.abandoneddog.bookmark.repository.BookmarkRepository;
import com.techeer.abandoneddog.image.dto.ImageResizingDto;
import com.techeer.abandoneddog.image.entity.Image;
import com.techeer.abandoneddog.image.repository.ImageRepository;
import com.techeer.abandoneddog.pet_board.dto.PetBoardDetailResponseDto;
import com.techeer.abandoneddog.pet_board.dto.PetBoardRequestDto;
import com.techeer.abandoneddog.pet_board.dto.PetBoardResponseDto;
import com.techeer.abandoneddog.pet_board.entity.PetBoard;
import com.techeer.abandoneddog.pet_board.entity.Status;
import com.techeer.abandoneddog.pet_board.repository.PetBoardRepository;
import com.techeer.abandoneddog.s3.S3Service;
import com.techeer.abandoneddog.shelter.entity.Shelter;
import com.techeer.abandoneddog.shelter.repository.ShelterRepository;
import com.techeer.abandoneddog.users.entity.Users;
import com.techeer.abandoneddog.users.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class PetBoardService {
	private final PetBoardRepository petBoardRepository;
	private final PetInfoRepository petInfoRepository;
	private final ShelterRepository shelterRepository;
	private final UserRepository userRepository;
	private final BookmarkRepository bookmarkRepository;
	private final S3Service s3Service;
	private final ImageRepository imageRepository;

	@Transactional
	public Long createPetBoard(PetBoardRequestDto petBoardRequestDto, MultipartFile mainImage,
		List<MultipartFile> images) {
		try {
			PetInfo petInfo = petBoardRequestDto.getPetInfo();
			petInfo.setPublicApi(false);
			petInfo.setPetBoardStored(false);

			Users user = userRepository.findById(petBoardRequestDto.getUserId())
				.orElseThrow(
					() -> new EntityNotFoundException("User not found with id: " + petBoardRequestDto.getUserId()));

			Shelter shelter = petInfo.getShelter();
			if (shelter != null) {
				shelter = shelterRepository.save(shelter);
				String careNm = "[개인보호] " + user.getUsername();
				shelter.setCareNm(careNm);
			}
			petInfo.setShelter(shelter);

			//PetInfo savedPetInfo = petInfoRepository.save(petInfo);

			Status status = Status.fromProcessState(petInfo.getProcessState());

			// Save main image to S3 and set as popfile
			String mainImageUrl = null;
			if (mainImage != null && !mainImage.isEmpty()) {
				mainImageUrl = s3Service.saveFile(mainImage);
				petInfo.setPopfile(mainImageUrl);
				petInfoRepository.save(petInfo);
			}
			log.info(String.valueOf(petInfo.getWeight()));

			// Save other images to S3 and Image repository
			List<Image> imageEntities = new ArrayList<>();
			for (MultipartFile file : images) {
				String fileUrl = s3Service.saveFile(file);
				Image image = Image.builder()
					.url(fileUrl)
					.petInfo(petInfo)
					.build();
				imageEntities.add(image);
			}
			imageRepository.saveAll(imageEntities);

			PetBoard newPetBoard = PetBoard.builder()
				.title("[" + petInfo.getKindCd() + "]" + String.valueOf(petBoardRequestDto.getTitle()))
				.description(petInfo.getSpecialMark())
				.petInfo(petInfo)
				.petType(petInfo.getPetType())
				.status(status)
				.users(user) // user 설정
				.build();

			newPetBoard.setUsers(user);
			PetBoard savedPetBoard = petBoardRepository.save(newPetBoard);

			// Update PetInfo to indicate it has a PetBoard
			petInfo.setPetBoardStored(true);
			petInfoRepository.save(petInfo);

			return savedPetBoard.getPetBoardId();
		} catch (DataIntegrityViolationException e) {
			throw new RuntimeException("Data integrity violation occurred while creating PetBoard", e);
		} catch (EntityNotFoundException e) {
			throw new RuntimeException("Entity not found while creating PetBoard", e);
		} catch (Exception e) {
			throw new RuntimeException("An unexpected error occurred while creating PetBoard", e);
		}
	}

	@Transactional
	public PetBoardResponseDto updatePetBoard(Long petBoardId, PetBoardRequestDto requestDto, MultipartFile mainImage,
		List<MultipartFile> images) {
		PetBoard petBoard = petBoardRepository.findById(petBoardId)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + petBoardId));

		petBoard.update(requestDto);
		PetInfo petInfo = petBoard.getPetInfo();
		petInfo.update(requestDto.getPetInfo());

		//추후  생성코드과 합쳐서 함수로 묶기

		if (mainImage != null && !mainImage.isEmpty()) {
			String mainImageUrl = s3Service.saveFile(mainImage);
			petInfo.setPopfile(mainImageUrl);
		}

		// 추가 이미지 처리
		List<Image> imageUrls = new ArrayList<>();
		for (MultipartFile image : images) {
			if (image != null && !image.isEmpty()) {
				String imageUrl = s3Service.saveFile(image);
				imageUrls.add(new Image(imageUrl));
			}
		}
		petInfo.updateImages(imageUrls); // 이미지 업데이트 로직 추가 필요

		petInfoRepository.save(petInfo);

		return PetBoardResponseDto.fromEntity(petBoard);
	}

	@Transactional
	public PetBoardDetailResponseDto getPetBoard(Long petBoardId, Long userId) {

		Boolean isLiked;

		PetBoard petBoard = petBoardRepository.findById(petBoardId)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + petBoardId));

		if (!(bookmarkRepository.existsByPetBoardAndUserIdAndIsDeletedFalse(petBoard, userId)) || userId.equals("-1")) {
			isLiked = false;
		} else {
			isLiked = true;
		}

		return PetBoardDetailResponseDto.fromEntity(petBoard, isLiked);
	}

	public Page<PetBoardResponseDto> getPetBoardsByPetType(String petType, Pageable pageable) {
		Page<PetBoard> petBoardPage = petBoardRepository.findByPetInfoPetTypeAndStatus(petType,
			Status.Awaiting_adoption, pageable);
		return petBoardPage.map(PetBoardResponseDto::fromEntity);
	}

	//필터링으로 검색
	public Page<PetBoardResponseDto> searchPetBoards(String categories, Status status, Integer minYear, Integer maxYear,
		String title, Boolean isYoung, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		if (minYear == null) {
			minYear = 0; // 혹은 0과 같이 적절한 기본값
		}
		if (maxYear == null) {
			maxYear = 50; // 혹은 적절한 기본값
		}
		Page<PetBoard> petBoards = petBoardRepository.searchPetBoards(categories, status, minYear, maxYear, title,
			isYoung, pageable);

		return petBoards.map(PetBoardResponseDto::fromEntity);
	}

	//    @Transactional

	//    public Page<PetBoardResponseDto> getPetBoards(Pageable pageable) {
	//        Page<PetBoard> petBoardPage = petBoardRepository.findAll(pageable);
	//        log.info("Retrieved pet boards: {}", petBoardPage.getContent());
	//        return petBoardPage.map(PetBoardResponseDto::fromEntity);
	//    }
	public Page<PetBoardResponseDto> getPetBoards(Pageable pageable) {
		Page<PetBoard> petBoardPage = petBoardRepository.findAll(pageable);
		return petBoardPage.map(PetBoardResponseDto::fromEntity);
	}

	@Transactional
	public void deletePetBoard(Long petBoardId) {
		PetBoard petBoard = petBoardRepository.findById(petBoardId)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + petBoardId));

		petBoardRepository.delete(petBoard);
	}

	//    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
	//    public void deleteOldPetInfo() {
	//        LocalDateTime tenDaysAgo = LocalDateTime.now().minus(10, ChronoUnit.DAYS);
	//        List<PetInfo> oldPetInfos = petBoardRepository.findByCreatedAtBefore(tenDaysAgo);
	//        petBoardRepository.deleteAll(oldPetInfos);
	//    }

	@Scheduled(fixedRate = 600000)
	@Transactional
	public void syncPetBoardFromPetInfo() {

		List<PetInfo> petInfos = petInfoRepository.findByPetBoardStoredFalse();
		for (PetInfo petInfo : petInfos) {
			Status status = Status.fromProcessState(petInfo.getProcessState());

			PetBoard newPetBoard = PetBoard.builder()
				.title("[" + petInfo.getKindCd() + "]" + String.valueOf(petInfo.getDesertionNo()))
				.description(petInfo.getSpecialMark())
				.petInfo(petInfo)
				.petType(petInfo.getPetType())
				.status(status)
				.build();
			petBoardRepository.save(newPetBoard);
			petInfo.setPetBoardStored(true); // PetBoard에 저장되었음을 표시
			petInfoRepository.save(petInfo); // PetInfo 엔티티 업데이트

		}
	}

	@Transactional
	public Page<PetBoardResponseDto> getMyPetBoard(Long userId, Pageable pageable) {
		Page<PetBoard> petBoardPage = petBoardRepository.findPetBoardByUsersId(userId, pageable);
		return petBoardPage.map(PetBoardResponseDto::fromEntity);
	}

	@Transactional
	public void updateResizingImg(ImageResizingDto dto) {
		Optional<PetInfo> petInfo = petInfoRepository.findPetInfoByPopfile(dto.getOriginalUrl());
		Optional<Image> image = imageRepository.findByUrl(dto.getOriginalUrl());

		if (petInfo.isPresent()) {
			petInfo.get().updatePopfile(dto.getResizedUrl());
			petInfoRepository.save(petInfo.get());
		}
		if (image.isPresent()) {
			image.get().updateImg(dto.getResizedUrl());
			imageRepository.save(image.get());
		}
	}
}
