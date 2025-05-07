// src/app/services/user.service.ts

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserDTO } from './register/register.component';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = 'http://localhost:8081/AuthService'; // URL de tu backend

  constructor(private http: HttpClient) {}

  // Método para registrar al usuario
  register(user: UserDTO): Observable<any> {
    return this.http.post(this.apiUrl, user);
  }
}
