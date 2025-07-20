// service/ReporteMatriculaService.java
package com.buscarpersonas.service;

import com.buscarpersonas.Entity.Establecimiento;
import com.buscarpersonas.Entity.Estudiante;
import com.buscarpersonas.repository.EstablecimientoRepository;
import com.buscarpersonas.repository.ReporteMatriculaRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ReporteMatriculaService {
    
    @Autowired
    private ReporteMatriculaRepository reporteRepository;
    
    @Autowired
    private EstablecimientoRepository establecimientoRepository;
    
    public ByteArrayOutputStream generarReporteMatriculasPDF(String comuna) {
        try {
            // Obtener establecimientos de la comuna
            List<Establecimiento> establecimientos = establecimientoRepository.findByComuna(comuna);
            
            // Crear documento PDF
            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            
            document.open();
            
            // Título del reporte
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("REPORTE DE MATRÍCULAS COMUNALES", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
            
            // Información general
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            
            document.add(new Paragraph("Comuna: " + comuna, headerFont));
            document.add(new Paragraph("Fecha de generación: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), normalFont));
            document.add(new Paragraph("Total de establecimientos: " + establecimientos.size(), normalFont));
            document.add(new Paragraph("\n"));
            
            // Procesar cada establecimiento
            int totalEstudiantesComunal = 0;
            for (Establecimiento est : establecimientos) {
                int totalEstudiantes = reporteRepository.countEstudiantesByEstablecimiento(est.getId());
                totalEstudiantesComunal += totalEstudiantes;
                
                // Información del establecimiento
                document.add(new Paragraph("ESTABLECIMIENTO: " + est.getNombre(), headerFont));
                document.add(new Paragraph("Dirección: " + est.getDireccion(), normalFont));
                document.add(new Paragraph("Teléfono: " + (est.getTelefono() != null ? est.getTelefono() : "No disponible"), normalFont));
                document.add(new Paragraph("Total de estudiantes: " + totalEstudiantes, normalFont));
                
                // Tabla de estudiantes por curso
                List<Object[]> estudiantesPorCurso = reporteRepository.countEstudiantesByCursoAndEstablecimiento(est.getId());
                if (!estudiantesPorCurso.isEmpty()) {
                    PdfPTable cursoTable = new PdfPTable(2);
                    cursoTable.setWidthPercentage(50);
                    cursoTable.setSpacingBefore(10);
                    
                    // Headers
                    PdfPCell cellCurso = new PdfPCell(new Phrase("Curso", headerFont));
                    PdfPCell cellCantidad = new PdfPCell(new Phrase("Cantidad", headerFont));
                    cellCurso.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cellCantidad.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cursoTable.addCell(cellCurso);
                    cursoTable.addCell(cellCantidad);
                    
                    // Datos
                    for (Object[] row : estudiantesPorCurso) {
                        cursoTable.addCell(new PdfPCell(new Phrase(row[0].toString(), normalFont)));
                        cursoTable.addCell(new PdfPCell(new Phrase(row[1].toString(), normalFont)));
                    }
                    
                    document.add(cursoTable);
                }
                
                document.add(new Paragraph("\n"));
            }
            
            // Resumen final
            document.add(new Paragraph("RESUMEN GENERAL", headerFont));
            document.add(new Paragraph("Total de estudiantes en la comuna: " + totalEstudiantesComunal, normalFont));
            
            document.close();
            return baos;
            
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el reporte PDF: " + e.getMessage(), e);
        }
    }
}