package com.techeer.abandoneddog.pet_board.service;

import com.techeer.abandoneddog.animal.entity.PetInfo;
import com.techeer.abandoneddog.animal.repository.PetInfoRepository;
import com.techeer.abandoneddog.image.entity.Image;
import com.techeer.abandoneddog.image.repository.ImageRepository;
import com.techeer.abandoneddog.pet_board.dto.PetBoardFilterRequest;
import com.techeer.abandoneddog.pet_board.dto.PetBoardRequestDto;
import com.techeer.abandoneddog.pet_board.dto.PetBoardResponseDto;
import com.techeer.abandoneddog.pet_board.entity.PetBoard;
import com.techeer.abandoneddog.pet_board.entity.Status;
import com.techeer.abandoneddog.pet_board.repository.PetBoardRepository;
import com.techeer.abandoneddog.s3.S3Service;
import com.techeer.abandoneddog.shelter.entity.Shelter;
import com.techeer.abandoneddog.shelter.repository.ShelterRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class PetBoardService {
    private final PetBoardRepository petBoardRepository;
    private final PetInfoRepository petInfoRepository;
    private final ShelterRepository shelterRepository;
    private final S3Service s3Service;
    private final ImageRepository imageRepository;

    @Transactional
    public String createPetBoard(PetBoardRequestDto petBoardRequestDto, MultipartFile mainImage, List<MultipartFile> images) {
        try {
            PetInfo petInfo = petBoardRequestDto.getPetInfo();
            petInfo = PetInfo.builder()
                    .desertionNo(petInfo.getDesertionNo())
                    .filename(petInfo.getFilename())
                    .happenDt(petInfo.getHappenDt())
                    .happenPlace(petInfo.getHappenPlace())
                    .petType(petInfo.getPetType())
                    .kindCd(petInfo.getKindCd())
                    .colorCd(petInfo.getColorCd())
                    .age(petInfo.getAge())
                    .weight(petInfo.getWeight())
                    .noticeNo(petInfo.getNoticeNo())
                    .noticeSdt(petInfo.getNoticeSdt())
                    .noticeEdt(petInfo.getNoticeEdt())
                    .processState(petInfo.getProcessState())
                    .sexCd(petInfo.getSexCd())
                    .neuterYn(petInfo.getNeuterYn())
                    .specialMark(petInfo.getSpecialMark())
                    .isPublicApi(false)
                    .petBoardStored(false)
                    .shelter(petInfo.getShelter() != null ? shelterRepository.save(petInfo.getShelter()) : null)
                    .orgNm(petInfo.getOrgNm())
                    .chargeNm(petInfo.getChargeNm())
                    .officetel(petInfo.getOfficetel())
                    .noticeComment(petInfo.getNoticeComment())
                    .isYoung(petInfo.isYoung())
                    .build();

            PetInfo savedPetInfo = petInfoRepository.save(petInfo);

            // Save main image to S3 and set as popfile
            String mainImageUrl = null;
            if (mainImage != null && !mainImage.isEmpty()) {
                mainImageUrl = s3Service.saveFile(mainImage);
                savedPetInfo = savedPetInfo.builder().popfile(mainImageUrl).build();
                petInfoRepository.save(savedPetInfo);
            }

            // Save other images to S3 and Image repository
            List<Image> imageEntities = new ArrayList<>();
            for (MultipartFile file : images) {
                String fileUrl = s3Service.saveFile(file);
                Image image = Image.builder()
                        .url(fileUrl)
                        .petInfo(savedPetInfo)
                        .build();
                imageEntities.add(image);
            }
            imageRepository.saveAll(imageEntities);

            // Build PetBoard
//            List<String> imageUrls = new ArrayList<>();
//            imageUrls.add(mainImageUrl); // Add main image URL
//
//            for (Image imageEntity : imageEntities) {
//                imageUrls.add(imageEntity.getUrl()); // Add other images' URLs
//            }

            PetBoard newPetBoard = PetBoard.builder()
                    .title("[" + savedPetInfo.getKindCd() + "]")
                    .description(savedPetInfo.getSpecialMark())
                    .petInfo(savedPetInfo)
                    .petType(savedPetInfo.getPetType())
                    .status(Status.fromProcessState(savedPetInfo.getProcessState()))
                    .build();

            petBoardRepository.save(newPetBoard);

            // Update PetInfo to indicate it has a PetBoard
            savedPetInfo = savedPetInfo.builder().petBoardStored(true).build();
            petInfoRepository.save(savedPetInfo);

            return mainImageUrl;
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Data integrity violation occurred while creating PetBoard", e);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException("Entity not found while creating PetBoard", e);
        } catch (Exception e) {
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
    public PetBoardResponseDto getPetBoard(Long petBoardId) {
        PetBoard petBoard = petBoardRepository.findById(petBoardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다. id=" + petBoardId));
        return PetBoardResponseDto.fromEntity(petBoard);
    }

    public Page<PetBoardResponseDto> getPetBoardsByPetType(String petType, Pageable pageable) {
        Page<PetBoard> petBoardPage = petBoardRepository.findByPetInfoPetTypeAndStatus(petType, Status.Awaiting_adoption, pageable);
        return petBoardPage.map(PetBoardResponseDto::fromEntity);
    }



//필터링으로 검색
    public Page<PetBoardResponseDto> searchPetBoards(String categories, Status status, int minYear, int maxYear, String title,boolean isYoung, int page, int size) {
            Pageable pageable = PageRequest.of(page, size);
            Page<PetBoard> petBoards = petBoardRepository.searchPetBoards(categories, status, minYear, maxYear, title,isYoung, pageable);

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
        PetInfo petInfo = petBoard.getPetInfo();

        petInfoRepository.delete(petInfo);
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
        public Page<PetBoardResponseDto> getMyPetBoard (Long userId, Pageable pageable){
            Page<PetBoard> petBoardPage = petBoardRepository.findPetBoardByUsersId(userId, pageable);
            return petBoardPage.map(PetBoardResponseDto::fromEntity);
        }

}
