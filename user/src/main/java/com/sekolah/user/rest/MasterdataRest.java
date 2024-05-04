package com.sekolah.user.rest;

import com.sekolah.user.dto.CandidateRequestDTO;
import com.sekolah.user.dto.GenericResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MasterdataRest {

    @Autowired
    private RestTemplate restTemplate;

    public CandidateRequestDTO findCandidate(String username, String token) {
        String url = "http://localhost:8081/api/masterdata/candidate/find/by-username";

        HttpHeaders headers = new HttpHeaders();
        headers.set("username", username);
        headers.set("token", token);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<CandidateRequestDTO> responseEntity = restTemplate.exchange(
                url, HttpMethod.GET, requestEntity, CandidateRequestDTO.class);

        return responseEntity.getBody();
    }

    public GenericResponseDTO deleteCandidate(String username, String token, Integer id) {
        String url = "http://localhost:8081/api/masterdata/candidate/delete/"+id;

        HttpHeaders headers = new HttpHeaders();
        headers.set("username", username);
        headers.set("token", token);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<GenericResponseDTO> responseEntity = restTemplate.exchange(
                url, HttpMethod.DELETE, requestEntity, GenericResponseDTO.class);

        return responseEntity.getBody();
    }

}
