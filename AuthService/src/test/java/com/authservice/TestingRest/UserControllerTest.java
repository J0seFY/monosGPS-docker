package com.authservice.TestingRest;

import com.authservice.controller.UserController;
import com.authservice.model.LoginRequest;
import com.authservice.model.UserDTO;
import com.authservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void agregarUsuarioTestConRUTValido() throws Exception {
    // Datos de prueba
    UserDTO userDTO = new UserDTO();
    userDTO.setRut("12345678-9");
    userDTO.setPassword("password123");

    when(userService.agregarUser(any(UserDTO.class))).thenReturn(userDTO);

    String userJson = new ObjectMapper().writeValueAsString(userDTO);

    mockMvc.perform(post("/AuthService")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))  
            .andExpect(status().isCreated())  
            .andExpect(jsonPath("$.rut").value("12345678-9")); // Verificar que el RUT sea correcto
}


    @Test
    public void agregarUsuarioTestConRUTInvalido() throws Exception {
        // Datos de prueba con RUT inválido
        UserDTO userDTO = new UserDTO();
        userDTO.setRut("1234");  // RUT inválido
        userDTO.setPassword("password123");

        // Realizar la petición POST y verificar la respuesta
        mockMvc.perform(post("/AuthService")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"rut\":\"1234\",\"password\":\"password123\"}"))
                .andExpect(status().isBadRequest())  // Verificar que la respuesta sea 400 (Bad Request)
                .andExpect(content().string("RUT debe tener el formato xxxxxxxx-x"));  // Verificar el mensaje de error
    }


    @Test
    public void loginTestConUsuarioInvalido() throws Exception {
        // Datos de prueba
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setRut("12345678-9");
        loginRequest.setPassword("wrongpassword");

        // Simular el comportamiento del servicio para un login incorrecto
        when(userService.findByRutAndPassword("12345678-9", "wrongpassword"))
                .thenReturn(null);

        // Realizar la petición POST para login y verificar la respuesta
        mockMvc.perform(post("/AuthService/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"rut\":\"12345678-9\",\"password\":\"wrongpassword\"}"))
                .andExpect(status().isUnauthorized())  // Verificar que la respuesta sea 401 (Unauthorized)
                .andExpect(content().string("uuario y/o contraseña incorrecto"));  // Verificar el mensaje de error
    }
}
