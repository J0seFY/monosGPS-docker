package com.buscarpersonas.service;

import com.buscarpersonas.dto.EstudianteRepitenciaDTO;
import com.buscarpersonas.repository.EstudianteRepitenciaRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

@Service
public class InformeRepitenciaService {

    @Autowired
    private EstudianteRepitenciaRepository estudianteRepitenciaRepository;

    public byte[] generarInformeRepitenciaPDF(BigDecimal promedioMinimo, BigDecimal asistenciaMinima) 
            throws DocumentException, IOException {
        
        List<EstudianteRepitenciaDTO> estudiantes = obtenerEstudiantesConRiesgo(promedioMinimo, asistenciaMinima);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 20, 20, 30, 30);
        PdfWriter.getInstance(document, baos);

        document.open();

        // Título del documento
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
        Paragraph title = new Paragraph("INFORME DE ESTUDIANTES CON POSIBLE REPITENCIA", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Información del reporte
        Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.DARK_GRAY);
        Paragraph info = new Paragraph();
        info.add("Fecha de generación: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n");
        info.add("Criterios aplicados:\n");
        info.add("• Promedio de notas menor a: " + promedioMinimo + "\n");
        info.add("• Porcentaje de asistencia menor a: " + asistenciaMinima + "%\n");
        info.add("Total de estudiantes identificados: " + estudiantes.size() + "\n\n");
        info.setFont(infoFont);
        document.add(info);

        if (estudiantes.isEmpty()) {
            Paragraph noData = new Paragraph("No se encontraron estudiantes que cumplan con los criterios de riesgo de repitencia.", 
                    FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12));
            noData.setAlignment(Element.ALIGN_CENTER);
            document.add(noData);
        } else {
            // Crear tabla
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Definir anchos de columnas
            float[] columnWidths = {15f, 25f, 10f, 12f, 13f, 25f};
            table.setWidths(columnWidths);

            // Headers
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.WHITE);
            String[] headers = {"RUT", "Nombre Completo", "Curso", "Promedio", "Asistencia", "Establecimiento"};
            
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(new BaseColor(52, 73, 94));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                table.addCell(cell);
            }

            // Datos
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
            Font alertFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.RED);
            
            for (EstudianteRepitenciaDTO estudiante : estudiantes) {
                // RUT
                table.addCell(createDataCell(estudiante.getRut(), dataFont));
                
                // Nombre completo
                table.addCell(createDataCell(estudiante.getNombreCompleto(), dataFont));
                
                // Curso
                table.addCell(createDataCell(estudiante.getCurso(), dataFont));
                
                // Promedio (resaltar si es bajo)
                Font promedioFont = estudiante.getPromedioNotas().compareTo(promedioMinimo) < 0 ? alertFont : dataFont;
                table.addCell(createDataCell(String.format("%.1f", estudiante.getPromedioNotas()), promedioFont));
                
                // Asistencia (resaltar si es baja)
                Font asistenciaFont = estudiante.getPorcentajeAsistencia().compareTo(asistenciaMinima) < 0 ? alertFont : dataFont;
                table.addCell(createDataCell(String.format("%.1f%%", estudiante.getPorcentajeAsistencia()), asistenciaFont));
                
                // Establecimiento
                table.addCell(createDataCell(estudiante.getNombreEstablecimiento(), dataFont));
            }

            document.add(table);

            // Resumen por establecimiento
            addResumenPorEstablecimiento(document, estudiantes);
        }

        // Pie de página
        Paragraph footer = new Paragraph("\nReporte generado automáticamente por el Sistema de Gestión Digital Comunal", 
                FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, BaseColor.GRAY));
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();
        return baos.toByteArray();
    }

    private PdfPCell createDataCell(String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content != null ? content : "", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(6);
        cell.setBorderColor(BaseColor.LIGHT_GRAY);
        return cell;
    }

    private void addResumenPorEstablecimiento(Document document, List<EstudianteRepitenciaDTO> estudiantes) 
            throws DocumentException {
        
        // Contar por establecimiento
        java.util.Map<String, Long> resumenPorEstablecimiento = estudiantes.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    EstudianteRepitenciaDTO::getNombreEstablecimiento,
                    java.util.stream.Collectors.counting()
                ));

        Paragraph resumenTitle = new Paragraph("\nResumen por Establecimiento", 
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
        resumenTitle.setSpacingBefore(20);
        document.add(resumenTitle);

        Font resumenFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        for (java.util.Map.Entry<String, Long> entry : resumenPorEstablecimiento.entrySet()) {
            Paragraph resumen = new Paragraph("• " + entry.getKey() + ": " + entry.getValue() + " estudiante(s)", resumenFont);
            document.add(resumen);
        }
    }

    public List<EstudianteRepitenciaDTO> obtenerEstudiantesConRiesgo(BigDecimal promedioMinimo, BigDecimal asistenciaMinima) {
        List<Object[]> resultados = estudianteRepitenciaRepository.findEstudiantesConRiesgoRepitenciaRaw(promedioMinimo, asistenciaMinima);
        List<EstudianteRepitenciaDTO> estudiantes = new ArrayList<>();
        
        for (Object[] resultado : resultados) {
            EstudianteRepitenciaDTO estudiante = new EstudianteRepitenciaDTO(
                (String) resultado[0],  // rut
                (String) resultado[1],  // nombre
                (String) resultado[2],  // apellido
                (String) resultado[3],  // curso
                new BigDecimal(resultado[4].toString()),  // promedio_notas
                new BigDecimal(resultado[5].toString()),  // porcentaje_asistencia
                (String) resultado[6]   // nombre_establecimiento
            );
            estudiantes.add(estudiante);
        }
        
        return estudiantes;
    }
}