package com.buscarpersonas.service;

import com.buscarpersonas.dto.ReporteSimceDTO;
import com.buscarpersonas.repository.ReporteSimceRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import java.util.stream.Stream;


@Service
public class ReporteSimceService {

    private final ReporteSimceRepository reporteSimceRepository;

    public ReporteSimceService(ReporteSimceRepository reporteSimceRepository) {
        this.reporteSimceRepository = reporteSimceRepository;
    }

    public ByteArrayInputStream generarReporteSimcePdf(String comuna) {
        List<ReporteSimceDTO> resultados = reporteSimceRepository.findResultadosSimcePorComuna(comuna);

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Agregar título
            Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph titulo = new Paragraph("Reporte SIMCE - Comuna: " + comuna, tituloFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            document.add(Chunk.NEWLINE);

            // Tabla con columnas
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{3, 4, 4, 5, 2, 3});

            // Encabezados
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            Stream.of("RUT Estudiante", "Nombre", "Apellido", "Establecimiento", "Puntaje", "Fecha Prueba")
                  .forEach(headerTitle -> {
                      PdfPCell header = new PdfPCell();
                      header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                      header.setPhrase(new Phrase(headerTitle, headerFont));
                      table.addCell(header);
                  });

            // Agregar filas
            for (ReporteSimceDTO dto : resultados) {
                table.addCell(dto.getRutEstudiante());
                table.addCell(dto.getNombreEstudiante());
                table.addCell(dto.getApellidoEstudiante());
                table.addCell(dto.getNombreEstablecimiento());
                table.addCell(String.valueOf(dto.getPuntajeSimce()));
                table.addCell(dto.getFechaPrueba().toString());
            }

            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
