// ReporteMatriculaService.java
package com.buscarpersonas.service;

import com.buscarpersonas.dto.ReporteMatriculaDTO;
import com.buscarpersonas.repository.ReporteMatriculaColegioRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ReporteMatriculaColegioService {

    @Autowired
    private ReporteMatriculaColegioRepository repository;

    public ByteArrayInputStream generarPdfPorEstablecimiento(Integer idEstablecimiento) {
        List<ReporteMatriculaDTO> datos = repository.obtenerReporteMatriculaPorEstablecimiento(idEstablecimiento);

        if (datos.isEmpty()) {
            throw new IllegalArgumentException(
                    "No se encontraron matrículas para el establecimiento ID: " + idEstablecimiento);
        }
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Reporte de Matrículas"));
            document.add(new Paragraph("Establecimiento: " + datos.get(0).getNombreEstablecimiento()));
            document.add(new Paragraph("Dirección: " + datos.get(0).getDireccion()));
            document.add(new Paragraph("Comuna: " + datos.get(0).getComuna()));
            document.add(new Paragraph("Teléfono: " + datos.get(0).getTelefono()));
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(6);
            table.setWidths(new int[] { 3, 3, 3, 2, 2, 2 });
            table.addCell("Nombre");
            table.addCell("Apellido");
            table.addCell("RUT");
            table.addCell("Curso");
            table.addCell("Nacionalidad");
            table.addCell("Estado");

            for (ReporteMatriculaDTO dto : datos) {
                table.addCell(dto.getNombreEstudiante());
                table.addCell(dto.getApellidoEstudiante());
                table.addCell(dto.getRutEstudiante());
                table.addCell(dto.getCurso());
                table.addCell(dto.getNacionalidad());
                table.addCell(dto.getEstado());
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
