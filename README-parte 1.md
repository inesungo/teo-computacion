| Token             | Patrón                          | Ejemplo                                | Valor devuelto |
| ----------------- | ------------------------------- | -------------------------------------- | -------------- |
| `RECETA_KW`       | `"RECETA"`                      | `RECETA`                               | —              |
| `INGREDIENTES_KW` | `"INGREDIENTES"`                | `INGREDIENTES`                         | —              |
| `PASOS_KW`        | `"PASOS"`                       | `PASOS`                                | —              |
| `TIEMPO_KW`       | `"TIEMPO"`                      | `TIEMPO=45`                            | —              |
| `PORCIONES_KW`    | `"PORCIONES"`                   | `PORCIONES=8`                          | —              |
| `CALORIAS_KW`     | `"CALORIAS"` o `"CALORÍAS"`     | `CALORIAS=320`                         | —              |
| `CATEGORIAS_KW`   | `"CATEGORIAS"` o `"CATEGORÍAS"` | `CATEGORIAS: [Merienda, Postre]`       | —              |
| `ORIGEN_KW`       | `"ORIGEN"`                      | `ORIGEN=Uruguay`                       | —              |
| `DIFICULTAD_KW`   | `"DIFICULTAD"`                  | `DIFICULTAD=Media`                     | —              |
| `TIPO_KW`         | `"TIPO"`                        | `TIPO=Postre`                          | —              |
| `RELACIONADAS_KW` | `"RECETAS RELACIONADAS"`        | `RECETAS RELACIONADAS:`                | —              |
| `OBS_KW`          | `"OBS"`                         | `OBS: Ideal para acompañar con helado` | —              |
| `COLON`           | `:`                             | `CATEGORIAS:`                          | —              |
| `COMMA`           | `,`                             | `[A, B]`                               | —              |
| `LBRACK`          | `[`                             | `[A, B]`                               | —              |
| `RBRACK`          | `]`                             | `[A, B]`                               | —              |
| `EQ`              | `=`                             | `TIPO=Postre`                          | —              |
| `DOT`             | `.`                             | `1.`                                   | —              |
| `STRING`          | Texto entre comillas            | `"Brownies de Chocolate"`              | String         |
| `ID`              | Palabra sin comillas            | `harina`, `sin_gluten`                 | String         |
| `INT`             | Entero                          | `500`                                  | Integer        |
| `DEC`             | Decimal con `.` o `,`           | `2.5`, `3,5`                           | String         |
| `FRAC`            | Fracción                        | `1/2`                                  | String         |
| `UNIT`            | Unidad                          | `g`, `kg`, `taza`, `cucharita`, `u`    | String         |
| `STEPNUM`         | Número con punto                | `1.`                                   | Integer        |
| `A_GUSTO`         | `"a gusto"`                     | `sal a gusto`                          | —              |
| `EOF`             | Fin de archivo                  | —                                      | —              |

Características adicionales

%ignorecase permite que las palabras clave sean insensibles a mayúsculas y minúsculas.

%line y %column mantienen el conteo para mensajes de error.

Se manejan caracteres acentuados (ÁÉÍÓÚÜÑ y minúsculas).

Regla final . captura cualquier carácter inesperado e informa línea y columna.

Pruebas realizadas

Se probó el lexer con el archivo de ejemplo test_receta.txt, obteniendo el reconocimiento correcto de todos los tokens.

También se incluye un tester general MainLexTest.java que imprime los tokens con posición:

java -cp ".;G:\ObligatorioTC\jflex-1.9.1\lib\java-cup-11b-runtime.jar" MainLexTest test_receta.txt


Salida esperada (fragmento):

[ 1:1] RECETA_KW
[ 1:8] STRING → Brownies de Chocolate
[ 3:1] TIEMPO_KW
[ 3:9] INT → 45
[ 5:1] INGREDIENTES_KW
...

Comandos de uso
Generar el lexer desde .flex
java -jar "G:\ObligatorioTC\jflex-1.9.1\lib\jflex-full-1.9.1.jar" "G:\ObligatorioTC\jflex-1.9.1\examples\cup-interpreter\src\main\jflex\RecetarioLexer.flex"

Compilar
javac -cp ".;G:\ObligatorioTC\jflex-1.9.1\lib\java-cup-11b-runtime.jar" RecetarioLexer.java MainLexTest.java sym.java

Ejecutar prueba
chcp 65001
java -cp ".;G:\ObligatorioTC\jflex-1.9.1\lib\java-cup-11b-runtime.jar" MainLexTest test_receta.txt

Estructura de carpetas
src/
 ├─ jflex/
 │   └─ RecetarioLexer.flex
 └─ main/java/
     ├─ RecetarioLexer.java
     ├─ MainLexTest.java
     ├─ sym.java
     └─ test_receta.txt

Estado final

 Analizador léxico completo y probado

 Tokens definidos según letra

 Pruebas funcionales realizadas

 Documentación de tokens incluida

 Listo para integración con CUP
