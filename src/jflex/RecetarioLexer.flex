// Analizador Léxico para Recetario Digital
// PARTE 1 - Integrante: [NOMBRE]

package jflex;

%%
%class RecetarioLexer
%public
%unicode
%line
%column
%cup

%{
    // Código Java que se incluirá en la clase generada
    private StringBuilder string = new StringBuilder();
    
    // Método para obtener el texto actual
    public String getText() {
        return yytext();
    }
%}

// Definiciones regulares

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace = [ \t\f]

// Comentarios (si se desea soportar)
Comment = {TraditionalComment} | {EndOfLineComment}
TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment = "//" {InputCharacter}*

// Palabras clave
PALABRA_RECETA = "RECETA"
PALABRA_INGREDIENTES = "INGREDIENTES"
PALABRA_PASOS = "PASOS"
PALABRA_TIEMPO = "Tiempo"
PALABRA_PORCIONES = "Porciones"
PALABRA_CALORIAS = "Calorías"
PALABRA_CATEGORIAS = "Categorías"
PALABRA_ORIGEN = "Origen"
PALABRA_DIFICULTAD = "Dificultad"
PALABRA_TIPO = "Tipo"
PALABRA_RECETAS_RELACIONADAS = "Recetas relacionadas"
PALABRA_OBS = "Obs"

// Unidades de medida
UNIDAD_PESO = "g" | "kg" | "mg"
UNIDAD_VOLUMEN = "l" | "ml" | "cm3" | "taza" | "tazas" | "litro" | "litros"
UNIDAD_CUCHARA = "cuchara" | "cucharas" | "cucharita" | "cucharitas"
UNIDAD_UNIDAD = "u" | "unidad" | "unidades"
UNIDAD_TIEMPO = "min" | "minutos" | "h" | "horas" | "hora"

// Números
DIGITO = [0-9]
NUMERO_ENTERO = {DIGITO}+
NUMERO_DECIMAL = {NUMERO_ENTERO}","{NUMERO_ENTERO} | {NUMERO_ENTERO}"."{NUMERO_ENTERO}
FRACCION = {NUMERO_ENTERO}"/"{NUMERO_ENTERO} | {NUMERO_ENTERO}" "{NUMERO_ENTERO}"/"{NUMERO_ENTERO}

// Categorías
CATEGORIA = "Desayuno" | "Merienda" | "Principal" | "Entrada" | "Colación" | "Postre" | "Almuerzo" | "Cena"

// Nivel de dificultad (ejemplos - ajustar según diseño)
DIFICULTAD = "BAJA" | "MEDIA" | "ALTA" | "MUY_ALTA" | [*]{1,5} | {NUMERO_ENTERO}

// Identificadores y texto
LETRA = [a-zA-ZÁÉÍÓÚáéíóúñÑ]
IDENTIFICADOR = {LETRA}({LETRA}|{DIGITO}|"_"|"-")*
TEXTO_LIBRE = {IDENTIFICADOR}({WhiteSpace}+{IDENTIFICADOR})*

// String entre comillas
STRING_SIMPLE = \"[^\"]*\"
STRING_DOBLE = \"\"[^\"]*\"\"

// Símbolos especiales
DOS_PUNTOS = ":"
PUNTO = "."
COMA = ","
CORCHETE_ABIERTO = "["
CORCHETE_CERRADO = "]"
IGUAL = "="
COMILLA_SIMPLE = "'"

// Calorías
CALORIA = "Kcal" | "kcal" | "calorías" | "Calorías"

%%

// Reglas léxicas

{Comment}           { /* Ignorar comentarios */ }

{WhiteSpace}        { /* Ignorar espacios en blanco */ }

{LineTerminator}    { /* Ignorar saltos de línea */ }

// Palabras clave
{PALABRA_RECETA}                { return symbol(sym.RECETA); }
{PALABRA_INGREDIENTES}         { return symbol(sym.INGREDIENTES); }
{PALABRA_PASOS}                 { return symbol(sym.PASOS); }
{PALABRA_TIEMPO}                { return symbol(sym.TIEMPO); }
{PALABRA_PORCIONES}             { return symbol(sym.PORCIONES); }
{PALABRA_CALORIAS}              { return symbol(sym.CALORIAS); }
{PALABRA_CATEGORIAS}            { return symbol(sym.CATEGORIAS); }
{PALABRA_ORIGEN}                { return symbol(sym.ORIGEN); }
{PALABRA_DIFICULTAD}            { return symbol(sym.DIFICULTAD); }
{PALABRA_TIPO}                  { return symbol(sym.TIPO); }
{PALABRA_RECETAS_RELACIONADAS}  { return symbol(sym.RECETAS_RELACIONADAS); }
{PALABRA_OBS}                   { return symbol(sym.OBS); }

// Números
{NUMERO_ENTERO}     { return symbol(sym.NUMERO, Integer.parseInt(yytext())); }
{NUMERO_DECIMAL}    { return symbol(sym.NUMERO_DECIMAL, yytext()); }
{FRACCION}          { return symbol(sym.FRACCION, yytext()); }

// Unidades
{UNIDAD_PESO}       { return symbol(sym.UNIDAD_PESO, yytext()); }
{UNIDAD_VOLUMEN}    { return symbol(sym.UNIDAD_VOLUMEN, yytext()); }
{UNIDAD_CUCHARA}    { return symbol(sym.UNIDAD_CUCHARA, yytext()); }
{UNIDAD_UNIDAD}     { return symbol(sym.UNIDAD_UNIDAD, yytext()); }
{UNIDAD_TIEMPO}     { return symbol(sym.UNIDAD_TIEMPO, yytext()); }

// Símbolos
{DOS_PUNTOS}        { return symbol(sym.DOS_PUNTOS); }
{PUNTO}             { return symbol(sym.PUNTO); }
{COMA}              { return symbol(sym.COMA); }
{CORCHETE_ABIERTO}  { return symbol(sym.CORCHETE_ABIERTO); }
{CORCHETE_CERRADO}  { return symbol(sym.CORCHETE_CERRADO); }
{IGUAL}             { return symbol(sym.IGUAL); }

// Strings
{STRING_SIMPLE}     { String text = yytext(); 
                      return symbol(sym.STRING, text.substring(1, text.length()-1)); }
{STRING_DOBLE}      { String text = yytext(); 
                      return symbol(sym.STRING, text.substring(2, text.length()-1)); }

// Identificadores (para nombres de ingredientes, países, tipos, etc.)
{IDENTIFICADOR}     { return symbol(sym.IDENTIFICADOR, yytext()); }

// Calorías
{CALORIA}           { return symbol(sym.CALORIA, yytext()); }

// Categorías
{CATEGORIA}         { return symbol(sym.CATEGORIA, yytext()); }

// Caracter no reconocido
.                   { System.err.println("Caracter no reconocido: '" + yytext() + "' en línea " + yyline + ", columna " + yycolumn); 
                      return symbol(sym.error); }

