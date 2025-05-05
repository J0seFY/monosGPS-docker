package com.authservice.service;

import com.authservice.model.User;
import com.authservice.model.UserDTO;
import com.authservice.model.UserMapper;
import com.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public UserDTO agregarUser(UserDTO userDTO){
        if (userRepository.findByRut(userDTO.getRut()).isPresent()){
            throw new IllegalArgumentException("Ya existe un usuario registrado con ese RUT");
        }

        User newUser = userRepository.save(userMapper.toEntity(userDTO));

        return userMapper.toDTO(newUser);
    }

    public User findByRutAndPassword(String rut,String password){

        Optional<User> findUser = userRepository.findByRutAndPassword(rut,password);

        if (findUser.isEmpty()){
            throw new NullPointerException("No existe ese usuario");
        }

        return findUser.get();
    }
}
