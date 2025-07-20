package com.buscarpersonas.service;

import com.buscarpersonas.dto.ReporteRendimientoDTO;
import com.buscarpersonas.repository.ReporteRendimientoRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReporteRendimientoService {

    private final ReporteRendimientoRepository repository;

    public ReporteRendimientoService(ReporteRendimientoRepository repository) {
        this.repository = repository;
    }

    // Conversión de List<Object[]> a List<ReporteRendimientoDTO>
    public List<ReporteRendimientoDTO> generarReporte() {
        List<Object[]> resultados = repository.obtenerPromedioRendimientoPorComunaYEstablecimiento();
        List<ReporteRendimientoDTO> lista = new ArrayList<>();

        for (Object[] fila : resultados) {
            String comuna = (String) fila[0];
            String establecimiento = (String) fila[1];
            Double promedio = (Double) fila[2];

            ReporteRendimientoDTO dto = new ReporteRendimientoDTO(comuna, establecimiento, promedio);
            lista.add(dto);
        }

        return lista;
    }

    public byte[] generarReportePDF() throws DocumentException {
        List<ReporteRendimientoDTO> datos = generarReporte();

        Document documento = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(documento, baos);
        documento.open();

        Font tituloFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph titulo = new Paragraph("Reporte de Rendimiento", tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        documento.add(titulo);

        PdfPTable tabla = new PdfPTable(3);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new int[]{3, 5, 2});

        tabla.addCell("Comuna");
        tabla.addCell("Establecimiento");
        tabla.addCell("Promedio");

        for (ReporteRendimientoDTO dato : datos) {
            tabla.addCell(dato.getComuna());
            tabla.addCell(dato.getEstablecimiento());
            tabla.addCell(String.format("%.2f", dato.getPromedio()));
        }

        documento.add(tabla);
        documento.close();

        return baos.toByteArray();
    }
}
