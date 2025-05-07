import { Component } from '@angular/core';
import { UserDTO } from '../register/register.component';
import { UserService } from '../user.service';
import { NgForm } from '@angular/forms';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  user: UserDTO = {
    rut: '',
    nombres: '',
    apellido1: '',
    email: '',
    establecimiento: '',
    password: ''
  };

  constructor(private userService: UserService) {}

  
}
