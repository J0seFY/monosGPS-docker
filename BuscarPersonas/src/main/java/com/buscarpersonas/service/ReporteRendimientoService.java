package com.buscarpersonas.service;

import com.buscarpersonas.dto.RendimientoDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ReporteRendimientoService {

    // Simulación de datos (reemplaza con tu consulta real de base de datos)
    public List<RendimientoDTO> obtenerDatosComunal() {
        return List.of(
                new RendimientoDTO("Escuela A", "Juan Pérez", 6.1),
                new RendimientoDTO("Escuela A", "María Soto", 5.9),
                new RendimientoDTO("Escuela B", "Pedro Díaz", 6.4)
        );
    }

    public List<RendimientoDTO> obtenerDatosPorEstablecimiento(String establecimiento) {
        return obtenerDatosComunal().stream()
                .filter(r -> r.getNombreEstablecimiento().equalsIgnoreCase(establecimiento))
                .toList();
    }

    public byte[] generarPDFRendimiento(List<RendimientoDTO> datos, String titulo) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);

        document.open();
        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph tituloParrafo = new Paragraph(titulo, fontTitulo);
        tituloParrafo.setAlignment(Element.ALIGN_CENTER);
        document.add(tituloParrafo);
        document.add(new Paragraph("\n"));

        PdfPTable tabla = new PdfPTable(3);
        tabla.setWidthPercentage(100);
        addCabeceras(tabla);
        for (RendimientoDTO dato : datos) {
            tabla.addCell(dato.getNombreEstablecimiento());
            tabla.addCell(dato.getNombreEstudiante());
            tabla.addCell(String.valueOf(dato.getPromedioNotas()));
        }

        document.add(tabla);
        document.close();
        return outputStream.toByteArray();
    }

    private void addCabeceras(PdfPTable table) {
        Stream.of("Establecimiento", "Estudiante", "Promedio")
                .forEach(columna -> {
                    PdfPCell header = new PdfPCell();
                    header.setPhrase(new Phrase(columna));
                    table.addCell(header);
                });
    }
}
