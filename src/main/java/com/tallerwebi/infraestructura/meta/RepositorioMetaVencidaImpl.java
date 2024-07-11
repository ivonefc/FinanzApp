package com.tallerwebi.infraestructura.meta;

import com.tallerwebi.dominio.excepcion.ExcepcionBaseDeDatos;
import com.tallerwebi.dominio.meta.Meta;
import com.tallerwebi.dominio.meta.MetaVencida;
import com.tallerwebi.dominio.meta.RepositorioMetaVencida;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioMetaVencidaImpl implements RepositorioMetaVencida {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioMetaVencidaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardarMetaVencida(Meta meta) throws ExcepcionBaseDeDatos {
        try {
            MetaVencida metaVencida = new MetaVencida();
            metaVencida.setUsuario(meta.getUsuario());
            metaVencida.setCategoriaMovimiento(meta.getCategoriaMovimiento());
            metaVencida.setMontoMeta(meta.getMontoMeta());
            metaVencida.setFechaInicio(meta.getFechaInicio());
            metaVencida.setFechaFin(meta.getFechaFin());

            sessionFactory.getCurrentSession().save(metaVencida);
        }catch (HibernateException e) {
            throw new ExcepcionBaseDeDatos();
        }
    }
}
