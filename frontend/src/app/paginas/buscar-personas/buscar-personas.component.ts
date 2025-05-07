import { Component } from '@angular/core';
import { PersonaService } from '../../servicios/persona.service';

@Component({
  standalone: false,
  selector: 'app-buscar-personas',
  templateUrl: './buscar-personas.component.html',
  styleUrl: './buscar-personas.component.css'
})
export class BuscarPersonasComponent {
  nombre: string = '';
  personas: any[] = [];
  busquedasRealizadas: boolean = false;

  constructor(private personaService: PersonaService) { }

  buscarPersonas() {
    this.busquedasRealizadas = true;
    this.personaService.buscarPorNombre(this.nombre).subscribe({
      next: (resultado) => {
        this.personas = resultado;
      },
      error: (err) => {
        console.error('Error al buscar personas:', err);
        this.personas = []; // Limpiar la lista si hay un error
      }
    }
    )
  }
}
