# 🌿 Tienda Artesanal — Aplicación de Escritorio JavaFX (MVC)

Aplicación interactiva de escritorio desarrollada con **Java 21** y **JavaFX API**, aplicando **Patrón de Arquitectura MVC (Modelo-Vista-Controlador)**, principios de **Clean Code** y un diseño **UX/UI artesanal moderno**.

---

## 📁 Estructura del Proyecto (MVC)

```text
tienda-artesanal/
├── pom.xml                                  <- Archivo de configuración Maven
├── README.md                                <- Guía paso a paso y arquitectura
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── tienda/
        │           ├── model/               <- CAPA MODELO
        │           │   ├── Producto.java    <- Representación de artesanías (id, precio, stock, categoría)
        │           │   ├── Comprador.java   <- Datos del cliente e historial de órdenes
        │           │   ├── Vendedor.java    <- Taller artesano y catálogo en venta
        │           │   ├── Envio.java       <- Gestión de estados y costo de envío
        │           │   ├── MetodoPago.java  <- Tarjeta, Transferencia y Efectivo
        │           │   └── ItemCarrito.java <- Elemento del carrito (Producto + Cantidad)
        │           │
        │           ├── controller/          <- CAPA CONTROLADOR
        │           │   └── TiendaController.java <- Lógica central, carrito y filtro dinámico
        │           │
        │           └── view/                <- CAPA VISTA (JavaFX Puro)
        │               ├── TiendaApp.java             <- Ventana principal Stage y Sidebar
        │               ├── CatalogoPanel.java         <- Grid de productos con dibujos Canvas
        │               ├── PerfilCompradorPanel.java  <- Perfil e historial de pedidos
        │               ├── PerfilVendedorPanel.java   <- Alta de piezas y métricas del taller
        │               └── CarritoCheckoutPanel.java  <- Resumen de carrito y pago
        │
        └── resources/                       <- RECURSOS Y CSS
            └── com/
                └── tienda/
                    └── view/
                        └── styles.css       <- Estilos modernos: terracota, sombras y hover
```

---

## ✨ Características y Principios de Diseño

1. **Arquitectura MVC Estricta:** Separación total de responsabilidades. El modelo no conoce la vista; el controlador gestiona el estado y notifica mediante listas observables y callbacks a las vistas.
2. **Interfaz Moderna:**
   - Paleta de colores cálida artesanal: Terracota (`#C85A32`), Crema (`#FDFBF7`), Café Espléndido (`#2C1E16`) y Verde Botánico (`#5E6F52`).
   - Componentes interactivos con bordes redondeados (`14px`), tarjetas con sombras suaves y estados `:hover`.
   - Badges animados de categorías y stock.
3. **Navegación Fluida:** Sidebar lateral persistente que cambia de vistas instantáneamente sin parpadeos ni reapertura de ventanas.
4. **Ilustraciones Vectoriales Canvas:** Cada tarjeta incluye un dibujo conceptual programático según su disciplina (Cerámica, Textil, Madera, Joyería, Cuero).
5. **Sistema de Notificaciones (Toast Overlay):** Alertas visuales animadas en la esquina inferior para confirmación de acciones, compras y validaciones.

---

## 🛠️ Requisitos e Instrucciones para Ejecutar desde 0

### Requisitos Previos:
- **JDK 21** o superior instalado en el equipo (e.g. OpenJDK 21, Eclipse Temurin 21 o Oracle JDK 21).
- **Apache Maven** (versión 3.8+).

### Pasos de Ejecución:

1. **Abrir la terminal en la raíz del proyecto:**
   ```bash
   cd C:\Users\hmont\.gemini\antigravity\scratch\tienda-artesanal
   ```

2. **Compilar y ejecutar la aplicación con Maven (Recomendado):**
   ```bash
   mvn clean javafx:run
   ```

3. **Ejecutar desde tu IDE preferido (IntelliJ IDEA / Eclipse / VS Code):**
   - Importar como proyecto Maven (`pom.xml`).
   - Ejecutar la clase principal `com.tienda.view.TiendaApp`.
