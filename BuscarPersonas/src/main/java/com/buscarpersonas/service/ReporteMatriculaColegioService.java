package com.buscarpersonas.service;

import com.buscarpersonas.Entity.Establecimiento;
import com.buscarpersonas.Entity.Estudiante;
import com.buscarpersonas.dto.EstudianteMatriculaColegioDTO;
import com.buscarpersonas.dto.ReporteMatriculaColegioDTO;
import com.buscarpersonas.repository.ReporteMatriculaColegioRepository;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReporteMatriculaColegioService {

    @Autowired
    private ReporteMatriculaColegioRepository reporteMatriculaColegioRepository;

    public ReporteMatriculaColegioDTO obtenerDatosReporteMatricula(Long establecimientoId) {
        Optional<Establecimiento> establecimientoOpt = reporteMatriculaColegioRepository.findEstablecimientoWithEstudiantes(establecimientoId);
        
        if (!establecimientoOpt.isPresent()) {
            throw new RuntimeException("Establecimiento no encontrado con ID: " + establecimientoId);
        }

        Establecimiento establecimiento = establecimientoOpt.get();
        List<EstudianteMatriculaColegioDTO> estudiantes = establecimiento.getEstudiantes()
            .stream()
            .map(est -> new EstudianteMatriculaColegioDTO(
                est.getRut(),
                est.getNombre(),
                est.getApellido(),
                est.getFechaNacimiento(),
                est.getTelefono(),
                est.getCurso(),
                est.getNacionalidad(),
                est.getEstado()
            ))
            .collect(Collectors.toList());

        return new ReporteMatriculaColegioDTO(
            establecimiento.getNombre(),
            establecimiento.getDireccion(),
            establecimiento.getComuna(),
            establecimiento.getTelefono(),
            estudiantes.size(),
            estudiantes
        );
    }

    public byte[] generarPDFMatricula(Long establecimientoId) {
        try {
            ReporteMatriculaColegioDTO reporteData = obtenerDatosReporteMatricula(establecimientoId);
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 36, 36, 54, 36);
            PdfWriter.getInstance(document, baos);
            
            document.open();

            // Título del documento
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("INFORME DE MATRÍCULA", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Información del establecimiento
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 10);

            document.add(new Paragraph("INFORMACIÓN DEL ESTABLECIMIENTO", headerFont));
            document.add(new Paragraph("Nombre: " + reporteData.getNombreEstablecimiento(), normalFont));
            document.add(new Paragraph("Dirección: " + reporteData.getDireccionEstablecimiento(), normalFont));
            document.add(new Paragraph("Comuna: " + reporteData.getComunaEstablecimiento(), normalFont));
            document.add(new Paragraph("Teléfono: " + (reporteData.getTelefonoEstablecimiento() != null ? reporteData.getTelefonoEstablecimiento() : "No especificado"), normalFont));
            document.add(new Paragraph("Total de Estudiantes: " + reporteData.getTotalEstudiantes(), normalFont));
            document.add(new Paragraph("Fecha de generación: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), normalFont));
            document.add(new Paragraph(" ", normalFont)); // Espaciado

            // Tabla de estudiantes
            if (!reporteData.getEstudiantes().isEmpty()) {
                document.add(new Paragraph("LISTADO DE ESTUDIANTES", headerFont));
                
                PdfPTable table = new PdfPTable(7);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10);
                
                // Headers
                Font tableHeaderFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
                addTableHeader(table, "RUT", tableHeaderFont);
                addTableHeader(table, "Nombre Completo", tableHeaderFont);
                addTableHeader(table, "F. Nacimiento", tableHeaderFont);
                addTableHeader(table, "Curso", tableHeaderFont);
                addTableHeader(table, "Nacionalidad", tableHeaderFont);
                addTableHeader(table, "Teléfono", tableHeaderFont);
                addTableHeader(table, "Estado", tableHeaderFont);

                // Datos
                Font tableCellFont = new Font(Font.FontFamily.HELVETICA, 8);
                for (EstudianteMatriculaColegioDTO estudiante : reporteData.getEstudiantes()) {
                    addTableCell(table, estudiante.getRut(), tableCellFont);
                    addTableCell(table, estudiante.getNombreCompleto(), tableCellFont);
                    addTableCell(table, estudiante.getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), tableCellFont);
                    addTableCell(table, estudiante.getCurso() != null ? estudiante.getCurso() : "-", tableCellFont);
                    addTableCell(table, estudiante.getNacionalidad() != null ? estudiante.getNacionalidad() : "-", tableCellFont);
                    addTableCell(table, estudiante.getTelefono() != null ? estudiante.getTelefono() : "-", tableCellFont);
                    addTableCell(table, estudiante.getEstado() != null ? estudiante.getEstado() : "-", tableCellFont);
                }
                
                document.add(table);
            }

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF: " + e.getMessage(), e);
        }
    }

    private void addTableHeader(PdfPTable table, String header, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(header, font));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addTableCell(PdfPTable table, String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setPadding(3);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
    }
}
