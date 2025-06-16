import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Persona {
  tipo: string;
  rut: string;
  nombre: string;
  apellido: string;
  telefono: string;
  establecimiento: string;
}

export interface BuscarPersonasResponse {
  personas: Persona[];
}

@Injectable({
  providedIn: 'root'
})
export class PersonaService {
  private apiUrl = '/api/personas'; // Cambia esto a la URL de tu API
  
  constructor(private http: HttpClient) { }

  buscarPorNombre(nombre: string): Observable<BuscarPersonasResponse> {
    return this.http.get<BuscarPersonasResponse>(`${this.apiUrl}/buscar/${nombre}`);
  }
}
