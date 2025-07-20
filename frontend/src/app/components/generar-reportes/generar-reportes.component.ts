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
    if (report === 'Centralización de información de todas las comunidades educativas') {
      this.downloadCentralizacionReport();
    } else if (report === 'Matrícula Comunal ') {
      this.downloadMatriculaComunalReport();
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

  downloadMatriculaComunalReport() {
    const url = 'http://pacheco.chillan.ubiobio.cl:8000/api/reportes/matriculas-comunales/certificado?comuna=Chillán';
    
    console.log('Descargando desde:', url);
    
    this.http.get(url, { 
      responseType: 'blob',
      headers: {
        'Accept': 'application/pdf'
      }
    }).subscribe({
      next: (response: Blob) => {
        console.log('Respuesta recibida:', response);
        console.log('Tamaño del blob:', response.size);
        console.log('Tipo del blob:', response.type);
        
        // Verificar que la respuesta sea válida
        if (response.size === 0) {
          alert('El archivo descargado está vacío. Verifique el reporte.');
          return;
        }
        
        // Verificar si realmente es un PDF
        if (response.type && !response.type.includes('pdf') && !response.type.includes('octet-stream')) {
          console.warn('El tipo de archivo no parece ser PDF:', response.type);
          // Intentar leer el contenido como texto para ver si hay un error
          response.text().then(text => {
            console.log('Contenido de la respuesta:', text.substring(0, 200));
          });
        }
        
        // Crear un blob URL y descargar el archivo
        const blob = new Blob([response], { type: 'application/pdf' });
        const downloadURL = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = downloadURL;
        link.download = 'matricula-comunal.pdf';
        
        // Forzar la descarga
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        
        // Limpiar el URL después de un breve delay
        setTimeout(() => {
          window.URL.revokeObjectURL(downloadURL);
        }, 100);
      },
      error: (error: HttpErrorResponse) => {
        console.error('Error completo:', error);
        console.log('Status:', error.status);
        console.log('Status Text:', error.statusText);
        console.log('Error message:', error.message);
        
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
}