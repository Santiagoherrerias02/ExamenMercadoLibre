# 📝 Documentación de Tests - Mutant Detector API

## 📋 Índice

1. [Visión General](#visión-general)
2. [Estrategia de Testing](#estrategia-de-testing)
3. [Tests del Algoritmo - MutantDetectorTest](#tests-del-algoritmo---mutantdetectortest)
4. [Tests del Servicio - MutantServiceTest](#tests-del-servicio---mutantservicetest)
5. [Tests de Estadísticas - StatsServiceTest](#tests-de-estadísticas---statsservicetest)
6. [Tests del Controller - MutantControllerTest](#tests-del-controller---mutantcontrollertest)
7. [Ejecutar Tests](#ejecutar-tests)
8. [Cobertura de Código](#cobertura-de-código)

---

## Visión General

Este proyecto incluye **35 tests** distribuidos en 4 archivos que cubren:

- ✅ Lógica del algoritmo de detección
- ✅ Lógica de negocio y persistencia
- ✅ Cálculo de estadísticas
- ✅ Endpoints REST

### Distribución de Tests

| Archivo | Tests | Tipo | Cobertura |
|---------|-------|------|-----------|
| MutantDetectorTest | 16 | Unitarios | 96% |
| MutantServiceTest | 5 | Unitarios (con mocks) | 95% |
| StatsServiceTest | 6 | Unitarios (con mocks) | 100% |
| MutantControllerTest | 8 | Integración | 100% |
| **TOTAL** | **35** | - | **~90%** |

---

## Estrategia de Testing

### Pirámide de Testing Aplicada

```
         /\
        /  \      E2E Tests
       /    \     (No incluidos)
      /──────\
     /        \
    / Integración \    8 tests - Controller
   /    Tests      \
  /                 \
 /───────────────────\
/                     \
/  Tests Unitarios     \   27 tests - Service + Detector
/_______________________\
```

### Principios Aplicados

1. **AAA Pattern** - Arrange, Act, Assert
2. **Tests independientes** - No comparten estado
3. **Nombres descriptivos** - Se entiende qué se prueba
4. **Un concepto por test** - Fácil de debuggear
5. **Fast tests** - Ejecución rápida (<100ms unitarios)

---

## Tests del Algoritmo - MutantDetectorTest

**Archivo:** `src/test/java/org/example/service/MutantDetectorTest.java`

**Objetivo:** Verificar que el algoritmo de detección funcione correctamente en todos los casos.

### Configuración

```java
@BeforeEach
void setUp() {
    mutantDetector = new MutantDetector();
}
```

Se crea una instancia nueva antes de cada test para garantizar independencia.

---

### Categoría 1: Tests de Mutantes (7 tests)

Verifican que el algoritmo detecta correctamente ADN mutante.

#### Test 1: Secuencias Horizontal y Diagonal

```java
@Test
@DisplayName("Debe detectar mutante con secuencias horizontal y diagonal")
void testMutantWithHorizontalAndDiagonalSequences()
```

**ADN de entrada:**
```
ATGCGA
CAGTGC
TTATGT
AGAAGG
CCCCTA  ← Horizontal: CCCC
TCACTG
```

**¿Qué verifica?**
- Encuentra secuencia horizontal en fila 4
- Encuentra secuencia diagonal
- Retorna `true` (es mutante)

**Assertion:** `assertTrue(mutantDetector.isMutant(dna))`

---

#### Test 2: Secuencias Verticales

```java
@Test
@DisplayName("Debe detectar mutante con secuencias verticales")
void testMutantWithVerticalSequences()
```

**¿Qué verifica?**
- Detección de 4 letras iguales en columnas
- Búsqueda vertical funciona correctamente

---

#### Test 3: Múltiples Horizontales

```java
@Test
@DisplayName("Debe detectar mutante con múltiples secuencias horizontales")
void testMutantWithMultipleHorizontalSequences()
```

**ADN de entrada:**
```
TTTTGA  ← Secuencia 1: TTTT
CAGTGC
TTATGT
AGAAGG
CCCCTA  ← Secuencia 2: CCCC
TCACTG
```

**¿Qué verifica?**
- Encuentra más de una secuencia
- No se detiene en la primera

---

#### Test 4: Diagonales Ascendentes y Descendentes

```java
@Test
@DisplayName("Debe detectar mutante con diagonales ascendentes y descendentes")
void testMutantWithBothDiagonals()
```

**¿Qué verifica?**
- Diagonal descendente (↘): De arriba-izq a abajo-der
- Diagonal ascendente (↗): De abajo-izq a arriba-der

---

#### Test 5: Matriz Grande 10x10

```java
@Test
@DisplayName("Debe detectar mutante en matriz grande 10x10")
void testMutantWithLargeDna()
```

**¿Qué verifica?**
- Escalabilidad del algoritmo
- Funciona con matrices mayores a 6x6

---

#### Test 6: Todas las Bases Iguales

```java
@Test
@DisplayName("Debe detectar mutante con todas las bases iguales")
void testMutantAllSameCharacter()
```

**ADN de entrada:**
```
AAAAAA
AAAAAA
AAAAAA
AAAAAA
AAAAAA
AAAAAA
```

**¿Qué verifica?**
- Caso extremo: todo igual
- Early termination funciona (para rápido)

---

#### Test 7: Matriz Mínima 4x4

```java
@Test
@DisplayName("Debe detectar mutante en matriz mínima 4x4")
void testMutantSmallMatrix4x4()
```

**ADN de entrada:**
```
AAAA  ← Secuencia 1
CCCC  ← Secuencia 2
TTAT
AGAC
```

**¿Qué verifica?**
- Funciona con tamaño mínimo permitido (4x4)

---

### Categoría 2: Tests de Humanos (2 tests)

Verifican que el algoritmo rechaza correctamente ADN humano.

#### Test 8: Una Sola Secuencia

```java
@Test
@DisplayName("No debe detectar mutante con una sola secuencia")
void testNotMutantWithOnlyOneSequence()
```

**¿Qué verifica?**
- Con 1 secuencia → NO es mutante
- Se necesitan **más de 1** (>1, no ≥1)

**Assertion:** `assertFalse(mutantDetector.isMutant(dna))`

---

#### Test 9: Sin Secuencias

```java
@Test
@DisplayName("No debe detectar mutante sin secuencias")
void testNotMutantWithNoSequences()
```

**¿Qué verifica?**
- Sin ninguna secuencia de 4 → NO es mutante

---

### Categoría 3: Tests de Validación (6 tests)

Verifican que el algoritmo valida correctamente entradas inválidas.

#### Test 10: ADN Nulo

```java
@Test
@DisplayName("Debe rechazar ADN nulo")
void testNullDna()
```

**¿Qué verifica?**
- `null` retorna `false` (no lanza excepción)
- Validación defensiva

---

#### Test 11: ADN Vacío

```java
@Test
@DisplayName("Debe rechazar ADN vacío")
void testEmptyDna()
```

**¿Qué verifica?**
- Array vacío `[]` retorna `false`

---

#### Test 12: Matriz No Cuadrada

```java
@Test
@DisplayName("Debe rechazar matriz no cuadrada")
void testNonSquareMatrix()
```

**ADN de entrada:**
```
ATGCGA  ← 6 caracteres
CAGTGC  ← 6 caracteres
TTATGT  ← 6 caracteres
        (solo 3 filas) ❌
```

**¿Qué verifica?**
- Matriz debe ser NxN (cuadrada)
- 3x6 es inválida

---

#### Test 13: Caracteres Inválidos

```java
@Test
@DisplayName("Debe rechazar caracteres inválidos")
void testInvalidCharacters()
```

**ADN de entrada:**
```
ATGCGA
CAGTXC  ← 'X' es inválido ❌
TTATGT
```

**¿Qué verifica?**
- Solo acepta: A, T, C, G
- Otros caracteres → inválido

---

#### Test 14: Fila Nula

```java
@Test
@DisplayName("Debe rechazar fila nula en el array")
void testNullRowInArray()
```

**¿Qué verifica?**
- Ninguna fila puede ser `null`

---

#### Test 15: Matriz Muy Pequeña

```java
@Test
@DisplayName("Debe rechazar matriz muy pequeña (menor a 4x4)")
void testTooSmallMatrix()
```

**¿Qué verifica?**
- Tamaño mínimo es 4x4
- 3x3 o menor es inválido

---

### Categoría 4: Test de Optimización (1 test)

#### Test 16: Early Termination

```java
@Test
@DisplayName("Debe usar early termination para eficiencia")
void testEarlyTermination()
```

**¿Qué verifica?**
- El algoritmo **para** al encontrar 2+ secuencias
- No revisa toda la matriz innecesariamente
- Tiempo de ejecución < 10ms

**Código clave:**
```java
if (sequenceCount > 1) {
    return true;  // ← Para aquí
}
```

---

## Tests del Servicio - MutantServiceTest

**Archivo:** `src/test/java/org/example/service/MutantServiceTest.java`

**Objetivo:** Verificar la lógica de negocio, caché y persistencia.

### Configuración con Mockito

```java
@ExtendWith(MockitoExtension.class)
class MutantServiceTest {

    @Mock
    private MutantDetector mutantDetector;

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private MutantService mutantService;
}
```

**¿Por qué mocks?**
- `MutantDetector`: Ya está testeado, no necesitamos probarlo de nuevo
- `DnaRecordRepository`: No queremos conectar a BD real

---

### Test 1: Analizar y Guardar Mutante

```java
@Test
@DisplayName("Debe analizar ADN mutante y guardarlo en DB")
void testAnalyzeMutantDnaAndSave()
```

**Flujo del test:**
1. Mock: `findByDnaHash()` → `Optional.empty()` (no existe)
2. Mock: `isMutant()` → `true` (es mutante)
3. Ejecutar: `analyzeDna(dna)`
4. Verificar: Se llamó a `save()` una vez

**Verificaciones:**
```java
verify(mutantDetector, times(1)).isMutant(mutantDna);
verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class));
```

---

### Test 2: Analizar y Guardar Humano

```java
@Test
@DisplayName("Debe analizar ADN humano y guardarlo en DB")
void testAnalyzeHumanDnaAndSave()
```

**Diferencia con Test 1:**
- Mock: `isMutant()` → `false`
- Resultado esperado: `false`

---

### Test 3: Retornar Resultado Cacheado

```java
@Test
@DisplayName("Debe retornar resultado cacheado si el ADN ya fue analizado")
void testReturnCachedResultForAnalyzedDna()
```

**Flujo del test:**
1. Mock: `findByDnaHash()` → `Optional.of(record)` (YA existe)
2. Ejecutar: `analyzeDna(dna)`
3. Verificar: NO se llamó a `isMutant()` ni a `save()`

**¿Por qué es importante?**
- Si el ADN ya fue analizado, NO se reprocesa
- Se retorna el resultado guardado (caché)
- Ahorra tiempo de procesamiento

**Verificaciones:**
```java
verify(mutantDetector, never()).isMutant(any());
verify(dnaRecordRepository, never()).save(any());
```

---

### Test 4: Hash Consistente

```java
@Test
@DisplayName("Debe generar hash consistente para el mismo ADN")
void testConsistentHashGeneration()
```

**¿Qué verifica?**
- El mismo ADN genera el **mismo hash** siempre
- Crucial para que la caché funcione

**Hash SHA-256:**
- Input: `["ATGCGA", "CAGTGC", ...]`
- Output: `"3a5f2c9e8b1d4f7a..."` (64 caracteres hex)
- Siempre igual para la misma entrada

---

### Test 5: Guardar con Hash Correcto

```java
@Test
@DisplayName("Debe guardar registro con hash correcto")
void testSavesRecordWithCorrectHash()
```

**¿Qué verifica?**
- El registro guardado tiene:
    - Hash no nulo
    - Hash de 64 caracteres (SHA-256)
    - Campo `isMutant` correcto

**Matcher personalizado:**
```java
verify(dnaRecordRepository).save(argThat(record ->
    record.getDnaHash() != null &&
    record.getDnaHash().length() == 64 &&
    record.isMutant()
));
```

---

## Tests de Estadísticas - StatsServiceTest

**Archivo:** `src/test/java/org/example/service/StatsServiceTest.java`

**Objetivo:** Verificar el cálculo correcto de estadísticas.

### Configuración

```java
@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private StatsService statsService;
}
```

---

### Test 1: Cálculo Correcto de Estadísticas

```java
@Test
@DisplayName("Debe calcular estadísticas correctamente")
void testGetStatsWithData()
```

**Mocks:**
- `countByIsMutant(true)` → 40 mutantes
- `countByIsMutant(false)` → 100 humanos

**Resultado esperado:**
```json
{
  "count_mutant_dna": 40,
  "count_human_dna": 100,
  "ratio": 0.4
}
```

**Cálculo del ratio:**
```
ratio = count_mutant_dna / count_human_dna
      = 40 / 100
      = 0.4
```

---

### Test 2: Ratio sin Humanos

```java
@Test
@DisplayName("Debe retornar ratio correcto cuando no hay humanos")
void testGetStatsWithNoHumans()
```

**Caso especial:**
- 10 mutantes, 0 humanos
- División por cero → ratio = 10.0 (no infinito)

**Lógica implementada:**
```java
if (countHuman == 0) {
    return countMutant > 0 ? countMutant : 0.0;
}
```

---

### Test 3: Sin Datos

```java
@Test
@DisplayName("Debe retornar ratio 0 cuando no hay datos")
void testGetStatsWithNoData()
```

**Caso inicial:**
- 0 mutantes, 0 humanos
- ratio = 0.0

---

### Test 4: Ratio con Decimales

```java
@Test
@DisplayName("Debe calcular ratio con decimales correctamente")
void testGetStatsWithDecimalRatio()
```

**Cálculo:**
```
1 mutante / 3 humanos = 0.333...
```

**Assertion con delta:**
```java
assertEquals(0.333, stats.getRatio(), 0.001);
//           ↑ esperado  ↑ actual     ↑ tolerancia
```

**¿Por qué delta?**
- Números de punto flotante tienen pequeños errores de precisión
- Delta de 0.001 = tolerancia de 3 decimales

---

### Test 5: Cantidades Iguales

```java
@Test
@DisplayName("Debe retornar ratio 1.0 cuando hay igual cantidad")
void testGetStatsWithEqualCounts()
```

**Caso:**
- 50 mutantes, 50 humanos
- ratio = 1.0 (igual cantidad)

---

### Test 6: Grandes Cantidades

```java
@Test
@DisplayName("Debe manejar grandes cantidades de datos")
void testGetStatsWithLargeNumbers()
```

**Caso:**
- 1,000,000 mutantes
- 2,000,000 humanos
- ratio = 0.5

**¿Qué verifica?**
- El servicio escala con millones de registros
- Tipo `long` soporta números grandes

---

## Tests del Controller - MutantControllerTest

**Archivo:** `src/test/java/org/example/controller/MutantControllerTest.java`

**Objetivo:** Verificar que los endpoints REST funcionan correctamente.

### Configuración

```java
@WebMvcTest(MutantController.class)
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MutantService mutantService;

    @MockBean
    private StatsService statsService;
}
```

**¿Qué es MockMvc?**
- Simula requests HTTP sin levantar servidor real
- No usa puerto 8080
- Ejecuta el código del Controller directamente

---

### Test 1: POST /mutant - 200 OK para Mutante

```java
@Test
@DisplayName("POST /mutant debe retornar 200 OK para ADN mutante")
void testCheckMutantReturns200ForMutant()
```

**Request simulado:**
```json
POST /mutant
Content-Type: application/json

{
  "dna": ["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
}
```

**Mock:** `analyzeDna()` → `true`

**Assertion:** `status().isOk()` (200)

---

### Test 2: POST /mutant - 403 Forbidden para Humano

```java
@Test
@DisplayName("POST /mutant debe retornar 403 Forbidden para ADN humano")
void testCheckMutantReturns403ForHuman()
```

**Mock:** `analyzeDna()` → `false`

**Assertion:** `status().isForbidden()` (403)

**Lógica del Controller:**
```java
return isMutant
    ? ResponseEntity.ok().build()           // 200
    : ResponseEntity.status(HttpStatus.FORBIDDEN).build();  // 403
```

---

### Test 3: POST /mutant - 400 Bad Request para Nulo

```java
@Test
@DisplayName("POST /mutant debe retornar 400 Bad Request para ADN nulo")
void testCheckMutantReturns400ForNullDna()
```

**Request:**
```json
{
  "dna": null
}
```

**¿Qué ocurre?**
1. Request llega al Controller
2. `@Validated` dispara Bean Validation
3. `@ValidDnaSequence` detecta que es nulo
4. Spring retorna 400 automáticamente

**Assertion:** `status().isBadRequest()` (400)

---

### Test 4: POST /mutant - 400 para Array Vacío

```java
@Test
@DisplayName("POST /mutant debe retornar 400 Bad Request para ADN vacío")
void testCheckMutantReturns400ForEmptyDna()
```

**Request:**
```json
{
  "dna": []
}
```

**Validación:** Array vacío también es inválido

---

### Test 5: GET /stats - Retorna Estadísticas

```java
@Test
@DisplayName("GET /stats debe retornar estadísticas correctamente")
void testGetStatsReturnsCorrectData()
```

**Mock:** `getStats()` → `StatsResponse(40, 100, 0.4)`

**Assertions con jsonPath:**
```java
.andExpect(jsonPath("$.count_mutant_dna").value(40))
.andExpect(jsonPath("$.count_human_dna").value(100))
.andExpect(jsonPath("$.ratio").value(0.4))
```

**¿Qué es jsonPath?**
- Verifica campos específicos del JSON
- `$` = raíz del JSON
- `.count_mutant_dna` = campo del objeto

---

### Test 6: GET /stats - Sin Datos

```java
@Test
@DisplayName("GET /stats debe retornar 200 OK incluso sin datos")
void testGetStatsReturns200WithNoData()
```

**¿Qué verifica?**
- Endpoint funciona incluso con BD vacía
- Retorna `(0, 0, 0.0)`

---

### Test 7: POST /mutant - Rechaza Body Vacío

```java
@Test
@DisplayName("POST /mutant debe rechazar request sin body")
void testCheckMutantRejectsEmptyBody()
```

**Request:**
```http
POST /mutant
Content-Type: application/json

(sin body)
```

**GlobalExceptionHandler** captura `HttpMessageNotReadableException`

**Assertion:** `status().isBadRequest()` (400)

---

### Test 8: POST /mutant - Acepta JSON

```java
@Test
@DisplayName("POST /mutant debe aceptar Content-Type application/json")
void testCheckMutantAcceptsJsonContentType()
```

**¿Qué verifica?**
- Acepta `Content-Type: application/json`
- Otros tipos (XML, plain text) no son aceptados

---

## Ejecutar Tests

### Comandos Básicos

```bash
# Todos los tests
./gradlew test

# Test específico
./gradlew test --tests MutantDetectorTest
./gradlew test --tests MutantServiceTest
./gradlew test --tests StatsServiceTest
./gradlew test --tests MutantControllerTest

# Con reporte de cobertura
./gradlew test jacocoTestReport

# Solo compilar (sin tests)
./gradlew build -x test

# Limpiar y ejecutar tests
./gradlew clean test
```

### En Windows

```bash
gradlew.bat test
gradlew.bat test --tests MutantDetectorTest
gradlew.bat test jacocoTestReport
```

---

## Cobertura de Código

### Generar Reporte JaCoCo

```bash
./gradlew test jacocoTestReport
```

**Reporte en:** `build/reports/jacoco/test/html/index.html`

### Métricas por Archivo

| Clase | Cobertura | Líneas Cubiertas |
|-------|-----------|------------------|
| MutantDetector | 96% | 150/156 |
| MutantService | 95% | 45/47 |
| StatsService | 100% | 20/20 |
| MutantController | 100% | 25/25 |
| DnaRecord (entity) | 71% | - |
| DTOs | 71% | - |

**Nota sobre Lombok:**
- Lombok genera código automático (equals, hashCode, toString)
- Esto baja la cobertura reportada
- Lo importante: **lógica de negocio >90%**

### Configuración JaCoCo

```gradle
jacoco {
    toolVersion = "0.8.11"
}

jacocoTestReport {
    dependsOn test
    reports {
        xml.required = true
        html.required = true
    }
    afterEvaluate {
        classDirectories.setFrom(files(classDirectories.files.collect {
            fileTree(dir: it, exclude: [
                '**/MutantDetectorApplication.class',
                '**/config/**'
            ])
        }))
    }
}
```

**Exclusiones:**
- Clase main (`MutantDetectorApplication`)
- Configuraciones (`SwaggerConfig`)

---

## Resumen Final

### Estadísticas del Proyecto

| Métrica | Valor |
|---------|-------|
| **Tests Totales** | 35 |
| **Tests Unitarios** | 27 |
| **Tests Integración** | 8 |
| **Cobertura Total** | ~90% |
| **Tiempo Ejecución** | <5 segundos |

### Buenas Prácticas Aplicadas

✅ **Nombres descriptivos** - `@DisplayName` en cada test  
✅ **Patrón AAA** - Arrange, Act, Assert  
✅ **Tests independientes** - No comparten estado  
✅ **Mocks para aislamiento** - Sin dependencias externas  
✅ **Verificaciones completas** - Assert + Verify  
✅ **Cobertura alta** - >90% en lógica de negocio  
✅ **Fast tests** - Ejecución rápida

---

## Conclusión

La suite de tests de este proyecto garantiza:

🔒 **Confiabilidad** - Código probado en múltiples escenarios  
🚀 **Mantenibilidad** - Tests como documentación viva  
🐛 **Detección temprana** - Bugs encontrados antes de producción  
♻️ **Refactoring seguro** - Cambios sin miedo a romper funcionalidad

**Resultado:** Código robusto, testeado y listo para producción. ✅