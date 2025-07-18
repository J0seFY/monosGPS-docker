import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface MaterialDescargableDTO {
  id: number;
  nombreOriginal: string;
  rutaArchivo: string;
  nivelEducativo: string;
  curso: string;
  fechaSubida: string;
}

@Injectable({
  providedIn: 'root'
})
export class MaterialDescargableService {
  private baseUrl = '/materiales'; // Ajusta según tu puerto de Spring Boot

  constructor(private http: HttpClient) {}

  // Mapear niveles del frontend al backend
  private mapearNivelEducativo(nivel: string): string {
    const mapeo: { [key: string]: string } = {
      'Parvularia': 'PARVULARIA',
      'Básica': 'BASICA',
      'Media': 'MEDIA'
    };
    return mapeo[nivel] || nivel;
  }

  // Mapear cursos del frontend al backend
  private mapearCurso(curso: string): string {
    const mapeo: { [key: string]: string } = {
      'Pre-Kinder': 'PRE_KINDER',
      'Kinder': 'KINDER',
      '1° Básico': 'PRIMERO_BASICO',
      '2° Básico': 'SEGUNDO_BASICO',
      '3° Básico': 'TERCERO_BASICO',
      '4° Básico': 'CUARTO_BASICO',
      '5° Básico': 'QUINTO_BASICO',
      '6° Básico': 'SEXTO_BASICO',
      '7° Básico': 'SEPTIMO_BASICO',
      '8° Básico': 'OCTAVO_BASICO',
      '1° Medio': 'PRIMERO_MEDIO',
      '2° Medio': 'SEGUNDO_MEDIO',
      '3° Medio': 'TERCERO_MEDIO',
      '4° Medio': 'CUARTO_MEDIO'
    };
    return mapeo[curso] || curso;
  }

  // Subir archivo
  subirArchivo(archivo: File, nivel: string, curso: string): Observable<MaterialDescargableDTO> {
    const formData = new FormData();
    formData.append('archivo', archivo);
    formData.append('nivelEducativo', this.mapearNivelEducativo(nivel));
    formData.append('curso', this.mapearCurso(curso));

    return this.http.post<MaterialDescargableDTO>(`${this.baseUrl}/subir`, formData);
  }

  // Listar archivos por nivel y curso
  listarArchivos(nivel: string, curso: string): Observable<MaterialDescargableDTO[]> {
    const nivelMapeado = this.mapearNivelEducativo(nivel);
    const cursoMapeado = this.mapearCurso(curso);
    
    return this.http.get<MaterialDescargableDTO[]>(
      `${this.baseUrl}/listar/nivel/${nivelMapeado}/curso/${cursoMapeado}`
    );
  }

  // Descargar archivo
  descargarArchivo(id: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/descargar/${id}`, {
      responseType: 'blob'
    });
  }

  // Obtener URL de descarga directa
  obtenerUrlDescarga(id: number): string {
    return `${this.baseUrl}/descargar/${id}`;
  }
}