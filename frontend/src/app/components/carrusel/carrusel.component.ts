import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbCarouselModule } from '@ng-bootstrap/ng-bootstrap';
@Component({
  selector: 'app-carrusel',
  templateUrl: './carrusel.component.html',
  styleUrl: './carrusel.component.css'
})
export class CarruselComponent {
  imagenes = [
    'HD-wallpaper-anime-school-anime-anime-nature-nature-anime-scenery-scenery.jpg',
    'EP1138_Escuela_de_Ciudad_Carmín.png',
    'saladeclases.png'
  ];

  indiceActual = 0;

  // Pasar a la siguiente imagen
  siguiente() {
    this.indiceActual = (this.indiceActual + 1) % this.imagenes.length;
  }

  // Pasar a la imagen anterior
  anterior() {
    this.indiceActual = (this.indiceActual - 1 + this.imagenes.length) % this.imagenes.length;
  }
}
