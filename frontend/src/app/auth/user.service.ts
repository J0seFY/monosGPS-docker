import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserDTO } from './register/register.component';
import { LoginDTO } from './login/login.component';
@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = 'http://monos.local/AuthService'; // URL de tu backend

  constructor(private http: HttpClient) {}

  register(user: LoginDTO): Observable<any> {
    return this.http.post(this.apiUrl, user);
  }

  login(user: {rut: string; password: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, user);
  }
}
