# HotelApp - Parte 1

Sistema de reservas de hotel desarrollado en **JavaFX + Maven**.  
Incluye módulo de **Informes y análisis de reservas** para el gerente (Tema 9 / Tarjeta de exposición UT6).

---

## Estructura del proyecto

```
HotelApp_Parte1/
├── pom.xml
├── README.md
└── src/main/
    ├── java/
    │   ├── Main.java                 # Punto de entrada JavaFX
    │   ├── HotelController.java      # Controlador de la vista (hotel + informes)
    │   └── Reserva.java              # Modelo de datos de una reserva
    └── resources/
        ├── vista.fxml                # Interfaz gráfica (pestañas: Habitaciones / Informes)
        └── reservas.xml              # Datos del hotel, habitaciones y reservas
```

---

## Características

### 1. Gestión de habitaciones
- Listado de todas las habitaciones del hotel con tipo, capacidad, precio y estado.
- Búsqueda por número de habitación para consultar si está disponible u ocupada.

### 2. Informes de reservas (según las capturas de la tarea)
- **Tabla de reservas**: código, estado, cliente, habitación, fecha de entrada, salida y número de personas.
- **Filtros**:
  - Por estado: Todos, Confirmada, Finalizada, Cancelada, Pendiente.
  - Por mes: Todos los meses, Octubre 2026, Noviembre 2026.
- **Totales (agrupación por estado)**:
  - Total de reservas filtradas.
  - Reservas confirmadas, finalizadas, canceladas y pendientes.
- Los datos se leen directamente de `reservas.xml` (no se modifica el archivo original).

---


### Requisitos
- Java 21 (o superior)
- Maven



## Datos de ejemplo (`reservas.xml`)

El XML contiene información de **Hotel Sol** (Madrid) con:
- 30 habitaciones en 3 plantas (estados: ocupada, disponible, mantenimiento).
- 30 reservas con estados: **confirmada**, **finalizada**, **cancelada**, **pendiente**.
- Fechas en octubre y noviembre de 2026.

---

## Integrantes
- Nacho
- Raul
- Iñigo

---
