import { Component } from '@angular/core';
import { PersonaService, Persona } from '../../servicios/persona.service';
import { AgregarPersonaComponent } from '../agregar-persona/agregar-persona.component';
@Component({
  selector: 'app-buscar-personas',
  templateUrl: './buscar-personas.component.html',
  styleUrls: ['./buscar-personas.component.css']
})
export class BuscarPersonasComponent {
  nombre: string = '';
  personas: Persona[] = [];
  busquedasRealizadas: boolean = false;

  constructor(private personaService: PersonaService) {}

  buscarPersonas() {
    console.log('Buscando nombre:', this.nombre);
    
    this.personaService.buscarPorNombre(this.nombre).subscribe(
      (data) => {
        console.log('Respuesta backend:', data);
        this.personas = data.personas;
        this.busquedasRealizadas = true;
      },
      (error) => {
        console.error('Error al buscar personas:', error);
        this.personas = [];
        this.busquedasRealizadas = true;
      }
    );
  }
}
