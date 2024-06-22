package com.tallerwebi.dominio.exportar.estrategias;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionExportacionDeArchivo;
import com.tallerwebi.dominio.exportar.TipoDeArchivo;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.ServicioMovimiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class EstrategiaDeExportacionPDF implements EstrategiaDeExportacion{
    ServicioMovimiento servicioMovimiento;

    @Autowired
    public EstrategiaDeExportacionPDF(ServicioMovimiento servicioMovimiento) {
        this.servicioMovimiento = servicioMovimiento;
    }

    @Override
    public TipoDeArchivo obtenerTipoDeArchivo() {
        return TipoDeArchivo.PDF;
    }

    // Este metodo crea el documento PDF y lo devuelve como un arreglo de bytes
    @Override
    public byte[] generarArchivo(Long idUsuario) throws ExcepcionBaseDeDatos, ExcepcionExportacionDeArchivo {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter pdfWriter = PdfWriter.getInstance(document, stream);
            document.open();
            //añado un titulo al documento
            Paragraph titulo = new Paragraph("Movimientos", FontFactory.getFont(FontFactory.COURIER, 40, BaseColor.BLACK));
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            // Añadir un párrafo vacío para crear espacio
            Paragraph espacio = new Paragraph(" ", FontFactory.getFont(FontFactory.COURIER, 10));
            document.add(espacio);

            List<Movimiento> movimientos = servicioMovimiento.obtenerMovimientos(idUsuario);
            // Verificar si hay movimientos
            if(!movimientos.isEmpty()){
                PdfPTable tabla = new PdfPTable(5); //defino la cantidad de columnas
                tabla.setWidthPercentage(100); // ancho de la tabla como porcentaje del documento

                // Añadir encabezados de la tabla
                agregarColumna(tabla, "Fecha");
                agregarColumna(tabla, "Tipo");
                agregarColumna(tabla, "Categoria");
                agregarColumna(tabla, "Descripcion");
                agregarColumna(tabla, "Monto");

                // Añadir filas de movimientos
                for (Movimiento movimiento : movimientos) {
                    agregarFila(tabla, movimiento);
                }

                // Añadir tabla al documento
                document.add(tabla);
            }else{
                throw new ExcepcionExportacionDeArchivo("No se pudo exportar archivo");
            }
            document.close();
            return stream.toByteArray();
        } catch (DocumentException e) {
            throw new ExcepcionExportacionDeArchivo(e);
        }
    }

    //METODOS PRIVADOS
    private void agregarColumna(PdfPTable tabla, String encabezado) {
        PdfPCell cell;
        cell = new PdfPCell(new Phrase(encabezado, FontFactory.getFont(FontFactory.COURIER, 12, BaseColor.BLACK)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        tabla.addCell(cell);
    }

    private void agregarFila(PdfPTable tabla, Movimiento movimiento) {
        tabla.addCell(new PdfPCell(new Phrase(movimiento.getFechayHora().toString(), FontFactory.getFont(FontFactory.COURIER, 12, BaseColor.BLACK))));
        tabla.addCell(new PdfPCell(new Phrase(movimiento.getCategoria().getTipo().getNombre(), FontFactory.getFont(FontFactory.COURIER, 12, BaseColor.WHITE)))).setBackgroundColor(movimiento.getCategoria().getTipo().getNombre().equals("EGRESO")? BaseColor.RED : BaseColor.GREEN);
        tabla.addCell(new PdfPCell(new Phrase(movimiento.getCategoria().getNombre(), FontFactory.getFont(FontFactory.COURIER, 8, Font.BOLD))));
        tabla.addCell(new PdfPCell(new Phrase(movimiento.getDescripcion(), FontFactory.getFont(FontFactory.COURIER, 12, BaseColor.BLACK))));
        tabla.addCell(new PdfPCell(new Phrase(movimiento.getCategoria().getTipo().getNombre().equals("EGRESO")?"- $" +String.format("%.0f", movimiento.getMonto()) : "+ $" +String.format("%.0f", movimiento.getMonto()), FontFactory.getFont(FontFactory.COURIER, 12, BaseColor.BLACK))));
    }
}
