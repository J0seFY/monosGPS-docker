package com.buscarpersonas.service;

import com.buscarpersonas.dto.ReporteSimcePaesDTO;
import com.buscarpersonas.Entity.Establecimiento;
import com.buscarpersonas.repository.ReporteSimcePaesRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReporteSimcePaesService {
    
    @Autowired
    private ReporteSimcePaesRepository reporteRepository;
    
    public ReporteSimcePaesDTO obtenerDatosReporte(Long establecimientoId) {
        // Obtener datos del establecimiento
        Optional<Establecimiento> establecimientoOpt = reporteRepository.findEstablecimientoById(establecimientoId);
        
        if (!establecimientoOpt.isPresent()) {
            throw new RuntimeException("Establecimiento no encontrado con ID: " + establecimientoId);
        }
        
        Establecimiento establecimiento = establecimientoOpt.get();
        
        // Crear DTO con datos del establecimiento
        ReporteSimcePaesDTO reporte = new ReporteSimcePaesDTO(
            establecimiento.getId(),
            establecimiento.getNombre(),
            establecimiento.getDireccion(),
            establecimiento.getComuna(),
            establecimiento.getTelefono()
        );
        
        // Obtener estadísticas de SIMCE
        Map<String, Object> estadisticasSimce = reporteRepository.getEstadisticasSimcePorEstablecimiento(establecimientoId);
        if (estadisticasSimce != null) {
            reporte.setCantidadEstudiantesSimce(((Number) estadisticasSimce.get("cantidad_estudiantes")).intValue());
            
            BigDecimal promedioSimce = new BigDecimal(estadisticasSimce.get("promedio").toString())
                .setScale(1, RoundingMode.HALF_UP);
            reporte.setPromedioSimce(promedioSimce);
            
            reporte.setPuntajeMaximoSimce(((Number) estadisticasSimce.get("puntaje_maximo")).intValue());
            reporte.setPuntajeMinimoSimce(((Number) estadisticasSimce.get("puntaje_minimo")).intValue());
        }
        
        // Obtener estadísticas de PAES
        Map<String, Object> estadisticasPaes = reporteRepository.getEstadisticasPaesPorEstablecimiento(establecimientoId);
        if (estadisticasPaes != null) {
            reporte.setCantidadEstudiantesPaes(((Number) estadisticasPaes.get("cantidad_estudiantes")).intValue());
            
            BigDecimal promedioPaes = new BigDecimal(estadisticasPaes.get("promedio").toString())
                .setScale(1, RoundingMode.HALF_UP);
            reporte.setPromedioPaes(promedioPaes);
            
            reporte.setPuntajeMaximoPaes(((Number) estadisticasPaes.get("puntaje_maximo")).intValue());
            reporte.setPuntajeMinimoPaes(((Number) estadisticasPaes.get("puntaje_minimo")).intValue());
        }
        
        return reporte;
    }
    
    public byte[] generarPDFReporte(Long establecimientoId) throws Exception {
        ReporteSimcePaesDTO reporte = obtenerDatosReporte(establecimientoId);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, baos);
        
        document.open();
        
        // Fuentes
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 10);
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        
        // Título principal
        Paragraph title = new Paragraph("INFORME DE RESULTADOS SIMCE Y PAES", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20f);
        document.add(title);
        
        // Información del establecimiento
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingAfter(20f);
        
        addInfoRow(infoTable, "Establecimiento:", reporte.getNombreEstablecimiento(), boldFont, normalFont);
        addInfoRow(infoTable, "Dirección:", reporte.getDireccionEstablecimiento(), boldFont, normalFont);
        addInfoRow(infoTable, "Comuna:", reporte.getComunaEstablecimiento(), boldFont, normalFont);
        addInfoRow(infoTable, "Teléfono:", reporte.getTelefonoEstablecimiento(), boldFont, normalFont);
        addInfoRow(infoTable, "Fecha de generación:", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), boldFont, normalFont);
        
        document.add(infoTable);
        
        // Sección SIMCE
        Paragraph simceHeader = new Paragraph("RESULTADOS SIMCE", headerFont);
        simceHeader.setSpacingAfter(10f);
        document.add(simceHeader);
        
        if (reporte.getCantidadEstudiantesSimce() != null && reporte.getCantidadEstudiantesSimce() > 0) {
            PdfPTable simceTable = new PdfPTable(2);
            simceTable.setWidthPercentage(100);
            simceTable.setSpacingAfter(15f);
            
            addInfoRow(simceTable, "Cantidad de estudiantes:", reporte.getCantidadEstudiantesSimce().toString(), boldFont, normalFont);
            addInfoRow(simceTable, "Puntaje promedio:", reporte.getPromedioSimce() != null ? reporte.getPromedioSimce().toString() + " pts" : "N/A", boldFont, normalFont);
            addInfoRow(simceTable, "Puntaje máximo:", reporte.getPuntajeMaximoSimce() != null ? reporte.getPuntajeMaximoSimce().toString() + " pts" : "N/A", boldFont, normalFont);
            addInfoRow(simceTable, "Puntaje mínimo:", reporte.getPuntajeMinimoSimce() != null ? reporte.getPuntajeMinimoSimce().toString() + " pts" : "N/A", boldFont, normalFont);
            
            document.add(simceTable);
            
            // Detalle de resultados SIMCE
            List<Map<String, Object>> detalleSimce = reporteRepository.getDetalleSimcePorEstablecimiento(establecimientoId);
            if (!detalleSimce.isEmpty()) {
                Paragraph detalleSimceHeader = new Paragraph("Detalle de Resultados SIMCE por Estudiante", boldFont);
                detalleSimceHeader.setSpacingAfter(10f);
                document.add(detalleSimceHeader);
                
                PdfPTable detalleTable = new PdfPTable(5);
                detalleTable.setWidthPercentage(100);
                detalleTable.setWidths(new float[]{20f, 25f, 25f, 15f, 15f});
                
                // Headers
                addTableHeader(detalleTable, "RUT", boldFont);
                addTableHeader(detalleTable, "Nombre", boldFont);
                addTableHeader(detalleTable, "Apellido", boldFont);
                addTableHeader(detalleTable, "Curso", boldFont);
                addTableHeader(detalleTable, "Puntaje", boldFont);
                
                // Datos
                for (Map<String, Object> estudiante : detalleSimce) {
                    detalleTable.addCell(new PdfPCell(new Phrase(estudiante.get("estudiante_rut").toString(), normalFont)));
                    detalleTable.addCell(new PdfPCell(new Phrase(estudiante.get("estudiante_nombre").toString(), normalFont)));
                    detalleTable.addCell(new PdfPCell(new Phrase(estudiante.get("estudiante_apellido").toString(), normalFont)));
                    detalleTable.addCell(new PdfPCell(new Phrase(estudiante.get("curso").toString(), normalFont)));
                    detalleTable.addCell(new PdfPCell(new Phrase(estudiante.get("puntaje").toString() + " pts", normalFont)));
                }
                
                document.add(detalleTable);
                document.add(new Paragraph(" ")); // Espacio
            }
        } else {
            Paragraph noSimce = new Paragraph("No hay resultados SIMCE registrados para este establecimiento.", normalFont);
            noSimce.setSpacingAfter(15f);
            document.add(noSimce);
        }
        
        // Sección PAES
        Paragraph paesHeader = new Paragraph("RESULTADOS PAES", headerFont);
        paesHeader.setSpacingAfter(10f);
        document.add(paesHeader);
        
        if (reporte.getCantidadEstudiantesPaes() != null && reporte.getCantidadEstudiantesPaes() > 0) {
            PdfPTable paesTable = new PdfPTable(2);
            paesTable.setWidthPercentage(100);
            paesTable.setSpacingAfter(15f);
            
            addInfoRow(paesTable, "Cantidad de estudiantes:", reporte.getCantidadEstudiantesPaes().toString(), boldFont, normalFont);
            addInfoRow(paesTable, "Puntaje promedio:", reporte.getPromedioPaes() != null ? reporte.getPromedioPaes().toString() + " pts" : "N/A", boldFont, normalFont);
            addInfoRow(paesTable, "Puntaje máximo:", reporte.getPuntajeMaximoPaes() != null ? reporte.getPuntajeMaximoPaes().toString() + " pts" : "N/A", boldFont, normalFont);
            addInfoRow(paesTable, "Puntaje mínimo:", reporte.getPuntajeMinimoPaes() != null ? reporte.getPuntajeMinimoPaes().toString() + " pts" : "N/A", boldFont, normalFont);
            
            document.add(paesTable);
            
            // Detalle de resultados PAES
            List<Map<String, Object>> detallePaes = reporteRepository.getDetallePaesPorEstablecimiento(establecimientoId);
            if (!detallePaes.isEmpty()) {
                Paragraph detallePaesHeader = new Paragraph("Detalle de Resultados PAES por Estudiante", boldFont);
                detallePaesHeader.setSpacingAfter(10f);
                document.add(detallePaesHeader);
                
                PdfPTable detalleTable = new PdfPTable(5);
                detalleTable.setWidthPercentage(100);
                detalleTable.setWidths(new float[]{20f, 25f, 25f, 15f, 15f});
                
                // Headers
                addTableHeader(detalleTable, "RUT", boldFont);
                addTableHeader(detalleTable, "Nombre", boldFont);
                addTableHeader(detalleTable, "Apellido", boldFont);
                addTableHeader(detalleTable, "Curso", boldFont);
                addTableHeader(detalleTable, "Puntaje", boldFont);
                
                // Datos
                for (Map<String, Object> estudiante : detallePaes) {
                    detalleTable.addCell(new PdfPCell(new Phrase(estudiante.get("estudiante_rut").toString(), normalFont)));
                    detalleTable.addCell(new PdfPCell(new Phrase(estudiante.get("estudiante_nombre").toString(), normalFont)));
                    detalleTable.addCell(new PdfPCell(new Phrase(estudiante.get("estudiante_apellido").toString(), normalFont)));
                    detalleTable.addCell(new PdfPCell(new Phrase(estudiante.get("curso").toString(), normalFont)));
                    detalleTable.addCell(new PdfPCell(new Phrase(estudiante.get("puntaje").toString() + " pts", normalFont)));
                }
                
                document.add(detalleTable);
            }
        } else {
            Paragraph noPaes = new Paragraph("No hay resultados PAES registrados para este establecimiento.", normalFont);
            document.add(noPaes);
        }
        
        document.close();
        return baos.toByteArray();
    }
    
    private void addInfoRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPaddingBottom(5f);
        table.addCell(labelCell);
        
        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "N/A", valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPaddingBottom(5f);
        table.addCell(valueCell);
    }
    
    private void addTableHeader(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(8f);
        table.addCell(cell);
    }
}