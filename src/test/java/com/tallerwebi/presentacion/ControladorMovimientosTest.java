package com.tallerwebi.presentacion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

//HAMCREST
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class ControladorMovimientosTest {
    ControladorMovimientos controladorMovimientos;
    @BeforeEach
    public void init(){
        controladorMovimientos = new ControladorMovimientos();
    }
    @Test
    public void queAlClickearLaOpcionVerMovimientosEnElMenuDirijaALaVistaMovimientos(){
        //ejecucion
        ModelAndView modelAndView = controladorMovimientos.irAMovimientos();

        //validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("movimientos"));
    }
}
