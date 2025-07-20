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
import com.buscarpersonas.dto.ReporteMatriculaDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.Date;

@Service
public class ReporteMatriculaService {

    @Autowired
    private ReporteMatriculaRepository reporteRepository;

    @Autowired
    private EstablecimientoRepository establecimientoRepository;

    private static final Logger logger = LoggerFactory.getLogger(ReporteMatriculaService.class);
    
    @Autowired
    private ReporteMatriculaRepository reporteMatriculaRepository;
    

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
            document.add(new Paragraph(
                    "Fecha de generación: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), normalFont));
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
                document.add(new Paragraph(
                        "Teléfono: " + (est.getTelefono() != null ? est.getTelefono() : "No disponible"), normalFont));
                document.add(new Paragraph("Total de estudiantes: " + totalEstudiantes, normalFont));

                // Tabla de estudiantes por curso
                List<Object[]> estudiantesPorCurso = reporteRepository
                        .countEstudiantesByCursoAndEstablecimiento(est.getId());
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

    /**
     * Genera el reporte de matrícula completo para un establecimiento
     */
    public ReporteMatriculaDTO generarReporteMatricula(Long establecimientoId) {
        logger.info("Generando reporte de matrícula para establecimiento ID: {}", establecimientoId);
        
        // Verificar si existe el establecimiento
        if (!reporteMatriculaRepository.existsEstablecimientoById(establecimientoId)) {
            throw new RuntimeException("No se encontró el establecimiento con ID: " + establecimientoId);
        }
        
        // Obtener datos completos del reporte
        List<Object[]> resultados = reporteMatriculaRepository.findReporteMatriculaByEstablecimientoId(establecimientoId);
        
        if (resultados.isEmpty()) {
            throw new RuntimeException("No se encontraron datos para el establecimiento ID: " + establecimientoId);
        }
        
        return construirReporteMatricula(resultados);
    }
    
    /**
     * Construye el DTO del reporte a partir de los resultados de la consulta
     */
    private ReporteMatriculaDTO construirReporteMatricula(List<Object[]> resultados) {
        ReporteMatriculaDTO reporte = new ReporteMatriculaDTO();
        List<ReporteMatriculaDTO.EstudianteMatriculaDTO> estudiantes = new ArrayList<>();
        
        for (Object[] resultado : resultados) {
            // Datos del establecimiento (se repiten en cada fila, tomamos solo una vez)
            if (reporte.getEstablecimientoId() == null) {
                reporte.setEstablecimientoId(((BigInteger) resultado[0]).longValue());
                reporte.setNombreEstablecimiento((String) resultado[1]);
                reporte.setDireccionEstablecimiento((String) resultado[2]);
                reporte.setComunaEstablecimiento((String) resultado[3]);
                reporte.setTelefonoEstablecimiento((String) resultado[4]);
                reporte.setFechaGeneracion(LocalDate.now());
            }
            
            // Datos del estudiante (pueden ser null si no hay estudiantes)
            if (resultado[5] != null) { // estudiante_rut no es null
                ReporteMatriculaDTO.EstudianteMatriculaDTO estudiante = 
                    new ReporteMatriculaDTO.EstudianteMatriculaDTO();
                
                estudiante.setRut((String) resultado[5]);
                estudiante.setNombre((String) resultado[6]);
                estudiante.setApellido((String) resultado[7]);
                
                // Convertir fecha de nacimiento
                if (resultado[8] != null) {
                    Date fechaNacimiento = (Date) resultado[8];
                    estudiante.setFechaNacimiento(fechaNacimiento.toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate());
                }
                
                estudiante.setTelefono((String) resultado[9]);
                estudiante.setCurso((String) resultado[10]);
                estudiante.setNacionalidad((String) resultado[11]);
                estudiante.setEstado((String) resultado[12]);
                
                estudiantes.add(estudiante);
            }
        }
        
        reporte.setEstudiantes(estudiantes);
        return reporte;
    }
    
    /**
     * Genera un archivo PDF del reporte de matrícula
     */
    public byte[] generarPdfReporteMatricula(Long establecimientoId) throws DocumentException, IOException {
        logger.info("Generando PDF de reporte de matrícula para establecimiento ID: {}", establecimientoId);
        
        ReporteMatriculaDTO reporte = generarReporteMatricula(establecimientoId);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);
        
        document.open();
        
        // Agregar contenido al PDF
        agregarEncabezadoPdf(document, reporte);
        agregarInformacionEstablecimiento(document, reporte);
        agregarResumenMatricula(document, reporte);
        agregarTablaEstudiantes(document, reporte);
        agregarPiePagina(document);
        
        document.close();
        
        logger.info("PDF generado exitosamente. Tamaño: {} bytes", outputStream.size());
        return outputStream.toByteArray();
    }
    
    private void agregarEncabezadoPdf(Document document, ReporteMatriculaDTO reporte) throws DocumentException {
        // Título principal
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("REPORTE DE MATRÍCULA", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        // Subtítulo con nombre del establecimiento
        Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Paragraph subtitle = new Paragraph(reporte.getNombreEstablecimiento().toUpperCase(), subtitleFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(30);
        document.add(subtitle);
    }
    
    private void agregarInformacionEstablecimiento(Document document, ReporteMatriculaDTO reporte) throws DocumentException {
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 10);
        
        // Información del establecimiento
        Paragraph establecimiento = new Paragraph();
        establecimiento.add(new Chunk("Establecimiento: ", boldFont));
        establecimiento.add(new Chunk(reporte.getNombreEstablecimiento(), normalFont));
        establecimiento.add(Chunk.NEWLINE);
        
        establecimiento.add(new Chunk("Dirección: ", boldFont));
        establecimiento.add(new Chunk(reporte.getDireccionEstablecimiento(), normalFont));
        establecimiento.add(Chunk.NEWLINE);
        
        establecimiento.add(new Chunk("Comuna: ", boldFont));
        establecimiento.add(new Chunk(reporte.getComunaEstablecimiento(), normalFont));
        establecimiento.add(Chunk.NEWLINE);
        
        establecimiento.add(new Chunk("Teléfono: ", boldFont));
        establecimiento.add(new Chunk(reporte.getTelefonoEstablecimiento() != null ? 
            reporte.getTelefonoEstablecimiento() : "No registrado", normalFont));
        establecimiento.add(Chunk.NEWLINE);
        
        establecimiento.add(new Chunk("Fecha de generación: ", boldFont));
        establecimiento.add(new Chunk(reporte.getFechaGeneracion()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), normalFont));
        
        establecimiento.setSpacingAfter(20);
        document.add(establecimiento);
    }
    
    private void agregarResumenMatricula(Document document, ReporteMatriculaDTO reporte) throws DocumentException {
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        
        // Resumen
        Paragraph resumen = new Paragraph();
        resumen.add(new Chunk("RESUMEN DE MATRÍCULA", boldFont));
        resumen.add(Chunk.NEWLINE);
        resumen.add(new Chunk("Total de estudiantes matriculados: " + reporte.getTotalEstudiantes(), boldFont));
        resumen.setSpacingAfter(20);
        document.add(resumen);
    }
    
    private void agregarTablaEstudiantes(Document document, ReporteMatriculaDTO reporte) throws DocumentException {
        // Crear tabla con 8 columnas
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        
        // Definir anchos de columnas
        float[] columnWidths = {15f, 20f, 20f, 10f, 10f, 15f, 15f, 10f};
        table.setWidths(columnWidths);
        
        // Encabezados de tabla
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
        String[] headers = {"RUT", "Nombre", "Apellido", "F. Nac.", "Teléfono", "Curso", "Nacionalidad", "Estado"};
        
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }
        
        // Datos de estudiantes
        Font dataFont = new Font(Font.FontFamily.HELVETICA, 7);
        
        for (ReporteMatriculaDTO.EstudianteMatriculaDTO estudiante : reporte.getEstudiantes()) {
            table.addCell(new PdfPCell(new Phrase(estudiante.getRut() != null ? estudiante.getRut() : "", dataFont)));
            table.addCell(new PdfPCell(new Phrase(estudiante.getNombre() != null ? estudiante.getNombre() : "", dataFont)));
            table.addCell(new PdfPCell(new Phrase(estudiante.getApellido() != null ? estudiante.getApellido() : "", dataFont)));
            
            String fechaNac = estudiante.getFechaNacimiento() != null ? 
                estudiante.getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
            table.addCell(new PdfPCell(new Phrase(fechaNac, dataFont)));
            
            table.addCell(new PdfPCell(new Phrase(estudiante.getTelefono() != null ? estudiante.getTelefono() : "", dataFont)));
            table.addCell(new PdfPCell(new Phrase(estudiante.getCurso() != null ? estudiante.getCurso() : "", dataFont)));
            table.addCell(new PdfPCell(new Phrase(estudiante.getNacionalidad() != null ? estudiante.getNacionalidad() : "", dataFont)));
            table.addCell(new PdfPCell(new Phrase(estudiante.getEstado() != null ? estudiante.getEstado() : "", dataFont)));
        }
        
        document.add(table);
    }
    
    private void agregarPiePagina(Document document) throws DocumentException {
        Font footerFont = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC);
        Paragraph footer = new Paragraph();
        footer.add(Chunk.NEWLINE);
        footer.add(Chunk.NEWLINE);
        footer.add(new Chunk("Documento generado automáticamente por el Sistema de Gestión Comunal", footerFont));
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }
    
    /**
     * Obtiene estadísticas básicas del establecimiento
     */
    public Optional<Object[]> obtenerEstadisticasEstablecimiento(Long establecimientoId) {
        return reporteMatriculaRepository.findEstadisticasEstablecimiento(establecimientoId);
    }
}