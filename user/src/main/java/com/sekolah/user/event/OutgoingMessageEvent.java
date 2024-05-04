package com.sekolah.user.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sekolah.user.dto.CandidateRequestDTO;
import com.sekolah.user.dto.UserRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OutgoingMessageEvent {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendRequest() {
        kafkaTemplate.send("hello", "Hello World!");
    }

    public void userSaveRequest(UserRequestDTO requestDTO) throws Exception {
        String jsonString = new ObjectMapper().writeValueAsString(requestDTO);

        kafkaTemplate.send("user-save", jsonString);
    }

    public void candidateSaveRequest(CandidateRequestDTO requestDTO) throws Exception {
        String jsonString = new ObjectMapper().writeValueAsString(requestDTO);

        kafkaTemplate.send("candidate-save", jsonString);
    }

}
