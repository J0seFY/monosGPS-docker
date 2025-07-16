import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-descargar-material',
  templateUrl: './descargar-material.component.html',
  styleUrl: './descargar-material.component.css'
})
export class DescargarMaterialComponent {
  niveles: string[] = ['Parvularia', 'Básica', 'Media'];
  cursosDisponibles: string[] = [];

  nivelSeleccionado: string = '';
  cursoSeleccionado: string = '';

  materialTotal: any[] = [];
  materialFiltrado: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.obtenerMaterialDesdeBackend();
  }

  onNivelChange() {
    if (this.nivelSeleccionado === 'Parvularia') {
      this.cursosDisponibles = ['Pre-Kinder', 'Kinder'];
    } else if (this.nivelSeleccionado === 'Básica') {
      this.cursosDisponibles = Array.from({ length: 8 }, (_, i) => `${i + 1}° Básico`);
    } else if (this.nivelSeleccionado === 'Media') {
      this.cursosDisponibles = Array.from({ length: 4 }, (_, i) => `${i + 1}° Medio`);
    }

    this.cursoSeleccionado = '';
    this.materialFiltrado = [];
  }

  obtenerMaterialDesdeBackend() {
    this.http.get<any[]>('http://localhost:3000/api/materiales').subscribe((data) => {
      this.materialTotal = data;
    });
  }

  filtrarMaterial() {
    this.materialFiltrado = this.materialTotal.filter(
      (m) =>
        m.nivel === this.nivelSeleccionado &&
        m.curso === this.cursoSeleccionado
    );
  }
}
