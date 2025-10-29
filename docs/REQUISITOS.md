# Análisis de Requisitos del Trabajo

## Requisitos Mínimos (Obligatorios)

### 1. Reconocer el formato básico de un recetario completo ✓
- Un recetario puede contener múltiples recetas
- Cada receta está separada por líneas en blanco o delimitadores claros
- El parser debe procesar todo el archivo

### 2. Identificar y validar todas las partes de una receta ✓
**Partes obligatorias:**
- Nombre de la receta (entre comillas)
- Lista de ingredientes
- Pasos de preparación
- Tiempo
- Porciones
- Calorías
- Categorías (al menos una)
- Dificultad

**Partes opcionales:**
- Origen
- Tipo
- Observaciones
- Recetas relacionadas
- Información adicional (Etiqueta=valor)

### 3. Identificar y validar ingredientes, cantidades y unidades ✓
**Formatos de cantidad:**
- Enteros: `200`, `3`
- Decimales: `0,5`, `0.5`, `0.3`
- Fracciones: `1/2`, `1 1/2`
- Texto especial: `a gusto`

**Unidades posibles:**
- Peso: `g`, `kg`, `mg`
- Volumen: `l`, `ml`, `cm3`, `taza`, `tazas`
- Cucharas: `cuchara`, `cucharas`, `cucharita`, `cucharitas`
- Unidades: `u`, `unidad`, `unidades`

### 4. Procesar la lista de pasos numerados ✓
- Formato: `1. Descripción del paso.`
- Los pasos deben estar numerados secuencialmente
- Cada paso puede terminar con punto o no

### 5. Soporte para múltiples recetas en un archivo ✓
- El parser debe poder procesar N recetas
- Cada receta es independiente

### 6. Manejar información adicional flexible ✓
- Formato: `Etiqueta=valor`
- Ejemplos:
  - `Grasas=Alto`
  - `Habilitado=Diabético`
  - `Costo=$$$`
- Puede haber múltiples pares Etiqueta=valor

### 7. Reportar errores de formato ✓
- Debe detectar cuando falta un campo obligatorio
- Debe detectar formato incorrecto
- Debe reportar línea y columna del error cuando sea posible

---

## Consideraciones de Diseño

### Elementos Obligatorios vs Opcionales

**Obligatorios (error si faltan):**
1. Nombre de receta
2. Lista de ingredientes (al menos 1)
3. Lista de pasos (al menos 1)
4. Tiempo
5. Porciones
6. Calorías
7. Categorías (al menos 1)
8. Dificultad

**Opcionales (pueden no estar):**
1. Origen
2. Tipo
3. Observaciones
4. Recetas relacionadas
5. Información adicional

### Manejo de Unidades de Medida

**Decisiones de diseño:**
- Almacenar la cantidad tal como viene en el texto (puede ser fracción, decimal, etc.)
- Almacenar la unidad como string
- Para cálculos (carrito de compras), necesitaremos conversiones
- Considerar normalización de unidades (ej: `taza` vs `tazas`)

### Variaciones en el Formato

**Tolerancia:**
- Espacios en blanco extra deben ser ignorados
- Saltos de línea pueden variar
- Orden de campos opcionales puede variar
- Formato de tiempo puede variar (ver Desafío D)

### Estructura de la Gramática

**Enfoque flexible:**
- Usar producciones opcionales para campos opcionales
- Permitir que campos opcionales aparezcan en cualquier orden (desafío)
- O fijar un orden específico para simplificar (recomendado inicialmente)

---

## Desafíos Adicionales (Opcionales)

### A. Validar Existencia de Recetas Relacionadas
- Al procesar una receta, verificar que todas las recetas en "Recetas relacionadas" existan en el recetario
- Comparar nombres (considerar diferencias de mayúsculas/minúsculas)
- Reportar advertencias o errores

### B. Ingredientes vs Unidades Posibles
- Crear un diccionario de ingredientes → unidades válidas
- Validar que cada ingrediente use solo unidades permitidas
- Ejemplo: `leche` puede ser `l`, `ml`, `taza`, pero NO `kg` ni `cucharita`
- Ejemplo: `harina` puede ser `g`, `kg`, `taza`, pero NO `cucharita`

