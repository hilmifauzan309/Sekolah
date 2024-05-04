package com.sekolah.user.controller;

import com.sekolah.user.dto.GenericResponseDTO;
import com.sekolah.user.dto.UserRequestDTO;
import com.sekolah.user.event.OutgoingMessageEvent;
import com.sekolah.user.model.UserModel;
import com.sekolah.user.service.UserService;
import com.sekolah.user.util.CheckUtils;
import com.sekolah.user.util.Constant;
import com.sekolah.user.util.GenerateJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user/login")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    OutgoingMessageEvent outgoingMessageEvent;

    @PostMapping("/save")
    public ResponseEntity<Object> saveBlogPost(@RequestBody UserRequestDTO requestDTO){
        String username = requestDTO.getUsername();
        if(username.equalsIgnoreCase("admin")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponseDTO().errorResponse("Username tidak boleh menggunakan ADMIN"));
        }
        String password = requestDTO.getPassword();
        if(CheckUtils.IsNullOrEmpty(username) && CheckUtils.IsNullOrEmpty(password)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponseDTO().errorResponse("Data cannot be empty"));
        }
        try {
            outgoingMessageEvent.userSaveRequest(requestDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericResponseDTO().errorResponse(e.getMessage()));

        }
        return ResponseEntity.ok().body(new GenericResponseDTO().successResponse("Data berhasil disimpan"));
    }

    @PostMapping("/create/jwt")
    public ResponseEntity<Map<String, Object>>login(
            @RequestBody final Map<String,String> request
    ) {
        Map<String, Object> response = new HashMap<>();

        String username = request.get("username");
        String password = request.get("password");

        Optional<UserModel> check = userService.findUsername(username);

        if(check.isEmpty()){
            response.put(Constant.STATUS, "Username not registered");
        }else{
            if(password.equals(check.get().getPassword())){
                String token = GenerateJWT.createToken(username);
                response.put(Constant.TOKEN, token);
                response.put(Constant.STATUS, "JWT Created");
            }else{
                response.put(Constant.STATUS, "Wrong Password");
            }
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
