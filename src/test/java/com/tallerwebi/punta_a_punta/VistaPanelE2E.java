package com.tallerwebi.punta_a_punta;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class VistaPanelE2E {
    static Playwright playwright;
    static Browser browser;
    Page page;

    @BeforeEach
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch();
        page = browser.newPage();
        page.navigate("http://localhost:8080/spring/login");

        // Rellenar el formulario de inicio de sesión
        page.fill("input#email", "test@unlam.edu.ar");
        page.fill("input#password", "test");

        page.waitForNavigation(() -> {
            // Hacer clic en el botón de inicio de sesión
            page.click("button#btn-login");
        });
    }

    /*
    @Test
    public void testInicioSesion() {
        // Esperar a que aparezca el título de bienvenida en la página del panel de control
        ElementHandle element = page.waitForSelector("text=Bienvenido grupo_10!", new Page.WaitForSelectorOptions().setTimeout(5000));

        // Verificar que el título está presente
        assert(element.isVisible());
    }
    */

    @Test
    public void testAgregarMovimientoYQueAparezcaEnMovimientosSiendoFree() {
        // Hacer clic en el menú desplegable de "Movimientos"
        page.click("text=Movimientos");

        // Navegar a la página de movimientos
        page.waitForNavigation(() -> page.click("text=Agregar movimiento"));

        // Llenar los campos del formulario de agregar movimiento
        page.fill("input#nombre", "Campera peluche");
        page.selectOption("select#tipoMovimiento", "EGRESO");
        page.selectOption("select#categoriaMovimiento", "INDUMENTARIA");
        page.fill("input#monto", "50000");

        // Hacer clic en el botón de envío del formulario
        page.waitForNavigation(() -> page.click("button[type='submit']"));

        // Verificar que el primer movimiento tiene la descripción "Cobro"
        String descripcion = page.textContent("ul#lista-movimientos li:first-child span:nth-child(2)");
        assertTrue(descripcion.contains("Campera peluche"));
    }

    @Test
    public void testCambiarNombreUsuarioYQueAparezcaActualizadoEnLaBienvenida() {
        page.click("text=Usuario");
        page.waitForNavigation(() -> page.click("text=Mi perfil"));
        page.waitForNavigation(() -> page.click("text=Editar datos personales"));

        // Llenar el campo del nombre con el nuevo nombre
        page.fill("input[name='nombreUsuario']", "grupo_prueba");

        // Hacer clic en el botón de envío del formulario
        page.waitForNavigation(() -> page.click("button[type='submit']"));

        page.waitForNavigation(() -> page.click("text=Inicio"));

        // Esperar a que aparezca el título de bienvenida en la página del panel de control
        ElementHandle element = page.waitForSelector("text=Bienvenido grupo_prueba!", new Page.WaitForSelectorOptions());

        // Verificar que el título está presente
        assert(element.isVisible());

    }

    @Test
    public void testConvertirEnPremiumYQueAparezcaLaOpcionDeCancelarSuscripcion(){
        page.click("text=Usuario");
        page.waitForNavigation(() -> page.click("text=Mi perfil"));
        page.waitForNavigation(() -> page.click("text=PLAN PREMIUM"));
        page.waitForNavigation(() -> page.click("text=Adquirir Plan"));

        // Llenar los campos del formulario de pago
        page.fill("input#apellidoYNombre", "Apellido, Nombre");
        page.fill("input#numero", "1234567812345678");
        page.fill("input#fecha", "12/24");
        page.fill("input#cvv", "123");

        // Hacer clic en el botón de envío del formulario
        page.waitForNavigation(() -> page.click("button[type='submit']"));

        page.click("text=Usuario");
        page.waitForNavigation(() -> page.click("text=Mi perfil"));
        page.waitForNavigation(() -> page.click("text=PLAN PREMIUM"));

        // Esperar a que aparezca el botón "Cancelar"
        ElementHandle botonCancelar = page.waitForSelector("text=Cancelar Plan", new Page.WaitForSelectorOptions());

        // Verificar que el botón está presente
        assertTrue(botonCancelar.isVisible());

    }


}