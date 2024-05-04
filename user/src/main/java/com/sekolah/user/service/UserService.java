package com.sekolah.user.service;

import com.sekolah.user.dto.GenericResponseDTO;
import com.sekolah.user.model.UserModel;
import com.sekolah.user.repository.UserRepository;
import com.sekolah.user.util.CheckUtils;
import com.sekolah.user.util.GenerateJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public Optional<UserModel> findUsername(String username){
        return userRepository.findByUsername(username);
    }

    @Transactional
    public ResponseEntity<Object> save(String username, String password){

        //dto to model
        UserModel employeeModel = new UserModel();
        employeeModel.setUsername(username);
        employeeModel.setPassword(password);

        //save to db
        userRepository.save(employeeModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(employeeModel);
    }

    @Transactional
    public ResponseEntity<Object> update(Integer id,String username,String password){
        Optional<UserModel> usergData = userRepository.findById(id);

        if (usergData.isPresent()) {
            UserModel loginModel = usergData.get();

            //update data
            if(CheckUtils.IsNullOrEmpty(username)){
                loginModel.setUsername(loginModel.getUsername());
            } else{
                loginModel.setUsername(username);
            }

            if(CheckUtils.IsNullOrEmpty(password)){
                loginModel.setPassword(loginModel.getPassword());
            }else{
                loginModel.setPassword(password);
            }

            //save to db
            userRepository.save(loginModel);

            return ResponseEntity.status(HttpStatus.OK).body(loginModel);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericResponseDTO().errorResponse("Data not Found"));
        }
    }

    public ResponseEntity<Object> checkToken(String username, String token){
        try{
            if(CheckUtils.IsNullOrEmpty(username) && CheckUtils.IsNullOrEmpty(token)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponseDTO().errorResponse("JWT Fail"));
            }

            Claims claims = GenerateJWT.validateToken(token);
            System.out.println("haha" + claims.getId());
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
