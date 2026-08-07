package com.tienda;

import com.tienda.view.TiendaApp;

/**
 * Clase principal de entrada (Launcher).
 * Al no extender de javafx.application.Application directamente,
 * evita el error "JavaFX runtime components are missing" cuando se ejecuta
 * desde un IDE o directamente mediante 'java'.
 */
public class Main {
    public static void main(String[] args) {
        TiendaApp.main(args);
    }
}
