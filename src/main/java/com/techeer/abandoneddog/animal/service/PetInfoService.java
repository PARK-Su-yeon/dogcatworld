package com.techeer.abandoneddog.animal.service;

import com.techeer.abandoneddog.animal.Dto.PetInfoRequestDto;
import com.techeer.abandoneddog.animal.entity.PetInfo;
import com.techeer.abandoneddog.animal.repository.PetInfoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PetInfoService {


    private final PetInfoRepository petInfoRepository;

    //    private UserRepository userRepository;
    //    private final PostMapper postMapper;


    public PetInfo createPetInfo(PetInfoRequestDto dto) {
        return petInfoRepository.save(PetInfo.builder()
                .shelterId(dto.getShelterId())
                .petName(dto.getPetName())
                .petType(dto.getPetType())
                .breed(dto.getBreed())
                .gender(dto.getGender())
                .weight(dto.getWeight())

                .build());
    }


//        public List<PostResponseDto> pageList(Pageable pageable) {
//            Page<Post> postList = postRepository.findAll(pageable);
//            return postMapper.toDtoPageList(postList).getContent();
//        }

    public PetInfo getPetInfo(Long id) {
        return petInfoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("not found"));
    }

    public Object updatePetInfo(PetInfoRequestDto dto, long id) {

        PetInfo entity = petInfoRepository.findById(id).get();
        entity.update(dto);
        return entity;
    }


    public void deletePetInfo(Long id) {
        PetInfo entity = petInfoRepository.findById(id).get();
        petInfoRepository.deleteById(entity.getId());

    }
}
