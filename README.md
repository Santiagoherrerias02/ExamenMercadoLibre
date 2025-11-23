# 🧬 Mutant Detector API

API REST construida con **Spring Boot** para determinar si una secuencia de ADN corresponde a un mutante o a un humano, como parte de un desafío de desarrollo.

El proyecto implementa la lógica de detección y almacena los resultados para generar estadísticas.

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java 21 (configurado vía Gradle Toolchain)
* **Framework:** Spring Boot 3.2.0
* **Sistema de Build:** Gradle (con `jacoco` para cobertura de código)
* **Base de Datos:** H2 Database (en memoria, ideal para el ambiente de desarrollo)
* **ORM:** Spring Data JPA
* **Documentación:** Springdoc OpenAPI / Swagger UI
* **Utilidades:** Lombok

## 🚀 Puesta en Marcha

### Prerrequisitos

Asegúrate de tener instalado:

* **Java Development Kit (JDK) 21**
* **Gradle** (o usar el wrapper incluido `./gradlew`)

### Ejecución del Proyecto

El proyecto puede ser ejecutado directamente con el *wrapper* de Gradle incluido:

```bash
# Ejecutar la aplicacion en modo desarrollo
./gradlew bootRun