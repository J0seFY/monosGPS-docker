import { Component } from '@angular/core';
import { HttpClient, HttpResponse, HttpErrorResponse } from '@angular/common/http';

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

  onReportClick(report: string) {
    if (report === 'Centralización de información de todas las comunidades educativas') {
      this.descargarReporteMatriculasComunales();
    } else {
      // Aquí puedes agregar lógica para otros reportes
      console.log('Reporte seleccionado:', report);
      // Opcional: mostrar mensaje de que el reporte no está disponible aún
      alert('Este reporte estará disponible próximamente');
    }
    
    // Cerrar el dropdown después de seleccionar
    this.isOpen = false;
  }

  descargarReporteMatriculasComunales() {
    const url = 'http://pacheco.chillan.ubiobio.cl:8000/api/reportes/matriculas-comunales/pdf?comuna=Chillán';
    
    // Mostrar indicador de carga (opcional)
    console.log('Descargando reporte...');
    
    this.http.get(url, { 
      responseType: 'blob',
      observe: 'response'
    }).subscribe({
      next: (response: HttpResponse<Blob>) => {
        // Crear blob del PDF
        const blob = new Blob([response.body!], { type: 'application/pdf' });
        
        // Crear URL temporal para el blob
        const downloadUrl = window.URL.createObjectURL(blob);
        
        // Crear elemento <a> temporal para descargar
        const link = document.createElement('a');
        link.href = downloadUrl;
        link.download = 'reporte_matriculas_Chillan.pdf';
        
        // Simular clic para descargar
        document.body.appendChild(link);
        link.click();
        
        // Limpiar
        document.body.removeChild(link);
        window.URL.revokeObjectURL(downloadUrl);
        
        console.log('Reporte descargado exitosamente');
      },
      error: (error: HttpErrorResponse) => {
        console.error('Error al descargar el reporte:', error);
        alert('Error al descargar el reporte. Por favor, intenta nuevamente.');
      }
    });
  }
}