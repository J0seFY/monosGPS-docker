package com.buscarpersonas.service;

import com.buscarpersonas.dto.RendimientoReporteDTO;
import com.buscarpersonas.repository.RendimientoRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class RendimientoReporteService {
    
    @Autowired
    private RendimientoRepository rendimientoRepository;
    
    /**
     * Genera reporte de rendimiento por establecimiento
     */
    public List<RendimientoReporteDTO> obtenerRendimientoPorEstablecimiento(Long establecimientoId) {
        return rendimientoRepository.obtenerRendimientoPorEstablecimiento(establecimientoId);
    }
    
    /**
     * Genera reporte de rendimiento comunal
     */
    public List<RendimientoReporteDTO> obtenerRendimientoComunal(String comuna) {
        return rendimientoRepository.obtenerRendimientoComunal(comuna);
    }
    
    /**
     * Genera reporte con filtros personalizados
     */
    public List<RendimientoReporteDTO> obtenerRendimientoConFiltros(Long establecimientoId, String asignatura, 
                                                                   String curso, LocalDate fechaInicio, LocalDate fechaFin) {
        return rendimientoRepository.obtenerRendimientoConFiltros(establecimientoId, asignatura, curso, fechaInicio, fechaFin);
    }
    
    /**
     * Genera PDF del reporte de rendimiento por establecimiento
     */
    public byte[] generarPDFRendimientoEstablecimiento(Long establecimientoId) throws DocumentException {
        List<RendimientoReporteDTO> datos = obtenerRendimientoPorEstablecimiento(establecimientoId);
        return generarPDFRendimiento(datos, "Reporte de Rendimiento por Establecimiento", true);
    }
    
    /**
     * Genera PDF del reporte de rendimiento comunal
     */
    public byte[] generarPDFRendimientoComunal(String comuna) throws DocumentException {
        List<RendimientoReporteDTO> datos = obtenerRendimientoComunal(comuna);
        String titulo = comuna != null ? "Reporte de Rendimiento - Comuna: " + comuna : "Reporte de Rendimiento Comunal";
        return generarPDFRendimiento(datos, titulo, false);
    }
    
    /**
     * Método privado para generar el PDF
     */
    private byte[] generarPDFRendimiento(List<RendimientoReporteDTO> datos, String titulo, boolean incluirEstablecimiento) 
            throws DocumentException {
        
        Document document = new Document(PageSize.A4.rotate()); // Orientación horizontal para más columnas
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            
            // Título del documento
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLUE);
            Paragraph title = new Paragraph(titulo, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            
            // Fecha de generación
            Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
            Paragraph date = new Paragraph("Generado el: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), dateFont);
            date.setAlignment(Element.ALIGN_RIGHT);
            document.add(date);
            
            document.add(new Paragraph(" ")); // Espacio
            
            if (datos.isEmpty()) {
                Paragraph noData = new Paragraph("No se encontraron datos para el reporte.", FontFactory.getFont(FontFactory.HELVETICA, 12));
                noData.setAlignment(Element.ALIGN_CENTER);
                document.add(noData);
            } else {
                // Crear tabla
                int numColumnas = incluirEstablecimiento ? 8 : 5;
                PdfPTable table = new PdfPTable(numColumnas);
                table.setWidthPercentage(100);
                
                // Configurar anchos de columna
                if (incluirEstablecimiento) {
                    table.setWidths(new float[]{3f, 2f, 2f, 2f, 1.5f, 1.5f, 1.5f, 1.5f});
                } else {
                    table.setWidths(new float[]{2f, 2f, 2f, 1.5f, 1.5f});
                }
                
                // Encabezados de la tabla
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
                
                if (incluirEstablecimiento) {
                    addTableHeader(table, "Establecimiento", headerFont);
                }
                addTableHeader(table, "Comuna", headerFont);
                addTableHeader(table, "Asignatura", headerFont);
                if (incluirEstablecimiento) {
                    addTableHeader(table, "Curso", headerFont);
                }
                addTableHeader(table, "Promedio", headerFont);
                addTableHeader(table, "Total Estudiantes", headerFont);
                addTableHeader(table, "Con Notas", headerFont);
                if (incluirEstablecimiento) {
                    addTableHeader(table, "Nota Máx.", headerFont);
                    addTableHeader(table, "Nota Mín.", headerFont);
                }
                
                // Agregar datos
                Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
                for (RendimientoReporteDTO dato : datos) {
                    if (incluirEstablecimiento) {
                        addTableCell(table, dato.getEstablecimientoNombre(), dataFont);
                    }
                    addTableCell(table, dato.getComuna(), dataFont);
                    addTableCell(table, dato.getAsignatura(), dataFont);
                    if (incluirEstablecimiento) {
                        addTableCell(table, dato.getCurso() != null ? dato.getCurso() : "N/A", dataFont);
                    }
                    addTableCell(table, String.format("%.1f", dato.getPromedioGeneral()), dataFont);
                    addTableCell(table, dato.getTotalEstudiantes().toString(), dataFont);
                    addTableCell(table, dato.getEstudiantesConNotas().toString(), dataFont);
                    if (incluirEstablecimiento) {
                        addTableCell(table, dato.getNotaMaxima() != null ? String.format("%.1f", dato.getNotaMaxima()) : "N/A", dataFont);
                        addTableCell(table, dato.getNotaMinima() != null ? String.format("%.1f", dato.getNotaMinima()) : "N/A", dataFont);
                    }
                }
                
                document.add(table);
                
                // Estadísticas generales
                document.add(new Paragraph(" "));
                addEstadisticasGenerales(document, datos);
            }
            
        } catch (DocumentException e) {
            throw e;
        } finally {
            document.close();
        }
        
        return baos.toByteArray();
    }
    
    private void addTableHeader(PdfPTable table, String headerText, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(headerText, font));
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(8);
        table.addCell(cell);
    }
    
    private void addTableCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }
    
    private void addEstadisticasGenerales(Document document, List<RendimientoReporteDTO> datos) throws DocumentException {
        Font statsFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLUE);
        document.add(new Paragraph("Estadísticas Generales", statsFont));
        
        long totalEstudiantes = datos.stream().mapToLong(RendimientoReporteDTO::getTotalEstudiantes).sum();
        double promedioGeneral = datos.stream()
                .mapToDouble(d -> d.getPromedioGeneral().doubleValue())
                .average()
                .orElse(0.0);
        
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
        document.add(new Paragraph("Total de estudiantes evaluados: " + totalEstudiantes, normalFont));
        document.add(new Paragraph("Promedio general: " + String.format("%.2f", promedioGeneral), normalFont));
        document.add(new Paragraph("Número de registros: " + datos.size(), normalFont));
    }
    
    /**
     * Obtiene listas auxiliares para filtros
     */
    public List<String> obtenerComunas() {
        return rendimientoRepository.obtenerComunas();
    }
    
    public List<String> obtenerAsignaturas() {
        return rendimientoRepository.obtenerAsignaturas();
    }
}