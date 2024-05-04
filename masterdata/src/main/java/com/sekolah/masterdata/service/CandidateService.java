package com.sekolah.masterdata.service;


import com.sekolah.masterdata.dto.CandidateRequestDTO;
import com.sekolah.masterdata.dto.GenericResponseDTO;
import com.sekolah.masterdata.model.CandidateModel;
import com.sekolah.masterdata.repository.CandidateRepository;
import com.sekolah.masterdata.util.CheckUtils;
import com.sekolah.masterdata.util.GenerateJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidateService {

    @Autowired
    CandidateRepository candidateRepository;

    @Transactional
    public void save(CandidateRequestDTO requestDTO){

        //dto to model
        CandidateModel candidateModel = new CandidateModel();
        candidateModel.setNama(requestDTO.getNama());
        candidateModel.setAlamat(requestDTO.getAlamat());
        candidateModel.setTempatLahir(requestDTO.getTempatLahir());
        candidateModel.setTanggalLahir(requestDTO.getTanggalLahir());
        candidateModel.setNomorKartuKeluarga(requestDTO.getNomorKartuKeluarga());
        candidateModel.setCreatedBy(requestDTO.getCreatedBy());

        //save to db
        candidateRepository.save(candidateModel);
    }

    public ResponseEntity<Object> find(String user) {
        CandidateModel candidateModel = candidateRepository.findByCreatedBy(user);
        if(candidateModel == null){
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(candidateModel);
    }

    public ResponseEntity<Object> findAll(CandidateRequestDTO requestDto){
        List<CandidateModel> listCandidateModel;
        Integer pageNo=null;
        Integer pageSize=null;
        String sortBy=null;
        if(requestDto != null) {
            pageNo = requestDto.getPageNo();
            pageSize = requestDto.getPageSize();
            sortBy = requestDto.getSortBy();
        }

        if(pageSize!= null && pageNo != null && sortBy != null){
            PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
            listCandidateModel = candidateRepository.findAll(pageRequest).getContent();
        } else if(pageSize!= null && pageNo != null){
            PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
            listCandidateModel = candidateRepository.findAll(pageRequest).getContent();
        } else if(sortBy != null){
            Sort sort = Sort.by(sortBy);
            listCandidateModel = candidateRepository.findAll(sort);
        } else {
            listCandidateModel = candidateRepository.findAll();
        }

        return ResponseEntity.status(HttpStatus.OK).body(listCandidateModel);
    }

    @Transactional
    public void update(CandidateRequestDTO requestDTO, Integer id){
        Optional<CandidateModel> candidateData = candidateRepository.findById(id);

        if (candidateData.isPresent()) {
            CandidateModel candidateModel = candidateData.get();

            //update data
            if(CheckUtils.IsNullOrEmpty(requestDTO.getNama())){
                candidateModel.setNama(candidateModel.getNama());
            } else{
                candidateModel.setNama(requestDTO.getNama());
            }
            if(CheckUtils.IsNullOrEmpty(requestDTO.getAlamat())){
                candidateModel.setAlamat(candidateModel.getAlamat());
            }else{
                candidateModel.setAlamat(requestDTO.getAlamat());
            }
            if(CheckUtils.IsNullOrEmpty(requestDTO.getTempatLahir())){
                candidateModel.setTempatLahir(candidateModel.getTempatLahir());
            }else{
                candidateModel.setTempatLahir(requestDTO.getTempatLahir());
            }
            if(CheckUtils.IsNullOrEmpty(requestDTO.getTanggalLahir())){
                candidateModel.setTanggalLahir(candidateModel.getTanggalLahir());
            }else{
                candidateModel.setTanggalLahir(requestDTO.getTanggalLahir());
            }
            if(CheckUtils.IsNullOrEmpty(requestDTO.getNomorKartuKeluarga())){
                candidateModel.setNomorKartuKeluarga(candidateModel.getNomorKartuKeluarga());
            }else{
                candidateModel.setNomorKartuKeluarga(requestDTO.getNomorKartuKeluarga());
            }
            //save to db
            candidateRepository.save(candidateModel);

        }
    }

    @Transactional
    public ResponseEntity<Object> delete(Integer request){
        //save to db
        candidateRepository.deleteById(request);
        return ResponseEntity.status(HttpStatus.OK).body(new GenericResponseDTO().successResponse("Delete Success"));
    }

    public ResponseEntity<Object> checkToken(String username, String token){
        try{
            if(CheckUtils.IsNullOrEmpty(username) && CheckUtils.IsNullOrEmpty(token)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponseDTO().errorResponse("JWT Fail"));
            }

            Claims claims = GenerateJWT.validateToken(token);

            if(!claims.getId().equals(username)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponseDTO().errorResponse("Not a token with a username " + username));
            }

        }catch (ExpiredJwtException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponseDTO().errorResponse("Token expired"));
        }catch (Exception x){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponseDTO().errorResponse("Token failed"));
        }
        return null;
    }

}
