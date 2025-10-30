# División Detallada del Trabajo

## Resumen Ejecutivo

Este documento detalla la división del trabajo en **3 partes principales** para permitir trabajo en paralelo. Cada integrante puede trabajar de forma independiente en su parte, con puntos de integración claramente definidos.

---

## PARTE 1: Analizador Léxico (JFlex)
**Archivo principal**: `src/jflex/RecetarioLexer.flex`  
**Integrante**: [Nombre]

### Objetivo
Crear el analizador léxico que reconoce todos los tokens del lenguaje de recetarios.

### Tareas Detalladas

#### 1. Tokens Básicos (Alta Prioridad)
- [ ] Reconocer palabra clave `RECETA`
- [ ] Reconocer palabra clave `INGREDIENTES`
- [ ] Reconocer palabra clave `PASOS`
- [ ] Reconocer secciones: `Tiempo`, `Porciones`, `Calorías`, `Categorías`, `Origen`, `Dificultad`, `Tipo`, `Recetas relacionadas`, `Obs`

#### 2. Números y Cantidades (Alta Prioridad)
- [ ] Reconocer números enteros (`200`, `3`)
- [ ] Reconocer números decimales con coma (`0,5`, `0.5`)
- [ ] Reconocer fracciones (`1/2`, `1 1/2`)
- [ ] Considerar expresión especial "a gusto"

#### 3. Unidades de Medida (Alta Prioridad)
- [ ] Unidades de peso: `g`, `kg`, `mg`
- [ ] Unidades de volumen: `l`, `ml`, `cm3`, `taza`, `tazas`
- [ ] Cucharas: `cuchara`, `cucharas`, `cucharita`, `cucharitas`
- [ ] Unidades simples: `u`, `unidad`, `unidades`
- [ ] Unidades de tiempo: `min`, `minutos`, `h`, `horas`

#### 4. Texto y Strings (Alta Prioridad)
- [ ] Reconocer strings entre comillas dobles (`"Brownies de Chocolate"`)
- [ ] Reconocer strings entre comillas simples (si aplica)
- [ ] Manejar caracteres especiales dentro de strings
- [ ] Reconocer identificadores (nombres de ingredientes, países, etc.)

#### 5. Símbolos Especiales (Alta Prioridad)
- [ ] Dos puntos `:`
- [ ] Coma `,`
- [ ] Corchetes `[` y `]`
- [ ] Igual `=` (para información adicional)
- [ ] Punto `.` (en pasos numerados)

#### 6. Categorías y Tipos (Media Prioridad)
- [ ] Reconocer categorías: Desayuno, Merienda, Principal, Entrada, Colación, Postre
- [ ] Reconocer niveles de dificultad como texto (`MEDIA`, `BAJA`, `ALTA`)
- [ ] Reconocer niveles de dificultad como estrellas (`***`)
- [ ] Reconocer tipos: `VEGETARIANO`, `Proteico`, etc.

#### 7. Calorías y Formato (Media Prioridad)
- [ ] Reconocer `Kcal`, `kcal`, `Calorías`
- [ ] Manejar espacios en blanco y saltos de línea
- [ ] Considerar comentarios (si se desea implementar)

#### 8. Testing (Alta Prioridad)
- [ ] Crear archivos de prueba con diferentes formatos
- [ ] Verificar reconocimiento correcto de todos los tokens
- [ ] Documentar casos especiales encontrados

### Puntos de Integración
- El lexer debe generar tokens que el parser (PARTE 2) pueda consumir
- Coordinar nombres de tokens con PARTE 2 antes de finalizar

### Entregables
1. Archivo `RecetarioLexer.flex` completo y funcional
2. Documentación de tokens reconocidos
3. Casos de prueba

---

## PARTE 2: Analizador Sintáctico y Estructura de Datos (CUP)
**Archivo principal**: `src/cup/RecetarioParser.cup`  
**Archivos Java**: `src/main/Receta.java`, `src/main/Ingrediente.java`  
**Integrante**: [Nombre]

### Objetivo
Crear el analizador sintáctico que valida la estructura del recetario y construye la representación en memoria.

### Tareas Detalladas

#### 1. Definición de Gramática Básica (Alta Prioridad)
- [ ] Definir producción para `recetario` (múltiples recetas)
- [ ] Definir producción para `receta` individual
- [ ] Definir producción para lista de ingredientes
- [ ] Definir producción para ingrediente (nombre, cantidad, unidad)
- [ ] Definir producción para lista de pasos
- [ ] Definir producción para paso numerado