### C. Extensión "Carrito de Compras"
**Entrada:**
```
CARRITO: 20 personas, RECETAS: [brownies, tortilla española, guiso, churrascos]
```

**Proceso:**
1. Identificar recetas en la lista
2. Para cada receta:
   - Calcular factor de escalado: `factor = personas_necesarias / porciones_receta`
   - Multiplicar cada cantidad de ingrediente por el factor
3. Consolidar ingredientes comunes:
   - Si dos recetas tienen `harina`, sumar las cantidades
   - Considerar conversión de unidades si es necesario
4. Generar lista única de ingredientes con cantidades totales

**Ejemplo:**
- Brownies (8 porciones) para 20 personas → factor = 20/8 = 2.5
- Si brownies tiene 200g de harina → necesito 200 × 2.5 = 500g

### D. Aceptar Diferentes Formatos de Tiempo
**Formatos a soportar:**
- `45 min`
- `1h 15 min` o `1h 15m` o `1h 15'`
- `1,25h` o `1.25h`
- `75m` o `75min`
- `5/4h` (fracción de hora)
- `1h 1/4`

**Normalización:**
- Convertir todo a minutos o a formato estándar
- Validar en el lexer o en una función auxiliar

### E. Extensión "Resumen de Recetas"
**Estadísticas a calcular:**
1. Cantidad de recetas por tipo de comida (Desayuno, Principal, etc.)
2. Porcentajes por procedencia (origen)
3. Cantidad de recetas por nivel de dificultad
4. Promedio de tiempo de recetas por nivel de dificultad
5. Promedio de tiempo de recetas por tipo

**Salida:**
- Generar reporte formateado con estas estadísticas

### F. Generación Automática de Menú Semanal
**Entrada:**
```
MENU "ligth", CALORIAS=1000
```

**Proceso:**
1. Filtrar recetas según criterio (ej: "ligth" podría filtrar por tipo o calorías)
2. Para cada día de la semana:
   - Seleccionar aleatoriamente:
     - 1 receta de desayuno (considerar 1 porción)
     - 1 receta de principal/almuerzo (considerar 1 porción)
     - 1 receta de merienda (considerar 1 porción)
     - 1 receta de cena (considerar 1 porción)
   - Calcular calorías totales del día
   - Si excede el tope, ajustar selección
3. Asegurar variedad (no repetir demasiado las mismas recetas)

**Salida:**
```
Lunes= {Queso, Guiso, huevos revueltos, pescado alemán}
Martes= {Tostadas, Tarta zapallitos, Fiambre y queso, Milanesas con puré}
...
Domingo= {Waffles salados, Tarta zapallitos, Fiambre y queso, Milanesas con puré}
```

---

## Priorización Recomendada

### Fase 1 (Esencial - Semana 1)
1. ✅ Lexer básico (tokens principales)
2. ✅ Parser básico (estructura mínima)
3. ✅ Validación de campos obligatorios

### Fase 2 (Integración - Semana 2)
1. ✅ Lexer completo
2. ✅ Parser completo con campos opcionales
3. ✅ Estructura de datos completa
4. ✅ Manejo básico de errores

### Fase 3 (Extras - Semana 3)
1. ⭐ Desafío C (Carrito) - Muy interesante y demostrativo
2. ⭐ Desafío F (Menú semanal) - Muy interesante y demostrativo
3. Desafío A (Validar relacionadas) - Fácil de implementar
4. Desafío E (Resumen) - Fácil de implementar
5. Desafío B (Validar unidades) - Requiere definir diccionario
6. Desafío D (Formatos tiempo) - Requiere trabajo en lexer/parser

---

## Decisiones de Implementación Pendientes

1. **Orden de campos**: ¿Se permite cualquier orden o hay un orden específico?
2. **Normalización de unidades**: ¿Normalizar `taza`/`tazas` o mantener como viene?
3. **Validación de números**: ¿Aceptar tanto `0,5` (coma) como `0.5` (punto)?
4. **Manejo de errores**: ¿Fallo completo o continuar procesando y reportar al final?
5. **Formato de salida**: ¿Qué formato usar para reportes y resúmenes?

