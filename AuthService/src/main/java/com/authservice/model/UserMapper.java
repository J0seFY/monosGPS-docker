package com.authservice.model;

import org.springframework.stereotype.Component;


@Component
public class UserMapper {
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setRut(user.getRut());
        dto.setNombres(user.getNombres());
        dto.setApellido1(user.getApellido1());
        dto.setEmail(user.getEmail());
        dto.setEstablecimiento(user.getEstablecimiento());
        dto.setPassword(user.getPassword());
        return dto;
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setRut(dto.getRut());
        user.setNombres(dto.getNombres());
        user.setApellido1(dto.getApellido1());
        user.setEmail(dto.getEmail());
        user.setEstablecimiento(dto.getEstablecimiento());
        user.setPassword(dto.getPassword());
        return user;
    }
}
