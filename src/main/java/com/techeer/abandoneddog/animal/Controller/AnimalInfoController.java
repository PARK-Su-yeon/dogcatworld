package com.techeer.abandoneddog.animal.Controller;

import com.techeer.abandoneddog.animal.entity.PetInfo;
import com.techeer.abandoneddog.animal.service.PetInfoService;
import org.springframework.data.domain.Page;
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


    @GetMapping("/petinfos")
    public ResponseEntity<Page<PetInfo>> getAllPetInfos(
            @RequestParam(name="page", required = false, defaultValue = "0") int page,
            @RequestParam(name="size",required = false, defaultValue = "10") int size) {
        try {
            Page<PetInfo> petInfos = petInfoService.getAllPetInfos(page, size);
            return ResponseEntity.ok(petInfos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/pet_info/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id) {

        try {

            return ResponseEntity.status(HttpStatus.OK).body( petInfoService.getPetInfo(id));


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("펫정보 조회에 실패하였습니다.");
        }

    }

//    @GetMapping("/update")
//    public ResponseEntity<String> updatePetInfo() {
//        try {
//
//            petInfoService.updatePetInfoDaily();
//            petInfoService.getAllAndSaveInfo("417000");
//            petInfoService.getAllAndSaveInfo("422400");
//
//            return ResponseEntity.ok("Pet information updated successfully.");
//        } catch (Exception e) {
////            log.error("Error updating pet information: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update pet information.");
//        }
//    }




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
