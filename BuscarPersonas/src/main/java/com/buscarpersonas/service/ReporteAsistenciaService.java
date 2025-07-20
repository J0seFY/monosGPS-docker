package com.buscarpersonas.service;

import com.buscarpersonas.dto.AsistenciaEstablecimientoDTO;
import com.buscarpersonas.repository.ReporteAsistenciaRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;


@Service
public class ReporteAsistenciaService {

    @Autowired
    private ReporteAsistenciaRepository reporteRepo;

    public byte[] generarReporteAsistencia(LocalDate fecha) throws Exception {
        List<AsistenciaEstablecimientoDTO> datos = reporteRepo.obtenerReporteAsistenciaPorFecha(fecha);

        Document documento = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(documento, out);
        documento.open();

        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph titulo = new Paragraph("Reporte de Asistencia – " + fecha, fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);
        documento.add(new Paragraph(" ")); // espacio

        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new int[]{4, 2, 2, 2});

        Stream.of("Establecimiento", "Total", "Presentes", "Ausentes").forEach(tituloCol -> {
            PdfPCell header = new PdfPCell();
            header.setPhrase(new Phrase(tituloCol, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            tabla.addCell(header);
        });

        for (AsistenciaEstablecimientoDTO dto : datos) {
            tabla.addCell(dto.getNombreEstablecimiento());
            tabla.addCell(dto.getTotalEstudiantes().toString());
            tabla.addCell(dto.getPresentes().toString());
            tabla.addCell(dto.getAusentes().toString());
        }

        documento.add(tabla);
        documento.close();

        return out.toByteArray();
    }
}
