import { Component } from '@angular/core';
import { MaterialDescargableService } from '../../servicios/material-descargable.service';
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
  nombreArchivo = '';

  archivoSeleccionado: File | null = null;
  mensaje = '';
  cargando = false;

  constructor(private materialService: MaterialDescargableService) {}

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
    if (this.archivoSeleccionado && this.nivelSeleccionado && this.cursoSeleccionado) {
      this.cargando = true;
      this.mensaje = '';

      this.materialService.subirArchivo(
        this.archivoSeleccionado,
        this.nivelSeleccionado,
        this.cursoSeleccionado
      ).subscribe({
        next: (response) => {
          this.mensaje = 'Material subido correctamente.';
          this.limpiarFormulario();
          this.cargando = false;
        },
        error: (error) => {
          console.error('Error al subir material:', error);
          this.mensaje = 'Error al subir el material: ' + (error.error || 'Error desconocido');
          this.cargando = false;
        }
      });
    }
  }

  limpiarFormulario() {
    this.nivelSeleccionado = '';
    this.cursoSeleccionado = '';
    this.nombreArchivo = '';
    this.archivoSeleccionado = null;
  }
}