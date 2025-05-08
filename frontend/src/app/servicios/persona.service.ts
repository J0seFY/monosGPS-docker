import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class PersonaService {
  private apiUrl = 'http://localhost:8080/api/personas'; // Cambia esto a la URL de tu API
  
  constructor(private http: HttpClient) { }

  buscarPorNombre(nombre: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/buscar/${nombre}`);
  }
}
