import { Component } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-generar-reportes',
  templateUrl: './generar-reportes.component.html',
  styleUrl: './generar-reportes.component.css'
})
export class GenerarReportesComponent {
  isOpen = false;

  reports: string[] = [
    'Centralización de información de todas las comunidades educativas',
    'Matrícula Comunal ',
    'Matrícula por colegio',
    'Listado de alumnos extranjeros',
    'Listado de alumnos retirados',
    'Listado de posibles repitencias por establecimiento, curso y asignaturas',
    'Listado de accidentes escolares a nivel comunal',
    'Enviar mensajes a los apoderados a nivel comunal',
    'Reportes de resultados comunal: SIMCE',
    'Reportes de resultados comunal: PAES ',
    'Reportes de resultados comunal: Cobertura Curricular',
    'Informe de asistencia diaria de todos los establecimientos',
    'Informe de rendimiento a nivel comunal y por establecimiento',
    'Reporte de inasistencias a nivel comunal y por establecimiento',
    'Sistema de mensajería a nivel sostenedor por perfiles',
    'Reporte de funcionarios a nivel comunal y por establecimiento',
    'Búsqueda de personas: alumnos, apoderados, profesores, administrativos',
  ];

  constructor(private http: HttpClient) {}

  toggleDropdown() {
    this.isOpen = !this.isOpen;
  }

  onReportSelected(report: string) {
    if (report === 'Centralización de información de todas las comunidades educativas') {
      this.downloadCentralizacionReport();
    }
    // Cerrar el dropdown después de seleccionar
    this.isOpen = false;
  }

  downloadCentralizacionReport() {
    const url = 'http://pacheco.chillan.ubiobio.cl:8000/api/reportes/matriculas-comunales/pdf?comuna=Chillán';
    
    this.http.get(url, { responseType: 'blob' }).subscribe({
      next: (response: Blob) => {
        // Crear un blob URL y descargar el archivo
        const blob = new Blob([response], { type: 'application/pdf' });
        const downloadURL = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = downloadURL;
        link.download = 'centralizacion-informacion-comunidades.pdf';
        link.click();
        window.URL.revokeObjectURL(downloadURL);
      },
      error: (error: HttpErrorResponse) => {
        console.error('Error al descargar el reporte:', error);
        alert('Error al descargar el reporte. Por favor, inténtalo de nuevo.');
      }
    });
  }
}