import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './components/auth/register/register.component';
import { LoginComponent } from './components/auth/login/login.component';
import { InicioComponent } from './components/inicio/inicio.component';
import { BuscarPersonasComponent } from './components/buscar-personas/buscar-personas.component';
import { PdfGeneratorComponent } from './components/pdfCertificado/pdf-generator/pdf-generator.component';
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
