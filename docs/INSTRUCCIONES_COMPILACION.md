# Instrucciones de Compilación

Guía para compilar y ejecutar el Analizador de Recetario Digital.

## Requisitos Previos

- Java JDK 8 o superior
- JFlex instalado y disponible en el PATH
- Make (opcional, para usar el Makefile)
- Los archivos JAR necesarios se encuentran en `libs/`:
  - `java-cup-11b.jar` (para generar el parser)
  - `java-cup-runtime.jar` (runtime necesario para ejecutar)

## Compilación con Makefile (Recomendado)

### Compilación Completa

```bash
# Limpiar archivos anteriores
make clean

# Generar lexer, parser y compilar todo
make all
```

Este comando:
1. Genera `RecetarioLexer.java` desde `RecetarioLexer.flex`
2. Genera `RecetarioParser.java` y `sym.java` desde `RecetarioParser.cup`
3. Compila todos los archivos Java (fuentes y generados)

### Ejecución

```bash
# Ejecutar con el ejemplo por defecto
make run

# O ejecutar manualmente con un archivo específico
java -cp "output:libs/java-cup-runtime.jar" main.Main examples/recetario.txt
```

### Otros Comandos Útiles

```bash
# Solo generar el lexer
make lexer

# Solo generar el parser
make parser

# Solo compilar (requiere que lexer y parser ya estén generados)
make compile

# Ejecutar todas las pruebas
make test

# Limpiar todos los archivos generados
make clean
```

## Compilación Manual

Si prefieres compilar paso a paso o usar un IDE:

### Paso 1: Crear directorio de salida

```bash
mkdir -p output
```

### Paso 2: Generar el Lexer

```bash
jflex -d output src/jflex/RecetarioLexer.flex
```

Esto genera `output/RecetarioLexer.java` en el paquete `cup`.

### Paso 3: Generar el Parser

```bash
java -cp libs/java-cup-11b.jar java_cup.Main \
  -parser RecetarioParser \
  -symbols sym \
  -destdir output \
  -expect 1 \
  src/cup/RecetarioParser.cup
```

Esto genera:
- `output/RecetarioParser.java` (paquete `cup`)
- `output/sym.java` (paquete `cup`)

### Paso 4: Compilar Todo

```bash
javac -d output -cp "output:libs/java-cup-runtime.jar" \
  src/main/*.java \
  output/*.java
```

Los archivos `.class` compilados se generarán en:
- `output/main/` (clases del paquete `main`)
- `output/cup/` (clases del paquete `cup`)

### Paso 5: Ejecutar

```bash
java -cp "output:libs/java-cup-runtime.jar" main.Main examples/recetario.txt
```

## Configuración en IDE

### IntelliJ IDEA

1. **Abrir el proyecto:**
   - File → Open → Selecciona la carpeta del proyecto

2. **Configurar librerías:**
   - File → Project Structure → Libraries
   - Click "+" → Java
   - Selecciona `libs/java-cup-runtime.jar`
   - Aplica los cambios

3. **Configurar source folders:**
   - File → Project Structure → Modules
   - En "Sources", marca `src/` como "Sources" (azul)
   - Marca `output/` como "Sources" (azul) - **Importante:** esto debe hacerse después de ejecutar `make all`

4. **Compilar:**
   - Ejecuta `make all` en la terminal integrada
   - O usa Build → Rebuild Project

### Eclipse

1. **Importar el proyecto:**
   - File → Import → Existing Projects into Workspace
   - Selecciona la carpeta del proyecto

2. **Configurar classpath:**
   - Click derecho en el proyecto → Properties
   - Java Build Path → Libraries
   - Add External JARs → Selecciona `libs/java-cup-runtime.jar`
   - Java Build Path → Source → Add Folder → Selecciona `output/`

3. **Compilar:**
   - Ejecuta `make all` en la terminal
   - O Project → Clean → Clean all projects

### VS Code

1. **Instalar extensión Java (si no está instalada):**
   - Extension Pack for Java

2. **Crear/editar `.vscode/settings.json`:**
```json
{
  "java.project.sourcePaths": ["src", "output"],
  "java.project.referencedLibraries": [
    "libs/**/*.jar"
  ]
}
```

3. **Compilar:**
   - Abre terminal integrada: `make all`
   - O usa el comando de compilación del IDE

## Estructura de Archivos Generados

Después de compilar, la estructura en `output/` será:

```
output/
├── RecetarioLexer.java      # Generado por JFlex (paquete cup)
├── RecetarioParser.java     # Generado por CUP (paquete cup)
├── sym.java                 # Generado por CUP (paquete cup)
├── cup/
│   ├── RecetarioLexer.class
│   ├── RecetarioParser.class
│   └── sym.class
└── main/
    ├── Main.class
    ├── Receta.class
    ├── Recetario.class
    ├── Ingrediente.class
    └── CamposOpcionales.class
```

## Verificación de Compilación Exitosa

Para verificar que todo compiló correctamente:

1. **Archivos generados:**
   ```bash
   ls -la output/*.java
   ```
   Debe mostrar: `RecetarioLexer.java`, `RecetarioParser.java`, `sym.java`

2. **Archivos compilados:**
   ```bash
   find output -name "*.class"
   ```
   Debe mostrar múltiples archivos `.class` en `output/cup/` y `output/main/`

3. **Ejecución de prueba:**
   ```bash
   make run
   ```
   Debe ejecutar sin errores y mostrar el análisis del recetario.

## Solución de Problemas Comunes

### Error: "package java_cup.runtime does not exist"

**Causa:** El classpath no incluye las librerías JAR.

**Solución:**
- Verifica que `libs/java-cup-runtime.jar` existe
- Añade el JAR al classpath al compilar: `-cp "output:libs/java-cup-runtime.jar"`
- En IDE: añade el JAR como librería externa

### Error: "package cup does not exist"

**Causa:** Los archivos generados no existen o no están en el classpath.

**Solución:**
1. Ejecuta `make all` para generar los archivos
2. Verifica que `output/` esté en el classpath
3. En IDE: marca `output/` como source folder

### Error: "cannot find symbol: RecetarioParser"

**Causa:** El parser no se generó correctamente.

**Solución:**
1. Verifica que `src/cup/RecetarioParser.cup` existe
2. Ejecuta `make parser` o `make all`
3. Verifica que `output/RecetarioParser.java` se generó

### En IDE: Los imports aparecen en rojo

**Solución:**
1. Ejecuta `make all` primero para generar los archivos
2. Refresca el proyecto (F5 o Reload en la mayoría de IDEs)
3. Rebuild del proyecto
4. Verifica que `output/` esté configurado como source folder
5. Verifica que las librerías JAR estén añadidas

### Error al ejecutar: "ClassNotFoundException"

**Causa:** El classpath no incluye `output/` o las librerías.

**Solución:**
- Ejecuta con: `java -cp "output:libs/java-cup-runtime.jar" main.Main ...`
- Verifica que los archivos `.class` estén en `output/`

## Notas Importantes

- Los archivos en `output/` son generados automáticamente. **No edites estos archivos directamente.**
- Si modificas `RecetarioLexer.flex` o `RecetarioParser.cup`, debes ejecutar `make all` nuevamente.
- Siempre ejecuta `make clean` antes de `make all` si tienes problemas con archivos antiguos.
- El paquete `java_cup.runtime` viene de los JARs en `libs/`, no de archivos fuente.
- El paquete `cup` contiene los archivos generados (lexer, parser, símbolos).
