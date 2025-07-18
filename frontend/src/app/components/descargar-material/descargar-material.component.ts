import { Component, OnInit } from '@angular/core';
import { MaterialDescargableDTO, MaterialDescargableService } from '../../servicios/material-descargable.service';
@Component({
  selector: 'app-descargar-material',
  templateUrl: './descargar-material.component.html',
  styleUrl: './descargar-material.component.css'
})
export class DescargarMaterialComponent implements OnInit {
  niveles: string[] = ['Parvularia', 'Básica', 'Media'];
  cursosDisponibles: string[] = [];

  nivelSeleccionado: string = '';
  cursoSeleccionado: string = '';

  materialFiltrado: MaterialDescargableDTO[] = [];
  cargando = false;
  mensaje = '';

  constructor(private materialService: MaterialDescargableService) {}

  ngOnInit(): void {
    // Inicializar con el primer nivel
    if (this.niveles.length > 0) {
      this.nivelSeleccionado = this.niveles[0];
      this.onNivelChange();
    }
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

  filtrarMaterial() {
    if (this.nivelSeleccionado && this.cursoSeleccionado) {
      this.cargando = true;
      this.mensaje = '';

      this.materialService.listarArchivos(this.nivelSeleccionado, this.cursoSeleccionado)
        .subscribe({
          next: (materiales) => {
            this.materialFiltrado = materiales;
            this.cargando = false;
            if (materiales.length === 0) {
              this.mensaje = 'No hay material disponible para el nivel y curso seleccionado.';
            }
          },
          error: (error) => {
            console.error('Error al cargar material:', error);
            this.mensaje = 'Error al cargar el material.';
            this.materialFiltrado = [];
            this.cargando = false;
          }
        });
    }
  }

  descargarArchivo(material: MaterialDescargableDTO) {
    this.materialService.descargarArchivo(material.id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = material.nombreOriginal || 'archivo.pdf';
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
      },
      error: (error) => {
        console.error('Error al descargar archivo:', error);
        this.mensaje = 'Error al descargar el archivo.';
      }
    });
  }

  obtenerUrlDescarga(material: MaterialDescargableDTO): string {
    return this.materialService.obtenerUrlDescarga(material.id);
  }
}
