# 🧬 Mutant Detector API

API REST para detectar mutantes analizando secuencias de ADN - Examen MercadoLibre Backend Developer

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Tests](https://img.shields.io/badge/Tests-35%20passing-success.svg)]()
[![Coverage](https://img.shields.io/badge/Coverage-90%25-brightgreen.svg)]()

## 🚀 Inicio Rápido

```bash
# Clonar repositorio
git clone <tu-repo>
cd ExamenMercadoLibre

# Compilar y ejecutar tests
./gradlew test

# Ejecutar aplicación
./gradlew bootRun

# Abrir Swagger UI
# http://localhost:8080/swagger-ui.html
```

## 📋 Requisitos

- Java 21+
- Gradle 8.x (incluido wrapper)

## 🧪 Testing

```bash
# Ejecutar todos los tests (35 tests)
./gradlew test

# Generar reporte de cobertura
./gradlew test jacocoTestReport

# Ver reporte: build/reports/jacoco/test/html/index.html
```

**Tests incluidos:**
- 17 tests - MutantDetectorTest (algoritmo)
- 5 tests - MutantServiceTest (lógica negocio)
- 6 tests - StatsServiceTest (estadísticas)
- 8 tests - MutantControllerTest (endpoints REST)

## 🌐 Endpoints

### POST /mutant
Verifica si un ADN es mutante.

**Request:**
```json
{
  "dna": ["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
}
```

**Responses:**
- `200 OK` - Es mutante
- `403 Forbidden` - No es mutante
- `400 Bad Request` - ADN inválido

### GET /stats
Obtiene estadísticas de verificaciones.

**Response:**
```json
{
  "count_mutant_dna": 40,
  "count_human_dna": 100,
  "ratio": 0.4
}
```

## 🏗️ Arquitectura

```
src/main/java/org/example/
├── controller/     - Endpoints REST
├── dto/            - Request/Response
├── service/        - Lógica de negocio
├── repository/     - Acceso a BD
├── entity/         - Entidades JPA
├── validation/     - Validaciones custom
├── exception/      - Manejo de errores
└── config/         - Configuración
```

## 🐳 Docker

```bash
# Construir imagen
docker build -t mutantes-api .

# Ejecutar contenedor
docker run -p 8080:8080 mutantes-api
```

## ⚡ Optimizaciones

- ✅ Early Termination (para al encontrar 2+ secuencias)
- ✅ Caché con Hash SHA-256
- ✅ Conversión a char[][] para acceso O(1)
- ✅ Índices en BD
- ✅ Boundary Checking

## 📊 Cobertura

- **Algoritmo (MutantDetector):** 96%
- **Servicios:** 95%+
- **Controller:** 100%
- **Total:** ~90%

## 📚 Documentación

- [GUIA_EVALUACION_ESTUDIANTE.md](GUIA_EVALUACION_ESTUDIANTE.md) - Guía detallada del examen
- [documentacionTest.md](documentacionTest.md) - Documentación completa de tests
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## 📄 Licencia

MIT License

## Comentario

Queria probar lo de docker por eso esta el archivo (Dockerfile)