#### 2. Campos Obligatorios (Alta Prioridad)
- [ ] Validar presencia de nombre de receta
- [ ] Validar presencia de lista de ingredientes (al menos 1)
- [ ] Validar presencia de lista de pasos (al menos 1)
- [ ] Validar presencia de tiempo
- [ ] Validar presencia de porciones
- [ ] Validar presencia de calorías
- [ ] Validar presencia de categorías (al menos 1)
- [ ] Validar presencia de dificultad

#### 3. Campos Opcionales (Media Prioridad)
- [ ] Manejar origen (puede estar o no)
- [ ] Manejar tipo (puede estar o no)
- [ ] Manejar observaciones (puede estar o no)
- [ ] Manejar recetas relacionadas (puede estar o no)
- [ ] Manejar información adicional como `Etiqueta=valor`

#### 4. Estructura de Datos (Alta Prioridad)
- [ ] Completar clase `Receta.java` con todos los campos
- [ ] Completar clase `Ingrediente.java`
- [ ] Crear clase `Paso.java` si es necesaria
- [ ] Implementar constructores y métodos de acceso
- [ ] Implementar método `toString()` para debugging

#### 5. Construcción del Árbol (Alta Prioridad)
- [ ] Construir objetos `Receta` durante el parsing
- [ ] Construir objetos `Ingrediente` y agregarlos a receta
- [ ] Agregar pasos a la lista de pasos
- [ ] Agregar categorías a la lista
- [ ] Construir mapa de información adicional

#### 6. Validaciones Sintácticas (Media Prioridad)
- [ ] Validar formato de lista de categorías `[Categoria1, Categoria2]`
- [ ] Validar formato de pasos (numeración correcta)
- [ ] Validar formato de lista de recetas relacionadas
- [ ] Validar formato de información adicional `Etiqueta=valor`

#### 7. Manejo de Errores (Media Prioridad)
- [ ] Mensajes de error claros cuando falta un campo obligatorio
- [ ] Mensajes de error cuando el formato es incorrecto
- [ ] Reportar línea y columna del error

#### 8. Testing (Alta Prioridad)
- [ ] Probar con recetario válido completo
- [ ] Probar con recetario que falta campos obligatorios
- [ ] Probar con recetario con formato incorrecto
- [ ] Verificar que se construyen correctamente los objetos en memoria

### Puntos de Integración
- Depende de PARTE 1: necesita que los tokens estén definidos
- Proporciona estructura de datos para PARTE 3
- Coordinar estructura de clases con PARTE 3 antes de finalizar

### Entregables
1. Archivo `RecetarioParser.cup` completo y funcional
2. Clases Java completas (`Receta`, `Ingrediente`, etc.)
3. Casos de prueba con recetarios válidos e inválidos

---

## PARTE 3: Validaciones Avanzadas y Funcionalidades Extras
**Archivos principales**: `src/main/Main.java`, `src/main/Validaciones.java`, etc.  
**Integrante**: [Nombre]

### Objetivo
Implementar funcionalidades avanzadas, validaciones adicionales y desafíos opcionales.

### Tareas Detalladas

#### 1. Clase Principal y Interfaz (Alta Prioridad)
- [ ] Completar clase `Main.java`
- [ ] Integrar lexer y parser de PARTE 1 y PARTE 2
- [ ] Crear menú interactivo o interfaz por línea de comandos
- [ ] Manejar argumentos de línea de comandos
- [ ] Manejar archivos de entrada

#### 2. Desafío A: Validación de Recetas Relacionadas (Media Prioridad)
- [ ] Crear método que valide existencia de recetas relacionadas
- [ ] Comparar nombres de recetas relacionadas con recetas en el recetario
- [ ] Reportar recetas relacionadas que no existen
- [ ] Considerar diferencias de mayúsculas/minúsculas y espacios

#### 3. Desafío B: Validación de Unidades por Ingrediente (Media Prioridad)
- [ ] Crear mapa de ingredientes → unidades válidas
  - Ejemplo: `leche` → `[l, ml, taza, cm3]`
  - Ejemplo: `harina` → `[g, kg, taza]` (NO puede ser `cucharita`)
- [ ] Validar cada ingrediente contra su unidad
- [ ] Reportar errores cuando la unidad no es válida para el ingrediente
- [ ] Manejar ingredientes no reconocidos

