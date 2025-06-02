import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './auth/register/register.component';
import { LoginComponent } from './auth/login/login.component';
import { InicioComponent } from './auth/inicio/inicio.component';
import { BuscarPersonasComponent } from './paginas/buscar-personas/buscar-personas.component';
import { PdfGeneratorComponent } from './pdfCertificado/pdf-generator/pdf-generator.component';

const routes: Routes = [
  { path: '', redirectTo: 'inicio', pathMatch: 'full' },
  {path:'inicio', component: InicioComponent},
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: 'CertificadoAlumno', component: PdfGeneratorComponent },
  { path: 'buscarPersonas', component: BuscarPersonasComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
