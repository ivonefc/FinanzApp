package com.tallerwebi.dominio.exportar.estrategias;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionExportacionDeArchivo;
import com.tallerwebi.dominio.exportar.TipoDeArchivo;
import com.tallerwebi.dominio.movimiento.Movimiento;
import com.tallerwebi.dominio.movimiento.ServicioMovimiento;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class EstrategiaDeExportacionXlsx implements EstrategiaDeExportacion{

    ServicioMovimiento servicioMovimiento;

    @Autowired
    public EstrategiaDeExportacionXlsx(ServicioMovimiento servicioMovimiento) {
        this.servicioMovimiento = servicioMovimiento;
    }

    @Override
    public TipoDeArchivo obtenerTipoDeArchivo() {
        return TipoDeArchivo.XLSX;
    }

    @Override
    public byte[] generarArchivo(Long idUsuario) throws ExcepcionBaseDeDatos, ExcepcionExportacionDeArchivo {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Workbook workbook = new XSSFWorkbook();

        try {
            //Creo una hoja llamada Movimientos
            Sheet hoja = workbook.createSheet("Movimientos");

            List<Movimiento> movimientos = servicioMovimiento.obtenerMovimientos(idUsuario);

            if (!movimientos.isEmpty()) {

                Row fila = hoja.createRow(0);

                agregarEncabezado(fila, 0, "FECHA");
                agregarEncabezado(fila, 1, "TIPO");
                agregarEncabezado(fila, 2, "CATEGORIA");
                agregarEncabezado(fila, 3, "DESCRIPCION");
                agregarEncabezado(fila, 4, "MONTO");
                int numeroDeFila = 1;
                CellStyle ajustarTexto = workbook.createCellStyle();
                ajustarTexto.setWrapText(true);
                for (Movimiento movimiento : movimientos) {
                    Row filaRegistro = hoja.createRow(numeroDeFila);
                    agregarRegistro(filaRegistro, movimiento);
                    numeroDeFila++;
                }

            }else{
                throw new ExcepcionExportacionDeArchivo("No se pudo exportar archivo");
            }

            workbook.write(stream);
            return stream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    //METODOS PRIVADOS
    private void agregarEncabezado(Row fila, int numeroDeCelda, String encabezado) {
        Cell celda = fila.createCell(numeroDeCelda);
        celda.setCellValue(encabezado);
    }

    private void agregarRegistro(Row filaRegistro, Movimiento movimiento) {
        Cell celdaFecha = filaRegistro.createCell(0);
        celdaFecha.setCellValue(movimiento.getFechayHora().toString());
        Cell celdaTipo = filaRegistro.createCell(1);
        celdaTipo.setCellValue(movimiento.getCategoria().getTipo().getNombre());
        Cell celdaCategoria = filaRegistro.createCell(2);
        celdaCategoria.setCellValue(movimiento.getCategoria().getNombre());
        Cell celdaDescripcion = filaRegistro.createCell(3);
        celdaDescripcion.setCellValue(movimiento.getDescripcion());
        Cell celdaMonto = filaRegistro.createCell(4);
        celdaMonto.setCellValue(movimiento.getMonto().toString());
    }

}