#### 4. Desafío C: Carrito de Compras (Alta Prioridad - Interesante)
- [ ] Crear parser para entrada tipo: `CARRITO: 20 personas, RECETAS: [brownies, tortilla...]`
- [ ] Calcular escalado de cantidades según número de porciones
  - Ejemplo: Receta hace 8 porciones, necesito para 20 → multiplicar por 20/8
- [ ] Consolidar ingredientes comunes (sumar cantidades)
- [ ] Convertir a unidades comunes si es necesario
- [ ] Generar lista de compras ordenada y legible

#### 5. Desafío D: Múltiples Formatos de Tiempo (Media Prioridad)
- [ ] Extender reconocimiento de tiempo:
  - `1h 15m`, `1h 15 min`
  - `1 15'` (notación con prima)
  - `1h 1/4`, `1,25h`
  - `75m`, `75min`
  - `5/4h`
- [ ] Normalizar todos los formatos a una representación común
- [ ] Validar formato en el parser o en validaciones

#### 6. Desafío E: Resumen de Recetas (Media Prioridad)
- [ ] Calcular estadísticas:
  - Cantidad de recetas por tipo de comida
  - Porcentajes por procedencia (origen)
  - Cantidad de recetas por nivel de dificultad
  - Promedio de tiempo por nivel de dificultad
  - Promedio de tiempo por tipo
- [ ] Generar reporte formateado

#### 7. Desafío F: Generación de Menú Semanal (Alta Prioridad - Interesante)
- [ ] Parser para entrada: `MENU "ligth", CALORIAS=1000`
- [ ] Selección aleatoria de recetas:
  - 1 receta de desayuno
  - 1 receta de almuerzo/principal
  - 1 receta de merienda
  - 1 receta de cena
- [ ] Validar tope de calorías por día
- [ ] Asegurar variedad (no repetir mucho)
- [ ] Generar menú para toda la semana

#### 8. Manejo de Errores Robusto (Media Prioridad)
- [ ] Try-catch para errores de archivo
- [ ] Manejo de errores de parsing
- [ ] Mensajes de error informativos al usuario
- [ ] Logging básico (opcional)

#### 9. Testing y Documentación (Alta Prioridad)
- [ ] Crear casos de prueba para cada desafío implementado
- [ ] Documentar cómo usar cada funcionalidad
- [ ] Crear ejemplos de uso
- [ ] Actualizar README con instrucciones

### Puntos de Integración
- Depende completamente de PARTE 1 y PARTE 2
- Utiliza las clases Java creadas en PARTE 2
- Puede requerir extensiones en el parser si los desafíos C y F requieren nueva sintaxis

### Entregables
1. Clase `Main.java` funcional
2. Implementación de al menos 3 desafíos opcionales
3. Documentación de uso
4. Ejemplos de ejecución

---

## Cronograma Sugerido

### Semana 1
- **PARTE 1**: Completar tokens básicos (al menos 80%)
- **PARTE 2**: Definir gramática básica y estructura de clases
- **PARTE 3**: Planificar funcionalidades y preparar estructura

### Semana 2
- **PARTE 1**: Finalizar todos los tokens, testing
- **PARTE 2**: Completar parser, validaciones básicas, testing
- **PARTE 3**: Integrar PARTE 1 y 2, comenzar desafíos

### Semana 3
- **Todas las partes**: Integración completa
- **PARTE 3**: Finalizar desafíos
- **Equipo**: Testing conjunto, documentación, presentación

---

## Coordinación y Comunicación

### Reuniones Sugeridas
1. **Kickoff**: Al inicio, para alinear expectativas
2. **Checkpoint Semana 1**: Revisar avances y coordinar tokens
3. **Checkpoint Semana 2**: Integrar partes 1 y 2
4. **Finalización**: Testing conjunto y revisión

### Puntos Críticos de Coordinación
1. **Nombres de tokens**: PARTE 1 debe coordinar con PARTE 2 antes de finalizar
2. **Estructura de clases**: PARTE 2 debe coordinar con PARTE 3
3. **Integración**: Semana 2 es crítica para integrar parser y lexer

---

## Notas Finales

- Cada parte tiene tareas de **alta prioridad** que deben completarse
- Las tareas de **media prioridad** son importantes pero se pueden ajustar según tiempo
- Los desafíos pueden elegirse según interés, pero se recomienda implementar al menos 3
- La comunicación entre integrantes es crucial para una buena integración



