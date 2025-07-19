package com.buscarpersonas.service;

import com.buscarpersonas.dto.MatriculasComunalesDTO;
import com.buscarpersonas.repository.EstudianteRepository;
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
public class MatriculasPDFService {
    
    private static final Logger logger = LoggerFactory.getLogger(MatriculasPDFService.class);
    
    @Autowired
    private EstudianteRepository estudianteRepository;
    
    // Definir fuentes
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font SUBTITLE_FONT = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
    private static final Font SMALL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    
    /**
     * Genera un PDF con el informe de matrículas comunales
     * @return Array de bytes del PDF generado
     * @throws Exception Si hay error en la generación del PDF
     */
    public byte[] generarInformeMatriculasComunales() throws Exception {
        logger.info("Iniciando generación de informe de matrículas comunales");
        
        try {
            // Obtener datos de la base de datos
            List<MatriculasComunalesDTO> datosMatriculas = estudianteRepository.obtenerMatriculasPorComuna();
            Long totalEstudiantes = estudianteRepository.obtenerTotalEstudiantes();
            Long totalComunas = estudianteRepository.obtenerTotalComunas();
            
            logger.info("Datos obtenidos: {} comunas, {} estudiantes total", totalComunas, totalEstudiantes);
            
            // Crear el documento PDF
            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            
            // Abrir el documento
            document.open();
            
            // Título principal
            Paragraph titulo = new Paragraph("INFORME DE MATRÍCULAS COMUNALES", TITLE_FONT);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);
            
            // Información de generación
            String fechaGeneracion = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            Paragraph infoGeneracion = new Paragraph("Fecha de generación: " + fechaGeneracion, SMALL_FONT);
            infoGeneracion.setAlignment(Element.ALIGN_RIGHT);
            infoGeneracion.setSpacingAfter(15);
            document.add(infoGeneracion);
            
            // Resumen estadístico
            Paragraph tituloResumen = new Paragraph("RESUMEN ESTADÍSTICO", SUBTITLE_FONT);
            tituloResumen.setSpacingAfter(10);
            document.add(tituloResumen);
            
            Paragraph totalEstudiantesP = new Paragraph("• Total de estudiantes: " + totalEstudiantes, NORMAL_FONT);
            document.add(totalEstudiantesP);
            
            Paragraph totalComunasP = new Paragraph("• Total de comunas: " + totalComunas, NORMAL_FONT);
            totalComunasP.setSpacingAfter(20);
            document.add(totalComunasP);
            
            // Título de la tabla
            Paragraph tituloTabla = new Paragraph("DETALLE POR COMUNA", SUBTITLE_FONT);
            tituloTabla.setSpacingAfter(10);
            document.add(tituloTabla);
            
            // Crear tabla con 3 columnas
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1f, 4f, 2f}); // Proporciones de las columnas
            
            // Encabezados de la tabla
            PdfPCell headerCell1 = new PdfPCell(new Phrase("N°", HEADER_FONT));
            headerCell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerCell1.setPadding(8);
            table.addCell(headerCell1);
            
            PdfPCell headerCell2 = new PdfPCell(new Phrase("COMUNA", HEADER_FONT));
            headerCell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerCell2.setPadding(8);
            table.addCell(headerCell2);
            
            PdfPCell headerCell3 = new PdfPCell(new Phrase("CANTIDAD DE ESTUDIANTES", HEADER_FONT));
            headerCell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerCell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerCell3.setPadding(8);
            table.addCell(headerCell3);
            
            // Agregar datos a la tabla
            int contador = 1;
            for (MatriculasComunalesDTO dato : datosMatriculas) {
                // Columna número
                PdfPCell cellNumero = new PdfPCell(new Phrase(String.valueOf(contador++), NORMAL_FONT));
                cellNumero.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellNumero.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellNumero.setPadding(5);
                table.addCell(cellNumero);
                
                // Columna comuna
                PdfPCell cellComuna = new PdfPCell(new Phrase(dato.getComuna(), NORMAL_FONT));
                cellComuna.setHorizontalAlignment(Element.ALIGN_LEFT);
                cellComuna.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellComuna.setPadding(5);
                table.addCell(cellComuna);
                
                // Columna cantidad
                PdfPCell cellCantidad = new PdfPCell(new Phrase(dato.getCantidadEstudiantes().toString(), NORMAL_FONT));
                cellCantidad.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellCantidad.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellCantidad.setPadding(5);
                table.addCell(cellCantidad);
            }
            
            document.add(table);
            
            // Pie de página
            Paragraph piePagina = new Paragraph("\n\nInforme generado automáticamente por el Sistema de Gestión Comunal", SMALL_FONT);
            piePagina.setAlignment(Element.ALIGN_CENTER);
            piePagina.setSpacingBefore(30);
            document.add(piePagina);
            
            // Cerrar el documento
            document.close();
            writer.close();
            
            logger.info("PDF generado exitosamente, tamaño: {} bytes", baos.size());
            return baos.toByteArray();
            
        } catch (Exception e) {
            logger.error("Error al generar el PDF de matrículas comunales", e);
            throw new Exception("Error al generar el informe PDF: " + e.getMessage(), e);
        }
    }
}