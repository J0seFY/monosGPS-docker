import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-agregar-persona',
  templateUrl: './agregar-persona.component.html',
  styleUrls: ['./agregar-persona.component.css']
})
export class AgregarPersonaComponent {
  persona: any = {
    tipo: '',
    rut: '',
    nombre: '',
    apellido: '',
    telefono: '',
    curso: '',
    asignatura: '',
    fechaNacimiento: '',
    establecimiento: ''
  };

  mensaje: string = '';

  constructor(private http: HttpClient) { }

  agregarPersona() {
    this.http.post('http://localhost:81/api/personas/agregar', this.persona).subscribe({
      next: () => {
        this.mensaje = 'Persona agregada correctamente.';
        this.persona = {
          tipo: '',
          rut: '',
          nombre: '',
          apellido: '',
          telefono: '',
          curso: '',
          asignatura: '',
          fechaNacimiento: '',
          establecimiento: ''
        };
      },
      error: (err) => {
        console.error(err);
        this.mensaje = 'Error al agregar persona.';
      }
    });
  }
}
