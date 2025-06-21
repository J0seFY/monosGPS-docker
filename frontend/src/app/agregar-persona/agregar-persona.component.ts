import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-agregar-persona',
  templateUrl: './agregar-persona.component.html',
  styleUrl: './agregar-persona.component.css'
})
export class AgregarPersonaComponent {
  persona: any = {
    tipo: 'Estudiante',
    rut: '',
    nombre: '',
    apellido: '',
    telefono: '',
    fechaNacimiento: '',
    curso: '',
    asignatura: '',
    establecimientoId: null
  };

  mensaje: string = '';

  constructor(private http: HttpClient) {}

  agregarPersona() {
    this.http.post('http://localhost:8082/api/personas/agregar', this.persona).subscribe({
      next: () => {
        this.mensaje = 'Persona agregada correctamente.';
        this.persona = {
          tipo: 'Estudiante',
          rut: '',
          nombre: '',
          apellido: '',
          telefono: '',
          fechaNacimiento: '',
          curso: '',
          asignatura: '',
          establecimientoId: null
        };
      },
      error: (err) => {
        console.error(err);
        this.mensaje = 'Error al agregar persona.';
      }
    });
  }
}
