package com.sekolah.masterdata.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sekolah.masterdata.dto.CandidateRequestDTO;
import com.sekolah.masterdata.model.CandidateModel;
import com.sekolah.masterdata.repository.CandidateRepository;
import com.sekolah.masterdata.service.CandidateService;
import com.sekolah.masterdata.util.CheckUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class IncomingMessageEvent {

    @Autowired
    CandidateRepository candidateRepository;
    @Autowired
    CandidateService candidateService;

    @KafkaListener(topics = "candidate-save", groupId = "sekolah")
    public void candidateSave(String request) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        CandidateRequestDTO requestDTO = objectMapper.readValue(request, CandidateRequestDTO.class);
        CandidateModel candidateModel = candidateRepository.findByCreatedBy(requestDTO.getCreatedBy());
        Integer id = null;
        if(candidateModel != null) {
            id = candidateModel.getId();
        }
        if (id == null) {
            candidateService.save(requestDTO);
        } else {
            candidateService.update(requestDTO, id);
        }
    }

}
