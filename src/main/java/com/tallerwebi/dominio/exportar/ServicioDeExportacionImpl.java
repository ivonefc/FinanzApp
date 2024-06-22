package com.tallerwebi.dominio.exportar;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.excepcion.ExcepcionExportacionDeArchivo;
import com.tallerwebi.dominio.exportar.estrategias.EstrategiaDeExportacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServicioDeExportacionImpl implements ServicioDeExportacion {
    private Map<TipoDeArchivo, EstrategiaDeExportacion> estrategiasDeExportacion;

    @Autowired
    public ServicioDeExportacionImpl(List<EstrategiaDeExportacion> estrategiasDeExportacion) {
        this.estrategiasDeExportacion = new HashMap<>();
        estrategiasDeExportacion.forEach(estrategia ->{
            this.estrategiasDeExportacion.put(estrategia.obtenerTipoDeArchivo(), estrategia);
        });
    }

    public Map<TipoDeArchivo, EstrategiaDeExportacion> obtenerEstrategiasDeExportacion() {
        return estrategiasDeExportacion;
    }

    @Override
    public byte[] generarArchivo(Long idUsuario, TipoDeArchivo tipoDeDoc) throws ExcepcionBaseDeDatos, ExcepcionExportacionDeArchivo {
        EstrategiaDeExportacion estrategia = this.estrategiasDeExportacion.get(tipoDeDoc);
        return estrategia.generarArchivo(idUsuario);
    }
}
