package com.buscarpersonas.service;

import com.buscarpersonas.dto.ReporteFuncionariosDTO;
import com.buscarpersonas.repository.ReporteFuncionariosRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ReporteFuncionariosService {

    @Autowired
    private ReporteFuncionariosRepository repository;

    public ByteArrayInputStream generarPDFComunal() {
        List<ReporteFuncionariosDTO> funcionarios = repository.obtenerFuncionariosComunal();
        return generarPDF(funcionarios, "Reporte de Funcionarios a Nivel Comunal");
    }

    public ByteArrayInputStream generarPDFPorEstablecimiento(int establecimientoId) {
        List<ReporteFuncionariosDTO> funcionarios = repository.obtenerFuncionariosPorEstablecimiento(establecimientoId);
        return generarPDF(funcionarios, "Reporte de Funcionarios del Establecimiento");
    }

    private ByteArrayInputStream generarPDF(List<ReporteFuncionariosDTO> lista, String tituloReporte) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph title = new Paragraph(tituloReporte, titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{2, 3, 2, 2, 3});

            Stream.of("Comuna", "Establecimiento", "Nombre", "Apellido", "Asignatura")
                    .forEach(header -> {
                        PdfPCell cell = new PdfPCell(new Phrase(header));
                        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);
                    });

            for (ReporteFuncionariosDTO dto : lista) {
                table.addCell(dto.getComuna());
                table.addCell(dto.getEstablecimiento());
                table.addCell(dto.getNombre());
                table.addCell(dto.getApellido());
                table.addCell(dto.getAsignatura());
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
