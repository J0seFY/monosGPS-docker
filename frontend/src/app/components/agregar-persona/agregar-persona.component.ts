import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { PersonaService } from '../../servicios/persona.service';

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
    correo:'',
    telefono: '',
    curso: '',
    asignatura: '',
    fechaNacimiento: '',
    establecimiento: ''
  };

  mensaje: string = '';

  // Lista de colegios con ID y nombre
  colegios = [
    { id: 1, nombre: 'Colegio Concepcion' },
    { id: 2, nombre: 'Liceo Técnico Industrial' },
    { id: 3, nombre: 'Colegio Santa Maria' },
    { id: 4, nombre: 'Colegio Chillan' },
    { id: 5, nombre: 'Colegio Alemán' },
  ];

  constructor(private http: HttpClient, private personaService: PersonaService) { }

  agregarPersona() {
    this.personaService.agregarPersona(this.persona).subscribe({
      next: () => {
        this.mensaje = 'Persona agregada correctamente.';
        this.persona = {
          tipo: '',
          rut: '',
          nombre: '',
          apellido: '',
          correo:'',
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