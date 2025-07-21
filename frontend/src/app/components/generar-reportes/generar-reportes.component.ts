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
    'Reportes de resultados comunal: SIMCE',
    'Reportes de resultados comunal: PAES ',
    'Reportes de resultados comunal: Cobertura Curricular',
    'Informe de asistencia diaria de todos los establecimientos',
    'Informe de rendimiento a nivel comunal y por establecimiento',
    'Reporte de inasistencias a nivel comunal y por establecimiento',
    'Reporte de funcionarios a nivel comunal y por establecimiento',
  ];

  constructor(private http: HttpClient) {}

  toggleDropdown() {
    this.isOpen = !this.isOpen;
  }

  onReportSelected(report: string) {
    switch (report.trim()) {
      case 'Centralización de información de todas las comunidades educativas':
        this.downloadCentralizacionReport();
        break;
      case 'Matrícula Comunal':
        this.downloadMatriculaComunalReport();
        break;
      case 'Matrícula por colegio':
        this.downloadFromUrl('http://pacheco.chillan.ubiobio.cl:8000/api/reportes/matricula/establecimiento/1/pdf', 'matricula-por-colegio.pdf');
        break;
      case 'Listado de alumnos extranjeros':
        this.downloadFromUrl('http://pacheco.chillan.ubiobio.cl:8000/api/reportes/estudiantes-extranjeros/pdf', 'alumnos-extranjeros.pdf');
        break;
      case 'Listado de alumnos retirados':
        this.downloadFromUrl('http://pacheco.chillan.ubiobio.cl:8000/api/reportes/estudiantes-retirados/pdf/establecimiento/1', 'alumnos-retirados.pdf');
        break;
      case 'Listado de posibles repitencias por establecimiento, curso y asignaturas':
        this.downloadFromUrl('http://pacheco.chillan.ubiobio.cl:8000/api/informes/repitencia/pdf', 'posibles-repitencias.pdf');
        break;
      case 'Listado de accidentes escolares a nivel comunal':
        this.downloadFromUrl('http://pacheco.chillan.ubiobio.cl:8000/api/reportesAccidentes/accidentes', 'accidentes-escolares.pdf');
        break; 
      case 'Informe de asistencia diaria de todos los establecimientos':
        this.downloadFromUrl('http://pacheco.chillan.ubiobio.cl:8000/api/reportes/asistencia/pdf?fecha=2025-07-21', 'asistencia-diaria.pdf');
        break;
      case 'Informe de rendimiento a nivel comunal y por establecimiento':
        this.downloadFromUrl('http://pacheco.chillan.ubiobio.cl:8000/api/informes/rendimiento/pdf', 'informe-rendimiento.pdf');
        break;
      case 'Reporte de inasistencias a nivel comunal y por establecimiento':
        this.downloadFromUrl('http://pacheco.chillan.ubiobio.cl:8000/api/reportes/asistencia/pdf?fecha=2025-07-21', 'reporte-inasistencias.pdf');
        break;
      case 'Reporte de funcionarios a nivel comunal y por establecimiento':
        this.downloadFromUrl('http://pacheco.chillan.ubiobio.cl:8000/api/reportesFuncionarios/funcionarios/comunal', 'funcionarios-comunal.pdf');
        break;
      default:
        alert('Este reporte aún no está implementado.');
    }

    this.isOpen = false;
  }

  downloadCentralizacionReport() {
    const url = 'http://pacheco.chillan.ubiobio.cl:8000/api/reportes/matriculas-comunales/informe-general';

    this.http.get(url, { responseType: 'blob' }).subscribe({
      next: (response: Blob) => {
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

  downloadMatriculaComunalReport() {
    const url = 'http://pacheco.chillan.ubiobio.cl:8000/api/reportes/matriculas-comunales/pdf?comuna=Chillán';
    
    this.http.get(url, { responseType: 'blob' }).subscribe({
      next: (response: Blob) => {
        if (response.size === 0) {
          alert('El archivo descargado está vacío. Verifique el reporte.');
          return;
        }

        if (response.type && !response.type.includes('pdf') && !response.type.includes('octet-stream')) {
          console.warn('El tipo de archivo no parece ser PDF:', response.type);
          response.text().then(text => {
            console.log('Contenido de la respuesta:', text.substring(0, 200));
          });
        }

        const blob = new Blob([response], { type: 'application/pdf' });
        const downloadURL = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = downloadURL;
        link.download = 'matricula-comunal.pdf';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        setTimeout(() => window.URL.revokeObjectURL(downloadURL), 100);
      },
      error: (error: HttpErrorResponse) => {
        console.error('Error completo:', error);
        if (error.status === 0) {
          alert('Error de conexión. Verifique que el servidor esté disponible y que no haya problemas de CORS.');
        } else if (error.status === 404) {
          alert('El endpoint no fue encontrado. Verifique la URL del servidor.');
        } else {
          alert(`Error al descargar el reporte (${error.status}): ${error.message}`);
        }
      }
    });
  }

  downloadFromUrl(url: string, filename: string) {
    this.http.get(url, { responseType: 'blob' }).subscribe({
      next: (response: Blob) => {
        if (response.size === 0) {
          alert('El archivo descargado está vacío.');
          return;
        }

        const blob = new Blob([response], { type: 'application/pdf' });
        const downloadURL = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = downloadURL;
        link.download = filename;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        setTimeout(() => window.URL.revokeObjectURL(downloadURL), 100);
      },
      error: (error: HttpErrorResponse) => {
        console.error('Error descargando:', error);
        alert(`Error al descargar el archivo (${error.status}): ${error.message}`);
      }
    });
  }
}
