package com.buscarpersonas.service;

import com.buscarpersonas.dto.EstudianteExtranjeroDTO;
import com.buscarpersonas.repository.EstudianteExRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReporteEstudianteExtranjeroService {
    
    @Autowired
    private EstudianteExRepository estudianteRepository;

    public byte[] generarReporteEstudiantesExtranjeros() throws DocumentException {
        // Obtener datos de estudiantes extranjeros
        List<EstudianteExtranjeroDTO> estudiantesExtranjeros = estudianteRepository.findEstudiantesExtranjeros();
        
        // Crear documento PDF
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        
        document.open();
        
        // Título del documento
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
        Paragraph title = new Paragraph("INFORME DE ESTUDIANTES EXTRANJEROS", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        // Fecha de generación
        Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
        Paragraph dateP = new Paragraph("Generado el: " + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), dateFont);
        dateP.setAlignment(Element.ALIGN_CENTER);
        dateP.setSpacingAfter(20);
        document.add(dateP);
        
        // Información general
        Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);
        Paragraph info = new Paragraph("Total de estudiantes extranjeros: " + estudiantesExtranjeros.size(), infoFont);
        info.setSpacingAfter(15);
        document.add(info);
        
        // Tabla de datos
        if (!estudiantesExtranjeros.isEmpty()) {
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3f, 2f, 1.5f, 2f, 3f});
            
            // Headers
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
            
            PdfPCell headerNombre = new PdfPCell(new Phrase("Nombre Completo", headerFont));
            headerNombre.setBackgroundColor(new BaseColor(52, 73, 94));
            headerNombre.setPadding(8);
            table.addCell(headerNombre);
            
            PdfPCell headerRut = new PdfPCell(new Phrase("RUT", headerFont));
            headerRut.setBackgroundColor(new BaseColor(52, 73, 94));
            headerRut.setPadding(8);
            table.addCell(headerRut);
            
            PdfPCell headerCurso = new PdfPCell(new Phrase("Curso", headerFont));
            headerCurso.setBackgroundColor(new BaseColor(52, 73, 94));
            headerCurso.setPadding(8);
            table.addCell(headerCurso);
            
            PdfPCell headerNacionalidad = new PdfPCell(new Phrase("Nacionalidad", headerFont));
            headerNacionalidad.setBackgroundColor(new BaseColor(52, 73, 94));
            headerNacionalidad.setPadding(8);
            table.addCell(headerNacionalidad);
            
            PdfPCell headerEstablecimiento = new PdfPCell(new Phrase("Establecimiento", headerFont));
            headerEstablecimiento.setBackgroundColor(new BaseColor(52, 73, 94));
            headerEstablecimiento.setPadding(8);
            table.addCell(headerEstablecimiento);
            
            // Datos
            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);
            
            for (EstudianteExtranjeroDTO estudiante : estudiantesExtranjeros) {
                PdfPCell cellNombre = new PdfPCell(new Phrase(estudiante.getNombreCompleto(), cellFont));
                cellNombre.setPadding(5);
                table.addCell(cellNombre);
                
                PdfPCell cellRut = new PdfPCell(new Phrase(estudiante.getRut(), cellFont));
                cellRut.setPadding(5);
                table.addCell(cellRut);
                
                PdfPCell cellCurso = new PdfPCell(new Phrase(estudiante.getCurso() != null ? estudiante.getCurso() : "", cellFont));
                cellCurso.setPadding(5);
                table.addCell(cellCurso);
                
                PdfPCell cellNacionalidad = new PdfPCell(new Phrase(estudiante.getNacionalidad(), cellFont));
                cellNacionalidad.setPadding(5);
                table.addCell(cellNacionalidad);
                
                PdfPCell cellEstablecimiento = new PdfPCell(new Phrase(estudiante.getEstablecimiento(), cellFont));
                cellEstablecimiento.setPadding(5);
                table.addCell(cellEstablecimiento);
            }
            
            document.add(table);
        } else {
            Paragraph noData = new Paragraph("No se encontraron estudiantes extranjeros.", infoFont);
            document.add(noData);
        }
        
        document.close();
        return baos.toByteArray();
    }
}
