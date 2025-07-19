import { Component } from '@angular/core';

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

  toggleDropdown() {
    this.isOpen = !this.isOpen;
  }
}