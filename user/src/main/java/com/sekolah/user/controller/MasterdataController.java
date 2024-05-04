package com.sekolah.user.controller;

import com.sekolah.user.dto.CandidateRequestDTO;
import com.sekolah.user.dto.GenericResponseDTO;
import com.sekolah.user.event.OutgoingMessageEvent;
import com.sekolah.user.rest.MasterdataRest;
import com.sekolah.user.service.UserService;
import com.sekolah.user.util.CheckUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/masterdata")
public class MasterdataController {

    @Autowired
    UserService userService;

    @Autowired
    MasterdataRest masterdataRest;

    @Autowired
    OutgoingMessageEvent outgoingMessageEvent;

    //fitur pertama dan kedua
    //Create or Update user untuk melakukan pendaftaran sekolah
    // (satu user hanya boleh daftar 1x bisa diupdate jika sudah daftar menggunakan api yang sama)
    @PostMapping("/save")
    public ResponseEntity<Object> saveCandidate(@RequestBody CandidateRequestDTO requestDTO,
                                               @RequestHeader(required = false, value = "username") String username,
                                               @RequestHeader(required = false, value = "token") String token) {
        requestDTO.setCreatedBy(username);
        ResponseEntity<Object> responseToken = userService.checkToken(username,token);
        if(responseToken == null) {
            CandidateRequestDTO candidateRequestDTO = masterdataRest.findCandidate(username,token);

            if(candidateRequestDTO == null){
                String nama = requestDTO.getNama();
                String alamat = requestDTO.getAlamat();
                String tempatLahir = requestDTO.getTempatLahir();
                String tanggalLahir = requestDTO.getTanggalLahir();
                String nomorKartuKeluarga = requestDTO.getNomorKartuKeluarga();
                if (CheckUtils.IsNullOrEmpty(nama) || CheckUtils.IsNullOrEmpty(alamat) || CheckUtils.IsNullOrEmpty(tempatLahir) || CheckUtils.IsNullOrEmpty(tanggalLahir) || CheckUtils.IsNullOrEmpty(nomorKartuKeluarga)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponseDTO().errorResponse("Data cannot be empty"));
                }
            }
            try {
                outgoingMessageEvent. candidateSaveRequest(requestDTO);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericResponseDTO().errorResponse(e.getMessage()));
            }
            return ResponseEntity.ok().body(new GenericResponseDTO().successResponse("Data berhasil disimpan"));
        } else {
            return responseToken;
        }
    }

    //fitur ketiga
    //delete khusus admin
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> DeleteCandidateById(@PathVariable("id") Integer id,
                                                 @RequestHeader(required = false, value = "username") String username,
                                                 @RequestHeader(required = false, value = "token") String token){
        if(username.equalsIgnoreCase("admin")) {
            ResponseEntity<Object> responseToken = userService.checkToken(username,token);
            if(responseToken == null) {
                GenericResponseDTO response = masterdataRest.deleteCandidate(username,token,id);

                return ResponseEntity.ok(response);
            }  else {
                return responseToken;
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericResponseDTO().errorResponse("Hanya untuk Admin")) ;
    }


}
