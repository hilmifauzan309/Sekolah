package com.sekolah.user.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sekolah.user.dto.UserRequestDTO;
import com.sekolah.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class IncomingMessageEvent {

    @Autowired
    UserService userService;

    @KafkaListener(topics = "hello", groupId = "sekolah")
    public void consume(String message) {
        System.out.println(message);
    }

    @KafkaListener(topics = "user-save", groupId = "sekolah")
    public void userSave(String request) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UserRequestDTO requestDTO = objectMapper.readValue(request, UserRequestDTO.class);

        Integer id = requestDTO.getId();
        String username = requestDTO.getUsername();
        String password = requestDTO.getPassword();
        if(id == null){
            userService.save(username,password);
        } else {
            userService.update(id,username,password);
        }
    }

}
