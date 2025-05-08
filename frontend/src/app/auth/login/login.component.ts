import { Component } from '@angular/core';
import { UserDTO } from '../register/register.component';
import { UserService } from '../user.service';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';

export interface LoginDTO {
  rut: string;
  password: string;
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  rut: string = '';
  user: LoginDTO = {
    rut: '',
    password: ''
  };

  constructor(private userService: UserService, private router: Router) {}

  onLogin() {
    this.user.rut = this.rut; 

    this.userService.login(this.user).subscribe({
      next: (res) => {
        console.log('Inicio de sesión exitoso', res);
        this.router.navigate(['/inicio']);
      },
      error: (err) => {
        console.error('Error al iniciar sesión', err);
        alert('RUT o contraseña incorrectos');
      }
    });
  }
}
