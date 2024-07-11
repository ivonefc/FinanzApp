package com.tallerwebi.dominio.meta;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;

public interface RepositorioMetaVencida {


    void guardarMetaVencida(Meta meta) throws ExcepcionBaseDeDatos;
}
