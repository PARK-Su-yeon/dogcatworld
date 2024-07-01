package com.techeer.abandoneddog.pet_board.service;

import com.techeer.abandoneddog.animal.entity.PetInfo;
import com.techeer.abandoneddog.animal.repository.PetInfoRepository;
import com.techeer.abandoneddog.bookmark.repository.BookmarkRepository;
import com.techeer.abandoneddog.pet_board.dto.PetBoardDetailResponseDto;
import com.techeer.abandoneddog.pet_board.dto.PetBoardRequestDto;
import com.techeer.abandoneddog.pet_board.dto.PetBoardResponseDto;
import com.techeer.abandoneddog.pet_board.entity.PetBoard;
import com.techeer.abandoneddog.pet_board.entity.Status;
import com.techeer.abandoneddog.pet_board.repository.PetBoardRepository;
import com.techeer.abandoneddog.shelter.entity.Shelter;
import com.techeer.abandoneddog.shelter.repository.ShelterRepository;
import com.techeer.abandoneddog.users.entity.Users;
import com.techeer.abandoneddog.users.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class PetBoardService {
    private final PetBoardRepository petBoardRepository;
    private final PetInfoRepository petInfoRepository;
    private final ShelterRepository shelterRepository;
    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public Long createPetBoard(PetBoardRequestDto petBoardRequestDto) {
        try {
            PetInfo petInfo = petBoardRequestDto.getPetInfo();
            petInfo.setPublicApi(false);
            petInfo.setPetBoardStored(false);

            Shelter shelter = petInfo.getShelter();
            if (shelter != null) {
                shelter = shelterRepository.save(shelter);
            }

            petInfo.setShelter(shelter);

            PetInfo savedPetInfo = petInfoRepository.save(petInfo);

            Status status = Status.fromProcessState(savedPetInfo.getProcessState());

            Users user = userRepository.findById(petBoardRequestDto.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + petBoardRequestDto.getUserId()));

            PetBoard newPetBoard = PetBoard.builder()

                    .title("[" + savedPetInfo.getKindCd() + "]" + String.valueOf(petBoardRequestDto.getTitle()))
                    .description(savedPetInfo.getSpecialMark())
                    .petInfo(savedPetInfo)
                    .petType(savedPetInfo.getPetType())
                    .status(status)
                    .users(user) // user 설정
                    .build();
            PetBoard savedPetBoard = petBoardRepository.save(newPetBoard);

            savedPetInfo.setPetBoardStored(true);
            petInfoRepository.save(savedPetInfo);

            return savedPetBoard.getPetBoardId();
        } catch (DataIntegrityViolationException e) {
            // 데이터 무결성 위반 예외 처리
            throw new RuntimeException("Data integrity violation occurred while creating PetBoard", e);
        } catch (EntityNotFoundException e) {
            // 엔티티를 찾을 수 없는 예외 처리
            throw new RuntimeException("Entity not found while creating PetBoard", e);
        } catch (Exception e) {
            // 일반 예외 처리
            throw new RuntimeException("An unexpected error occurred while creating PetBoard", e);
        }
    }

    @Transactional
    public PetBoardResponseDto updatePetBoard(Long petBoardId, PetBoardRequestDto requestDto) {
        PetBoard petBoard = petBoardRepository.findById(petBoardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + petBoardId));

        petBoard.update(requestDto);
        PetInfo petInfo = petBoard.getPetInfo();
        petInfo.update(requestDto.getPetInfo());


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
        Page<PetBoard> petBoardPage = petBoardRepository.findByPetInfoPetTypeAndStatus(petType, Status.Awaiting_adoption, pageable);
        return petBoardPage.map(PetBoardResponseDto::fromEntity);
    }


    //필터링으로 검색
    public Page<PetBoardResponseDto> searchPetBoards(String categories, Status status, int minYear, int maxYear, String title, boolean isYoung, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PetBoard> petBoards = petBoardRepository.searchPetBoards(categories, status, minYear, maxYear, title, isYoung, pageable);

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

}
