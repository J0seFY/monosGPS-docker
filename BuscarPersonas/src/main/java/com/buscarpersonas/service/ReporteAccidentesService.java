package com.buscarpersonas.service;

import com.buscarpersonas.dto.ReporteAccidentesDTO;
import com.buscarpersonas.repository.ReporteAccidentesRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;


@Service
public class ReporteAccidentesService {

    @Autowired
    private ReporteAccidentesRepository repository;

    public ByteArrayInputStream generarReporteAccidentesPDF() {
        List<ReporteAccidentesDTO> datos = repository.obtenerResumenAccidentes();

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph title = new Paragraph("Reporte de Accidentes Escolares a Nivel Comunal", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{3, 5, 2});

            // Encabezados
            Stream.of("Comuna", "Establecimiento", "Cantidad")
                .forEach(header -> {
                    PdfPCell cell = new PdfPCell(new Phrase(header));
                    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                });

            // Cuerpo
            for (ReporteAccidentesDTO dto : datos) {
                table.addCell(dto.getComuna());
                table.addCell(dto.getEstablecimiento());
                table.addCell(dto.getCantidadAccidentes().toString());
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
