import { Component } from '@angular/core';
import { Persona } from '../../../servicios/persona.service';
import { PdfService } from '../../../servicios/pdf.service';
import { PersonaService } from '../../../servicios/persona.service';
import { NgModel } from '@angular/forms';
@Component({
  selector: 'app-pdf-generator',
  templateUrl: './pdf-generator.component.html',
  styleUrls: ['./pdf-generator.component.css']
})
export class PdfGeneratorComponent {

  persona : Persona = {
    tipo: '',
    rut: '',
    nombre: '',
    apellido: '',
    telefono: '',
    establecimiento: ''
  };

  constructor(private pdfService: PdfService) { }

  generarPDF() {
    this.pdfService.generarPdf(this.persona).subscribe(blob => {
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = 'ficha_persona.pdf';
      link.click();
      window.URL.revokeObjectURL(url);
    }, error => {
      console.error('Error generando el PDF:', error);
      alert('Hubo un error al generar el PDF.');
    });
  }
}
