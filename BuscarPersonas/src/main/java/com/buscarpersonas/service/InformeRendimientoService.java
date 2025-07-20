package com.buscarpersonas.service;

import com.buscarpersonas.dto.RendimientoAsignaturaDTO;
import com.buscarpersonas.repository.RendimientoRepository;
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
public class InformeRendimientoService {

    @Autowired
    private RendimientoRepository rendimientoRepository;

    public byte[] generarInformeRendimientoPDF(String asignatura, BigDecimal promedioMinimo) 
            throws DocumentException, IOException {
        
        List<RendimientoAsignaturaDTO> rendimientos = obtenerRendimientoPorAsignatura(asignatura, promedioMinimo);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 20, 20, 30, 30);
        PdfWriter.getInstance(document, baos);

        document.open();

        // Título del documento
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);
        String titulo = asignatura != null ? 
            "INFORME DE RENDIMIENTO - " + asignatura.toUpperCase() :
            "INFORME GENERAL DE RENDIMIENTO POR ASIGNATURAS";
        Paragraph title = new Paragraph(titulo, titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Información del reporte
        Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.DARK_GRAY);
        Paragraph info = new Paragraph();
        info.add("Fecha de generación: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\n");
        info.add("Criterios aplicados:\n");
        if (asignatura != null) {
            info.add("• Asignatura: " + asignatura + "\n");
        } else {
            info.add("• Todas las asignaturas\n");
        }
        if (promedioMinimo != null) {
            info.add("• Promedio mínimo: " + promedioMinimo + "\n");
        }
        info.add("Total de registros: " + rendimientos.size() + "\n\n");
        info.setFont(infoFont);
        document.add(info);

        if (rendimientos.isEmpty()) {
            Paragraph noData = new Paragraph("No se encontraron datos de rendimiento que cumplan con los criterios especificados.", 
                    FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12));
            noData.setAlignment(Element.ALIGN_CENTER);
            document.add(noData);
        } else {
            // Agregar estadísticas generales
            addEstadisticasGenerales(document, asignatura);

            // Crear tabla
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Definir anchos de columnas
            float[] columnWidths = {20f, 25f, 15f, 10f, 15f, 15f};
            table.setWidths(columnWidths);

            // Headers
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.WHITE);
            String[] headers = {"RUT Estudiante", "Asignatura", "Promedio", "Cant. Notas", "Nota Máxima", "Nota Mínima"};
            
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(new BaseColor(52, 73, 94));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(6);
                table.addCell(cell);
            }

            // Datos
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
            Font excellentFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.GREEN);
            Font poorFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, BaseColor.RED);
            
            for (RendimientoAsignaturaDTO rendimiento : rendimientos) {
                // RUT
                table.addCell(createDataCell(rendimiento.getEstudianteRut(), dataFont));
                
                // Asignatura
                table.addCell(createDataCell(rendimiento.getAsignatura(), dataFont));
                
                // Promedio (resaltar según el rendimiento)
                Font promedioFont = dataFont;
                if (rendimiento.getPromedioNotas().compareTo(new BigDecimal("6.0")) >= 0) {
                    promedioFont = excellentFont;
                } else if (rendimiento.getPromedioNotas().compareTo(new BigDecimal("4.0")) < 0) {
                    promedioFont = poorFont;
                }
                table.addCell(createDataCell(String.format("%.1f", rendimiento.getPromedioNotas()), promedioFont));
                
                // Cantidad de notas
                table.addCell(createDataCell(rendimiento.getCantidadNotas().toString(), dataFont));
                
                // Nota máxima
                table.addCell(createDataCell(String.format("%.1f", rendimiento.getNotaMaxima()), dataFont));
                
                // Nota mínima
                table.addCell(createDataCell(String.format("%.1f", rendimiento.getNotaMinima()), dataFont));
            }

            document.add(table);

            // Resumen por asignatura (solo si no se especificó una asignatura)
            if (asignatura == null) {
                addResumenPorAsignatura(document, rendimientos);
            }
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
        cell.setPadding(4);
        cell.setBorderColor(BaseColor.LIGHT_GRAY);
        return cell;
    }

    private void addEstadisticasGenerales(Document document, String asignatura) throws DocumentException {
        List<Object[]> estadisticas = rendimientoRepository.findEstadisticasGeneralesPorAsignatura(asignatura);
        
        if (!estadisticas.isEmpty()) {
            Paragraph estadisticasTitle = new Paragraph("Estadísticas Generales", 
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
            estadisticasTitle.setSpacingBefore(10);
            document.add(estadisticasTitle);

            Font estadisticasFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            for (Object[] estadistica : estadisticas) {
                String asig = (String) estadistica[0];
                BigDecimal promedio = new BigDecimal(estadistica[1].toString());
                Long totalEstudiantes = ((Number) estadistica[2]).longValue();
                Long totalNotas = ((Number) estadistica[3]).longValue();
                
                Paragraph stat = new Paragraph(String.format("• %s: Promedio %.1f - %d estudiantes - %d notas registradas", 
                        asig, promedio, totalEstudiantes, totalNotas), estadisticasFont);
                document.add(stat);
            }
            
            // Espacio adicional
            document.add(new Paragraph(" "));
        }
    }

    private void addResumenPorAsignatura(Document document, List<RendimientoAsignaturaDTO> rendimientos) 
            throws DocumentException {
        
        // Contar por asignatura
        java.util.Map<String, Long> resumenPorAsignatura = rendimientos.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    RendimientoAsignaturaDTO::getAsignatura,
                    java.util.stream.Collectors.counting()
                ));

        Paragraph resumenTitle = new Paragraph("\nResumen por Asignatura", 
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
        resumenTitle.setSpacingBefore(20);
        document.add(resumenTitle);

        Font resumenFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        for (java.util.Map.Entry<String, Long> entry : resumenPorAsignatura.entrySet()) {
            Paragraph resumen = new Paragraph("• " + entry.getKey() + ": " + entry.getValue() + " estudiante(s)", resumenFont);
            document.add(resumen);
        }
    }

    public List<RendimientoAsignaturaDTO> obtenerRendimientoPorAsignatura(String asignatura, BigDecimal promedioMinimo) {
        List<Object[]> resultados = rendimientoRepository.findRendimientoPorAsignaturaRaw(asignatura, promedioMinimo);
        List<RendimientoAsignaturaDTO> rendimientos = new ArrayList<>();
        
        for (Object[] resultado : resultados) {
            RendimientoAsignaturaDTO rendimiento = new RendimientoAsignaturaDTO(
                (String) resultado[0],  // estudiante_rut
                (String) resultado[1],  // asignatura
                new BigDecimal(resultado[2].toString()),  // promedio_notas
                ((Number) resultado[3]).intValue(),  // cantidad_notas
                new BigDecimal(resultado[4].toString()),  // nota_maxima
                new BigDecimal(resultado[5].toString())   // nota_minima
            );
            rendimientos.add(rendimiento);
        }
        
        return rendimientos;
    }

    public List<String> obtenerTodasLasAsignaturas() {
        return rendimientoRepository.findAllAsignaturas();
    }
}