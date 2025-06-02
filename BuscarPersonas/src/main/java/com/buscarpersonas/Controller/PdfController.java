package com.buscarpersonas.Controller;

import com.buscarpersonas.dto.PersonaDTO;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    @PostMapping("/generar")
    public ResponseEntity<byte[]> generarPdf(@RequestBody PersonaDTO persona) throws IOException {
        ClassPathResource pdfTemplate = new ClassPathResource("templates/plantilla.pdf");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfReader reader = null;
        PdfStamper stamper = null;

        try {
            reader = new PdfReader(pdfTemplate.getInputStream());
            stamper = new PdfStamper(reader, baos);
            AcroFields form = stamper.getAcroFields();
            
            System.out.println("Campos en el PDF:");
            for (String key : form.getFields().keySet()) {
                System.out.println(key);
            }

            // Setear los campos que tienes en el PDF
            form.setField("tipo", persona.getTipo());
            form.setField("rut", persona.getRut());
            form.setField("nombre", persona.getNombre());
            form.setField("apellido", persona.getApellido());
            form.setField("telefono", persona.getTelefono());
            form.setField("establecimiento", persona.getEstablecimiento());

            stamper.setFormFlattening(true); // Para que no se pueda editar después

        } finally {
            if (stamper != null) stamper.close();
            if (reader != null) reader.close();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("ficha_persona.pdf").build());

        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
    }
}
