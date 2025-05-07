package com.authservice.controller;

import com.authservice.model.LoginRequest;
import com.authservice.model.User;
import com.authservice.model.UserDTO;
import com.authservice.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;


import java.util.regex.Pattern;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/AuthService")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> agregarUsuario(@RequestBody UserDTO userDTO) {
        try {

            String regex = "^(\\d{7,8}-[0-9K])$";
            Pattern patron = Pattern.compile(regex);
            String run = userDTO.getRut();
            if(!patron.matcher(run).matches()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("RUT debe tener el formato xxxxxxxx-x");
            }
            UserDTO nuevoUsuario = userService.agregarUser(userDTO);
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request,
                                   HttpServletResponse response) {
        String rut = loginRequest.getRut();
        String password = loginRequest.getPassword();

        String regex = "^(\\d{7,8}-[0-9K])$";
        Pattern patron = Pattern.compile(regex);
        if(!patron.matcher(rut).matches()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("RUN debe tener el formato xxxxxxxx-x");
        }

        User oauthUsuario = userService.findByRutAndPassword(rut,password);
        System.out.println("Usuario encontrado: " + oauthUsuario); // Esto imprimirá el usuario o `null`
        if (oauthUsuario != null) {
            /*
            HttpSession session = request.getSession(true);
            session.setAttribute("usuarioActual",oauthUsuario);
            Cookie sessionCookie = new Cookie("JSESSIONID", session.getId());
            sessionCookie.setHttpOnly(true);
            sessionCookie.setPath("/");
            response.addCookie(sessionCookie);
             */
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(oauthUsuario);
        } else {
            System.out.println("usuario y contraseña incorrectos");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("uuario y/o contraseña incorrecto");
        }
    }
}
