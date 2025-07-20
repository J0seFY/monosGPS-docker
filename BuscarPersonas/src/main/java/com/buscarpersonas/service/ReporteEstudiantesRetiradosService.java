package com.buscarpersonas.service;

import com.buscarpersonas.dto.EstudianteRetiradoDTO;
import com.buscarpersonas.repository.ReporteEstudiantesRetiradosRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReporteEstudiantesRetiradosService {

    private static final Logger logger = LoggerFactory.getLogger(ReporteEstudiantesRetiradosService.class);

    @Autowired
    private ReporteEstudiantesRetiradosRepository reporteEstudiantesRetiradosRepository;

    /**
     * Genera el reporte PDF de estudiantes retirados
     */
    public byte[] generarReportePDF() throws Exception {
        logger.info("Iniciando generación de reporte PDF de estudiantes retirados");

        List<EstudianteRetiradoDTO> estudiantesRetirados = reporteEstudiantesRetiradosRepository.findEstudiantesRetirados();

        if (estudiantesRetirados.isEmpty()) {
            logger.warn("No se encontraron estudiantes retirados para el reporte");
            throw new RuntimeException("No hay estudiantes retirados para generar el reporte");
        }

        return crearPDF(estudiantesRetirados);
    }

    /**
     * Genera el reporte PDF de estudiantes retirados por establecimiento
     */
    public byte[] generarReportePDFPorEstablecimiento(Long establecimientoId) throws Exception {
        logger.info("Iniciando generación de reporte PDF de estudiantes retirados para establecimiento: {}", establecimientoId);

        List<EstudianteRetiradoDTO> estudiantesRetirados = reporteEstudiantesRetiradosRepository.findEstudiantesRetiradosByEstablecimiento(establecimientoId);

        if (estudiantesRetirados.isEmpty()) {
            logger.warn("No se encontraron estudiantes retirados para el establecimiento: {}", establecimientoId);
            throw new RuntimeException("No hay estudiantes retirados para este establecimiento");
        }

        return crearPDF(estudiantesRetirados);
    }

    /**
     * Crea el PDF con la lista de estudiantes retirados
     */
    private byte[] crearPDF(List<EstudianteRetiradoDTO> estudiantes) throws Exception {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Título del documento
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("REPORTE DE ESTUDIANTES RETIRADOS", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Información del reporte
            Font infoFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            LocalDateTime now = LocalDateTime.now();
            String fechaGeneracion = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            
            Paragraph info = new Paragraph("Fecha de generación: " + fechaGeneracion, infoFont);
            info.setAlignment(Element.ALIGN_RIGHT);
            info.setSpacingAfter(10);
            document.add(info);

            Paragraph totalEstudiantes = new Paragraph("Total de estudiantes retirados: " + estudiantes.size(), infoFont);
            totalEstudiantes.setSpacingAfter(20);
            document.add(totalEstudiantes);

            // Crear tabla
            PdfPTable table = new PdfPTable(5); // 5 columnas
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3f, 2f, 2f, 2f, 3f}); // Anchos relativos

            // Headers de la tabla
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            addTableHeader(table, "Nombre Completo", headerFont);
            addTableHeader(table, "RUT", headerFont);
            addTableHeader(table, "Curso", headerFont);
            addTableHeader(table, "Nacionalidad", headerFont);
            addTableHeader(table, "Establecimiento", headerFont);

            // Agregar datos de estudiantes
            Font cellFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);
            for (EstudianteRetiradoDTO estudiante : estudiantes) {
                addTableCell(table, estudiante.getNombreCompleto(), cellFont);
                addTableCell(table, estudiante.getRut(), cellFont);
                addTableCell(table, estudiante.getCurso() != null ? estudiante.getCurso() : "N/A", cellFont);
                addTableCell(table, estudiante.getNacionalidad() != null ? estudiante.getNacionalidad() : "N/A", cellFont);
                addTableCell(table, estudiante.getEstablecimiento(), cellFont);
            }

            document.add(table);

            // Pie de página
            Paragraph footer = new Paragraph("\n\nReporte generado automáticamente por el Sistema de Gestión Digital Comunal", infoFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

        } catch (Exception e) {
            logger.error("Error al generar el PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Error al generar el reporte PDF", e);
        } finally {
            document.close();
        }

        logger.info("Reporte PDF generado exitosamente. Tamaño: {} bytes", out.size());
        return out.toByteArray();
    }

    /**
     * Agrega un header a la tabla
     */
    private void addTableHeader(PdfPTable table, String headerTitle, Font font) {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header.setBorderWidth(2);
        header.setPhrase(new Phrase(headerTitle, font));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setVerticalAlignment(Element.ALIGN_MIDDLE);
        header.setPadding(5);
        table.addCell(header);
    }

    /**
     * Agrega una celda a la tabla
     */
    private void addTableCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(4);
        table.addCell(cell);
    }

    /**
     * Obtiene el conteo total de estudiantes retirados
     */
    public Long obtenerTotalEstudiantesRetirados() {
        return reporteEstudiantesRetiradosRepository.countEstudiantesRetirados();
    }
}
