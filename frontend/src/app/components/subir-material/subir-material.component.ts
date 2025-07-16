import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-subir-material',
  templateUrl: './subir-material.component.html',
  styleUrl: './subir-material.component.css'
})
export class SubirMaterialComponent {
  niveles: string[] = ['Parvularia', 'Básica', 'Media'];
  cursosDisponibles: string[] = [];

  nivelSeleccionado = '';
  cursoSeleccionado = '';

  archivoSeleccionado: File | null = null;
  mensaje = '';

  constructor(private http: HttpClient) {}

  onNivelChange() {
    if (this.nivelSeleccionado === 'Parvularia') {
      this.cursosDisponibles = ['Pre-Kinder', 'Kinder'];
    } else if (this.nivelSeleccionado === 'Básica') {
      this.cursosDisponibles = Array.from({ length: 8 }, (_, i) => `${i + 1}° Básico`);
    } else if (this.nivelSeleccionado === 'Media') {
      this.cursosDisponibles = Array.from({ length: 4 }, (_, i) => `${i + 1}° Medio`);
    }

    this.cursoSeleccionado = '';
  }

  onArchivoSeleccionado(event: any) {
    const file = event.target.files[0];
    if (file && file.type === 'application/pdf') {
      this.archivoSeleccionado = file;
      this.mensaje = '';
    } else {
      this.mensaje = 'Solo se permiten archivos PDF.';
      this.archivoSeleccionado = null;
    }
  }

  subirMaterial() {
    if (this.archivoSeleccionado) {
      const formData = new FormData();
      formData.append('archivo', this.archivoSeleccionado);
      formData.append('nivel', this.nivelSeleccionado);
      formData.append('curso', this.cursoSeleccionado);

      this.http.post('http://localhost:3000/api/materiales', formData).subscribe({
        next: () => {
          this.mensaje = 'Material subido correctamente.';
        },
        error: () => {
          this.mensaje = 'Error al subir el material.';
        },
      });
    }
  }
}
