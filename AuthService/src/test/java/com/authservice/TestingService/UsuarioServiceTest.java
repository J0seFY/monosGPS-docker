package com.authservice.TestingService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.authservice.model.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.authservice.repository.UserRepository;
import com.authservice.service.UserService;
import com.authservice.model.User;
import com.authservice.model.UserDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Test
    public void agregarUserCuandoUsuarioYaExisteLanzaExcepcion() {
        // Datos de prueba
        UserDTO userDTO = new UserDTO();
        userDTO.setRut("12345678-9");
        
        // Configurar el mock para que findByRut devuelva un Optional con un usuario
        when(userRepository.findByRut(userDTO.getRut())).thenReturn(Optional.of(new User()));

        // Verificar que se lanza una excepción
        assertThrows(IllegalArgumentException.class, () -> userService.agregarUser(userDTO));
    }

    @Test
    public void agregarUserCuandoUsuarioNoExisteLoAgregaCorrectamente() {
        // Datos de prueba
        UserDTO userDTO = new UserDTO();
        userDTO.setRut("12345678-9");

        // Mockear el comportamiento del repositorio y el mapper
        when(userRepository.findByRut(userDTO.getRut())).thenReturn(Optional.empty());
        User userMock = new User(); // Usuario mockeado
        when(userMapper.toEntity(userDTO)).thenReturn(userMock);
        when(userRepository.save(userMock)).thenReturn(userMock);
        when(userMapper.toDTO(userMock)).thenReturn(userDTO);

        // Llamar al método
        UserDTO resultado = userService.agregarUser(userDTO);

        // Verificar que el resultado no sea nulo y que el rut sea el esperado
        assertNotNull(resultado);
        assertEquals(userDTO.getRut(), resultado.getRut());
    }

    @Test
    public void findByRutAndPasswordCuandoUsuarioNoExisteLanzaExcepcion() {
        // Datos de prueba
        String rut = "12345678-9";
        String password = "password123";

        // Configurar el mock para que findByRutAndPassword devuelva un Optional vacío
        when(userRepository.findByRutAndPassword(rut, password)).thenReturn(Optional.empty());

        // Verificar que se lanza una excepción
        assertThrows(NullPointerException.class, () -> userService.findByRutAndPassword(rut, password));
    }

    @Test
    public void findByRutAndPasswordCuandoUsuarioExisteRetornaUsuario() {
        // Datos de prueba
        String rut = "12345678-9";
        String password = "password123";
        User userMock = new User();
        userMock.setRut(rut);
        userMock.setPassword(password);

        // Configurar el mock para que findByRutAndPassword devuelva un Optional con el usuario
        when(userRepository.findByRutAndPassword(rut, password)).thenReturn(Optional.of(userMock));

        // Llamar al método
        User resultado = userService.findByRutAndPassword(rut, password);

        // Verificar que el resultado no sea nulo y que el rut coincida
        assertNotNull(resultado);
        assertEquals(rut, resultado.getRut());
    }
}
