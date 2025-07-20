package com.buscarpersonas.service;

import com.buscarpersonas.dto.ReporteSimceDTO;
import com.buscarpersonas.repository.ReporteSimceRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la generación de reportes SIMCE
 */
@Service
public class ReporteSimceService {
    
    private static final Logger logger = LoggerFactory.getLogger(ReporteSimceService.class);
    
    @Autowired
    private ReporteSimceRepository reporteSimceRepository;
    
    // Colores y estilos para el PDF
    private static final BaseColor COLOR_HEADER = new BaseColor(41, 128, 185);  // Azul
    private static final BaseColor COLOR_ACCENT = new BaseColor(52, 73, 94);    // Gris oscuro
    private static final BaseColor COLOR_LIGHT_GRAY = new BaseColor(236, 240, 241);
    
    /**
     * Genera el reporte SIMCE para un establecimiento específico
     */
    public byte[] generarReporteSimcePDF(Long establecimientoId) throws Exception {
        logger.info("Iniciando generación de reporte SIMCE para establecimiento ID: {}", establecimientoId);
        
        // Verificar que el establecimiento existe
        if (!existeEstablecimiento(establecimientoId)) {
            throw new IllegalArgumentException("No existe un establecimiento con ID: " + establecimientoId);
        }
        
        // Obtener datos del reporte
        ReporteSimceDTO reporteData = obtenerDatosReporte(establecimientoId);
        
        // Generar PDF
        return generarPDF(reporteData, establecimientoId);
    }
    
    /**
     * Verifica si existe un establecimiento
     */
    private boolean existeEstablecimiento(Long establecimientoId) {
        Integer count = reporteSimceRepository.existeEstablecimiento(establecimientoId);
        return count != null && count > 0;
    }
    
    /**
     * Obtiene los datos del reporte desde la base de datos
     */
    private ReporteSimceDTO obtenerDatosReporte(Long establecimientoId) {
        Optional<Object[]> result = reporteSimceRepository.findDatosReporteSimceByEstablecimiento(establecimientoId);
        
        if (result.isPresent()) {
            Object[] data = result.get();
            return new ReporteSimceDTO(
                ((Number) data[0]).longValue(),     // establecimientoId
                (String) data[1],                   // nombreEstablecimiento
                (String) data[2],                   // direccionEstablecimiento
                (String) data[3],                   // comunaEstablecimiento
                (String) data[4],                   // telefonoEstablecimiento
                data[5] != null ? ((Number) data[5]).intValue() : 0,  // totalEstudiantesSimce
                data[6] != null ? new BigDecimal(data[6].toString()) : BigDecimal.ZERO,  // puntajePromedio
                data[7] != null ? ((Number) data[7]).intValue() : null,  // puntajeMaximo
                data[8] != null ? ((Number) data[8]).intValue() : null   // puntajeMinimo
            );
        } else {
            throw new RuntimeException("No se encontraron datos para el establecimiento ID: " + establecimientoId);
        }
    }
    
    /**
     * Genera el archivo PDF del reporte
     */
    private byte[] generarPDF(ReporteSimceDTO reporte, Long establecimientoId) throws DocumentException, IOException {
        logger.info("Generando PDF para establecimiento: {}", reporte.getNombreEstablecimiento());
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        
        document.open();
        
        try {
            // Agregar contenido al PDF
            agregarEncabezado(document, reporte);
            agregarInformacionEstablecimiento(document, reporte);
            agregarResumenResultados(document, reporte);
            
            if (reporte.tieneResultados()) {
                agregarDetalleEstudiantes(document, establecimientoId);
                agregarDistribucionNiveles(document, establecimientoId);
                agregarEstadisticasAdicionales(document, establecimientoId);
            } else {
                agregarMensajeSinResultados(document);
            }
            
            agregarPiePagina(document);
            
        } finally {
            document.close();
        }
        
        logger.info("PDF generado exitosamente. Tamaño: {} bytes", outputStream.size());
        return outputStream.toByteArray();
    }
    
    /**
     * Agrega el encabezado del documento
     */
    private void agregarEncabezado(Document document, ReporteSimceDTO reporte) throws DocumentException {
        // Título principal
        Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, COLOR_HEADER);
        Paragraph titulo = new Paragraph("REPORTE DE RESULTADOS SIMCE", tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(10);
        document.add(titulo);
        
        // Subtítulo
        Font subtituloFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, COLOR_ACCENT);
        Paragraph subtitulo = new Paragraph("Sistema de Medición de la Calidad de la Educación", subtituloFont);
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        subtitulo.setSpacingAfter(20);
        document.add(subtitulo);
        
