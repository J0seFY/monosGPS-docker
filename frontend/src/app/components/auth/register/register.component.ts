import { Component } from '@angular/core';
import { UserService } from '../user.service';
import { Router } from '@angular/router';

export interface UserDTO {
  rut: string;
  nombres: string;
  apellido1: string;
  email: string;
  establecimiento: string;
  password: string;
}

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  user: UserDTO = {
    rut: '',
    nombres: '',
    apellido1: '',
    email: '',
    establecimiento: '',
    password: '',
  };


  confirmPassword: string = '';
  constructor(private userService: UserService, private router: Router) {}

  onRegister() {
    if (this.user.password !== this.confirmPassword) {
        alert('Las contraseñas no coinciden');
        return;
    }

    this.userService.register(this.user).subscribe({
        next: (res) => {
            console.log('Usuario registrado correctamente', res);
            this.router.navigate(['/login']); 
        },
        error: (err) => {
            console.error('Error al registrar usuario', err);
            alert('Error al registrar. Verifica los datos.');

            console.log('Detalles del error:', err);
        }
    });
}

}
