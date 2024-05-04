package com.sekolah.masterdata.controller;

import com.sekolah.masterdata.dto.CandidateRequestDTO;
import com.sekolah.masterdata.dto.GenericResponseDTO;
import com.sekolah.masterdata.event.OutgoingMessageEvent;
import com.sekolah.masterdata.service.CandidateService;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/masterdata/candidate")
public class CandidateController {

    @Autowired
    CandidateService candidateService;

    @Autowired
    OutgoingMessageEvent outgoingMessageEvent;

    //fitur ke 4
    //find spesific untuk orang yg sudah login dan daftar
    @GetMapping("/find/by-username")
    public ResponseEntity<Object> findCandidateByUser(
                                               @RequestHeader(required = false, value = "username") String username,
                                               @RequestHeader(required = false, value = "token") String token){
            ResponseEntity<Object> responseToken = candidateService.checkToken(username, token);
            if (responseToken == null) {
                ResponseEntity<Object> response = candidateService.find(username);

                return response;
            } else {
                return responseToken;
            }
    }

    //fitur ke 5
    //find all untuk admin, dynamic query bisa pakai sortby atau paging
    @PostMapping("/find/all")
    public ResponseEntity<Object> findAllCandidate(@Nullable @RequestBody CandidateRequestDTO requestDTO,
                                                  @RequestHeader(required = false, value = "username") String username,
                                                  @RequestHeader(required = false, value = "token") String token){
        if(username.equalsIgnoreCase("admin")) {
                ResponseEntity<Object> responseToken = candidateService.checkToken(username,token);
            if(responseToken == null) {
                ResponseEntity<Object> response = candidateService.findAll(requestDTO);
                return response;
            } else {
                return responseToken;
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericResponseDTO().errorResponse("Hanya untuk Admin")) ;
    }

    //delete khusus admin
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> DeleteCandidateById(@PathVariable("id") Integer request,
                                                 @RequestHeader(required = false, value = "username") String username,
                                                 @RequestHeader(required = false, value = "token") String token){
        if(username.equalsIgnoreCase("admin")) {
            ResponseEntity<Object> responseToken = candidateService.checkToken(username,token);
            if(responseToken == null) {
                ResponseEntity<Object> response = candidateService.delete(request);

                return response;
            }  else {
                return responseToken;
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericResponseDTO().errorResponse("Hanya untuk Admin")) ;
    }
}