        // Línea separadora
        document.add(new Paragraph(" "));
    }
    
    /**
     * Agrega la información del establecimiento
     */
    private void agregarInformacionEstablecimiento(Document document, ReporteSimceDTO reporte) throws DocumentException {
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, COLOR_HEADER);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
        
        // Título de sección
        Paragraph seccionTitulo = new Paragraph("INFORMACIÓN DEL ESTABLECIMIENTO", headerFont);
        seccionTitulo.setSpacingBefore(15);
        seccionTitulo.setSpacingAfter(10);
        document.add(seccionTitulo);
        
        // Tabla de información
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{30f, 70f});
        
        // Configurar celdas
        addCellToTable(table, "Nombre:", reporte.getNombreEstablecimiento(), true, normalFont);
        addCellToTable(table, "Dirección:", reporte.getDireccionEstablecimiento(), false, normalFont);
        addCellToTable(table, "Comuna:", reporte.getComunaEstablecimiento(), true, normalFont);
        addCellToTable(table, "Teléfono:", reporte.getTelefonoEstablecimiento(), false, normalFont);
        addCellToTable(table, "Fecha del Reporte:", reporte.getFechaGeneracion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), true, normalFont);
        
        document.add(table);
        document.add(new Paragraph(" "));
    }
    
    /**
     * Agrega el resumen de resultados SIMCE
     */
    private void agregarResumenResultados(Document document, ReporteSimceDTO reporte) throws DocumentException {
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, COLOR_HEADER);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
        Font highlightFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, COLOR_ACCENT);
        
        // Título de sección
        Paragraph seccionTitulo = new Paragraph("RESUMEN DE RESULTADOS SIMCE", headerFont);
        seccionTitulo.setSpacingBefore(15);
        seccionTitulo.setSpacingAfter(10);
        document.add(seccionTitulo);
        
        // Tabla de resumen
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{50f, 50f});
        
        addCellToTable(table, "Total de Estudiantes Evaluados:", 
                      reporte.getTotalEstudiantesSimce() != null ? reporte.getTotalEstudiantesSimce().toString() : "0", 
                      true, normalFont);
        
        addCellToTable(table, "Puntaje Promedio:", 
                      reporte.getPuntajePromedioFormateado() + " pts", 
                      false, highlightFont);
        
        if (reporte.getPuntajeMaximo() != null) {
            addCellToTable(table, "Puntaje Máximo:", reporte.getPuntajeMaximo().toString() + " pts", true, normalFont);
        }
        
        if (reporte.getPuntajeMinimo() != null) {
            addCellToTable(table, "Puntaje Mínimo:", reporte.getPuntajeMinimo().toString() + " pts", false, normalFont);
        }
        
        document.add(table);
        document.add(new Paragraph(" "));
    }
    
    /**
     * Agrega el detalle de estudiantes evaluados
     */
    private void agregarDetalleEstudiantes(Document document, Long establecimientoId) throws DocumentException {
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, COLOR_HEADER);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);
        Font tableHeaderFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.WHITE);
        
        List<Object[]> estudiantes = reporteSimceRepository.findDetalleEstudiantesSimceByEstablecimiento(establecimientoId);
        
        if (estudiantes.isEmpty()) {
            return;
        }
        
        // Título de sección
        Paragraph seccionTitulo = new Paragraph("DETALLE DE ESTUDIANTES EVALUADOS", headerFont);
        seccionTitulo.setSpacingBefore(15);
        seccionTitulo.setSpacingAfter(10);
        document.add(seccionTitulo);
        
        // Tabla de estudiantes
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{25f, 25f, 25f, 15f, 10f});
        
        // Headers
        addHeaderCell(table, "RUT", tableHeaderFont);
        addHeaderCell(table, "Nombre", tableHeaderFont);
        addHeaderCell(table, "Apellido", tableHeaderFont);
        addHeaderCell(table, "Curso", tableHeaderFont);
        addHeaderCell(table, "Puntaje", tableHeaderFont);
        
        // Datos
        for (Object[] estudiante : estudiantes) {
            addDataCell(table, (String) estudiante[0], normalFont);  // RUT
            addDataCell(table, (String) estudiante[1], normalFont);  // Nombre
            addDataCell(table, (String) estudiante[2], normalFont);  // Apellido
            addDataCell(table, (String) estudiante[3], normalFont);  // Curso
            addDataCell(table, estudiante[4] != null ? estudiante[4].toString() : "N/A", normalFont);  // Puntaje
        }
        
        document.add(table);
        document.add(new Paragraph(" "));
    }
    
    /**
     * Agrega la distribución por niveles de logro
     */
    private void agregarDistribucionNiveles(Document document, Long establecimientoId) throws DocumentException {
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, COLOR_HEADER);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
        Font tableHeaderFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
        
        List<Object[]> distribucion = reporteSimceRepository.findDistribucionPorNivelesLogro(establecimientoId);
        
        if (distribucion.isEmpty()) {
            return;
        }
        
        // Título de sección
        Paragraph seccionTitulo = new Paragraph("DISTRIBUCIÓN POR NIVELES DE LOGRO", headerFont);
        seccionTitulo.setSpacingBefore(15);
        seccionTitulo.setSpacingAfter(10);
        document.add(seccionTitulo);
        
        // Tabla de distribución
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(60);
        table.setWidths(new float[]{70f, 30f});
        
        // Headers
        addHeaderCell(table, "Nivel de Logro", tableHeaderFont);
        addHeaderCell(table, "Cantidad", tableHeaderFont);
        
        // Datos
        for (Object[] nivel : distribucion) {
            addDataCell(table, (String) nivel[0], normalFont);  // Nivel de logro
            addDataCell(table, nivel[1].toString(), normalFont);  // Cantidad
        }
        
        document.add(table);
        document.add(new Paragraph(" "));
    }
    
    /**
     * Agrega estadísticas adicionales
     */
    private void agregarEstadisticasAdicionales(Document document, Long establecimientoId) throws DocumentException {
        Optional<Object[]> estadisticas = reporteSimceRepository.findEstadisticasAdicionales(establecimientoId);
        
        if (!estadisticas.isPresent()) {
            return;
        }
        
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, COLOR_HEADER);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
        
        Object[] data = estadisticas.get();
        
        // Título de sección
        Paragraph seccionTitulo = new Paragraph("ESTADÍSTICAS ADICIONALES", headerFont);
        seccionTitulo.setSpacingBefore(15);
        seccionTitulo.setSpacingAfter(10);
        document.add(seccionTitulo);
        
        // Tabla de estadísticas
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{60f, 40f});
        
        addCellToTable(table, "Total de Pruebas Aplicadas:", data[0] != null ? data[0].toString() : "0", true, normalFont);
        addCellToTable(table, "Fechas de Evaluación Diferentes:", data[1] != null ? data[1].toString() : "0", false, normalFont);
        
        if (data[2] != null && data[3] != null) {
            addCellToTable(table, "Periodo de Evaluación:", data[2].toString() + " - " + data[3].toString(), true, normalFont);
        }
        
        document.add(table);
    }
    
    /**
     * Agrega mensaje cuando no hay resultados
     */
    private void agregarMensajeSinResultados(Document document) throws DocumentException {
        Font messageFont = new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC, COLOR_ACCENT);
        
        Paragraph mensaje = new Paragraph("No se encontraron resultados SIMCE para este establecimiento.", messageFont);
        mensaje.setAlignment(Element.ALIGN_CENTER);
        mensaje.setSpacingBefore(30);
        mensaje.setSpacingAfter(30);
        document.add(mensaje);
    }
    
    /**
     * Agrega el pie de página del documento
     */
    private void agregarPiePagina(Document document) throws DocumentException {
        Font pieFont = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.GRAY);
        
        Paragraph pie = new Paragraph("Reporte generado automáticamente por el Sistema de Gestión Educacional - " + 
                                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), pieFont);
        pie.setAlignment(Element.ALIGN_CENTER);
        pie.setSpacingBefore(30);
        document.add(pie);
    }
    
    /**
     * Método auxiliar para agregar celdas a una tabla
     */
    private void addCellToTable(PdfPTable table, String label, String value, boolean isEven, Font font) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, font));
        PdfPCell valueCell = new PdfPCell(new Phrase(value, font));
        
        if (isEven) {
            labelCell.setBackgroundColor(COLOR_LIGHT_GRAY);
            valueCell.setBackgroundColor(COLOR_LIGHT_GRAY);
        }
        
        labelCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(8);
        valueCell.setPadding(8);
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
    
    /**
     * Método auxiliar para agregar celdas de encabezado
     */
    private void addHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(COLOR_HEADER);
        cell.setPadding(8);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }
    
    /**
     * Método auxiliar para agregar celdas de datos
     */
    private void addDataCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(6);
        cell.setBorderColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
    }
    
    /**
     * Obtiene todos los reportes SIMCE (método opcional para reportes generales)
     */
    public List<ReporteSimceDTO> obtenerTodosLosReportesSimce() {
        List<Object[]> results = reporteSimceRepository.findAllDatosReporteSimce();
        List<ReporteSimceDTO> reportes = new ArrayList<>();
        
        for (Object[] data : results) {
            reportes.add(new ReporteSimceDTO(
                ((Number) data[0]).longValue(),     // establecimientoId
                (String) data[1],                   // nombreEstablecimiento
                (String) data[2],                   // direccionEstablecimiento
                (String) data[3],                   // comunaEstablecimiento
                (String) data[4],                   // telefonoEstablecimiento
                data[5] != null ? ((Number) data[5]).intValue() : 0,  // totalEstudiantesSimce
                data[6] != null ? new BigDecimal(data[6].toString()) : BigDecimal.ZERO,  // puntajePromedio
                data[7] != null ? ((Number) data[7]).intValue() : null,  // puntajeMaximo
                data[8] != null ? ((Number) data[8]).intValue() : null   // puntajeMinimo
            ));
        }
        
        return reportes;
    }
}