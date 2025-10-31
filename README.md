# Analizador de Recetario Digital

Trabajo práctico de Teoría de la Computación - Análisis Léxico y Sintáctico con JFlex y CUP

## Descripción del Proyecto

Este proyecto implementa un analizador léxico (JFlex) y sintáctico (CUP) para procesar archivos de recetas culinarias estructuradas, validando su formato y permitiendo consultas sobre el recetario.

## Estructura del Proyecto

```
.
├── src/
│   ├── jflex/          # Archivos JFlex (.flex)
│   ├── cup/            # Archivos CUP (.cup)
│   └── main/           # Clases Java principales
├── examples/            # Ejemplos de recetarios
├── output/              # Archivos generados (compilación)
├── tests/               # Casos de prueba
├── docs/                # Documentación adicional
├── Makefile             # Compilación automatizada
├── .gitignore
└── README.md
```

## Requisitos

- Java JDK 8 o superior
- JFlex
- CUP (Constructor de Utilidades para Parsers)
- Make (opcional, para usar el Makefile)

## Compilación

```bash
make clean
make all
```

## Ejecución

```bash
java -cp output Main examples/recetario.txt
```

## División del Trabajo

El proyecto está dividido en **3 partes principales** para trabajo en paralelo:

### **PARTE 1: Analizador Léxico (JFlex)**
**Responsable**: [Nombre del integrante 1]

**Tareas**:
- Definir tokens para todos los elementos del recetario
- Reconocer palabras clave (RECETA, INGREDIENTES, PASOS, etc.)
- Reconocer nombres de recetas (entre comillas)
- Reconocer cantidades numéricas con diferentes formatos (decimales, fracciones)
- Reconocer unidades de medida (g, kg, l, ml, taza, cuchara, cucharita, u, etc.)
- Reconocer categorías, niveles de dificultad, tipos
- Reconocer información adicional (Etiqueta=valor)
- Manejar comentarios y espacios en blanco
- Manejar saltos de línea y formato

**Archivos principales**: `src/jflex/RecetarioLexer.flex`

**Deliverables**:
- [ ] Archivo .flex completo con todos los tokens definidos
- [ ] Pruebas unitarias verificando reconocimiento de tokens
- [ ] Documentación de tokens reconocidos

---

### **PARTE 2: Analizador Sintáctico y Validaciones Básicas (CUP)**
**Responsable**: [Nombre del integrante 2]

**Tareas**:
- Definir la gramática del recetario
- Implementar producción para recetario completo (múltiples recetas)
- Implementar producción para una receta individual
- Validar estructura obligatoria: nombre, ingredientes, pasos, tiempo, porciones, calorías, categorías, dificultad
- Manejar elementos opcionales: origen, tipo, observaciones, recetas relacionadas, info adicional
- Validar formato de lista de ingredientes
- Validar formato de pasos numerados
- Validar formato de categorías (lista entre corchetes)
- Generar árbol sintáctico o estructura de datos
- Manejo básico de errores sintácticos

**Archivos principales**: `src/cup/RecetarioParser.cup`, `src/main/Receta.java`, `src/main/Ingrediente.java`

**Deliverables**:
- [ ] Archivo .cup con gramática completa
- [ ] Clases Java para representar estructura de datos (Receta, Ingrediente, Pasos, etc.)
- [ ] Validaciones de formato básicas
- [ ] Pruebas con ejemplos de recetarios válidos e inválidos

---

### **PARTE 3: Validaciones Avanzadas y Funcionalidades Extras**
**Responsable**: [Nombre del integrante 3]

**Tareas**:
- Implementar validación de recetas relacionadas (desafío A)
- Implementar validación de unidades de medida por ingrediente (desafío B)
- Implementar extensión "Carrito de compras" (desafío C)
- Implementar múltiples formatos de tiempo (desafío D)
- Implementar extensión "Resumen de recetas" (desafío E)
- Implementar generación de menú semanal (desafío F)
- Clase principal (Main) con interfaz de usuario
- Manejo de errores y mensajes informativos
- Documentación final

**Archivos principales**: `src/main/Main.java`, `src/main/Validaciones.java`, `src/main/CarritoCompras.java`, etc.

**Deliverables**:
- [ ] Implementación de al menos 3 desafíos opcionales
- [ ] Clase Main funcional
- [ ] Manejo de errores robusto
- [ ] Documentación completa de funcionalidades

---

## Cronograma Sugerido

- **Semana 1**: Implementación de Parte 1 y 2 en paralelo
- **Semana 2**: Integración de Parte 1 y 2, inicio de Parte 3
- **Semana 3**: Finalización de Parte 3, pruebas integradas, documentación

## Convenciones de Código

- Comentarios en español
- Nombres de clases en PascalCase
- Nombres de variables en camelCase
- Cada integrante trabaja en su rama: `feature/parte1`, `feature/parte2`, `feature/parte3`

## Git Workflow

1. Trabajar en ramas separadas
2. Hacer commits frecuentes con mensajes descriptivos
3. Antes de mergear, verificar que compile
4. Resolver conflictos en equipo

## Contacto

[Agregar información de contacto del equipo]