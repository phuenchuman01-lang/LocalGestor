# Gestor Local / Mapu ñi rakizuamulu

## Descripción del proyecto
El presente repositorio contiene el código fuente de una herramienta de software diseñada para administrar contenedores y casilleros dentro de un laboratorio universitario. El programa busca agilizar la búsqueda de objetos y eliminar la necesidad de memorizar la ubicación exacta de los elementos almacenados. El sistema opera bajo el patrón de diseño arquitectónico Modelo-Vista-Controlador y funciona mediante una interfaz de entrada y salida basada en la consola del sistema operativo.

## Funcionalidades principales
*   **Separación de accesos:** Diferenciación estructural entre los comandos de administración global y las herramientas orientadas a los usuarios regulares del recinto.
*   **Componente visual:** Renderizado de una matriz bidimensional mediante texto para ilustrar espacialmente la cuadrícula de contenedores y su estado de ocupación en tiempo real.
*   **Gestión de inventario:** Capacidad técnica para agregar, retirar y reubicar objetos específicos dentro de las celdas virtuales habilitadas.
*   **Motor de búsqueda:** Algoritmo integrado para encontrar un ítem en particular y devolver al operador sus coordenadas exactas dentro del espacio de almacenamiento.

## Tecnologías empleadas
*   **Lenguaje de programación:** Java Standard Edition.
*   **Arquitectura:** Patrón Modelo-Vista-Controlador.
*   **Almacenamiento de datos (fase en desarrollo):** Persistencia estructural mediante lectura y escritura de archivos JSON.

## Instrucciones de ejecución
Para iniciar la simulación del entorno es necesario compilar las clases del código fuente e invocar el método principal. Al arrancar el programa, la terminal desplegará el menú inicial de autenticación esperando las instrucciones numéricas del operador. Toda la navegación posterior ocurre mediante respuestas por teclado.

## Equipo de desarrollo
*   Luis Arias Quezada
*   Patricio Huenchuman

---
Proyecto desarrollado como requisito académico para la estructuración de soluciones orientadas a objetos.