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
    { id: 1, nombre: 'Colegio San Patricio' },
    { id: 2, nombre: 'Liceo Técnico Industrial' },
    { id: 3, nombre: 'Colegio Santa Teresa' },
    { id: 4, nombre: 'Instituto Nacional' },
    { id: 5, nombre: 'Colegio Alemán' }
  ];

  constructor(private http: HttpClient, private personaService: PersonaService) { }

  agregarPersona() {
    this.persona.establecimiento = Number(this.persona.establecimiento);
    
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
          establecimiento: '',
        };
      },
      error: (err) => {
        console.error(err);
        this.mensaje = 'Error al agregar persona.';
      }
    });
  }
}