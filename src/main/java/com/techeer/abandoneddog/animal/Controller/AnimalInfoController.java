package com.techeer.abandoneddog.animal.Controller;

import com.techeer.abandoneddog.animal.Dto.PetInfoRequestDto;
import com.techeer.abandoneddog.animal.service.PetInfoService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/")
@RestController
public class AnimalInfoController {

    private final PetInfoService petInfoService;


    public AnimalInfoController(PetInfoService petInfoService) {

        this.petInfoService = petInfoService;
    }



    @PostMapping("/pet_info")
    public ResponseEntity<?> posting(@RequestBody PetInfoRequestDto dto) {


        try {
            return ResponseEntity.ok(petInfoService.createPetInfo(dto));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("게시물 작성에 실패하였습니다.");
        }
    }


//    @GetMapping("/posts")
//    public ResponseEntity<?> postList(@PageableDefault(size = 10) final Pageable pageable) {
//        return ResponseEntity.ok( postservice.pageList(pageable));
//
//    }

    @GetMapping("/pet_info/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {

        try {

            return ResponseEntity.status(HttpStatus.OK).body( petInfoService.getPetInfo(id));


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("펫정보 조회에 실패하였습니다.");
        }



    

    }

    @PostMapping("/pet_info/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id,@RequestBody  PetInfoRequestDto dto) {

        try {
            petInfoService.updatePetInfo(dto,id);
            return ResponseEntity.status(HttpStatus.OK).body("펫정보 수정에 성공하였습니다.");


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("펫정보 수정에 실패하였습니다.");
        }



    }


    @DeleteMapping("/pet_info/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        try {
            petInfoService.deletePetInfo(id);
            return ResponseEntity.status(HttpStatus.OK).body("펫정보 삭제에 성공하였습니다.");


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("정보 삭제에 실패하였습니다.");
        }

    }

}
