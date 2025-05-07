import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
@Component({
  standalone: false,
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  rut: string = '';
  password: string = '';

  onSubmit() {
    if (this.rut && this.password) {
      console.log('RUT:', this.rut);
      console.log('Contraseña:', this.password);
      // Aquí puedes agregar la lógica para autenticar al usuario
    } else {
      alert('Por favor complete todos los campos');
    }
  }
}
