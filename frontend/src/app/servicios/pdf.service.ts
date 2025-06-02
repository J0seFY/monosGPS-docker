import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Persona {
  rut: string;
  nombre: string;
  establecimiento?: string;
}

@Injectable({
  providedIn: 'root'
})
export class PdfService {

  private apiUrl = 'http://monos.local/api/pdf/generar';

  constructor(private http: HttpClient) { }

  generarPdf(persona: any): Observable<Blob> {
    return this.http.post(this.apiUrl, persona, {
      responseType: 'blob' // 👈 Aquí ya no usamos 'as "json"'
    }) as Observable<Blob>;
  }
}
