/* ========= RecetarioLexer.flex ========= */
/* Analizador Léxico para Recetario Digital - PARTE 1 + mejoras PARTE 2 */

package cup;

import java_cup.runtime.Symbol;

%%
%unicode
%cup
%line
%column
%public
%class RecetarioLexer
%ignorecase

%{
  /* ===== Helpers ===== */
  private Symbol sym(int type) {
    return new Symbol(type, yyline+1, yycolumn+1);
  }
  private Symbol sym(int type, Object value) {
    return new Symbol(type, yyline+1, yycolumn+1, value);
  }

  // Unescape mínimo para strings: \" y \\ → " y \
  private String unescapeMin(String rawWithQuotes) {
    String inner = rawWithQuotes.substring(1, rawWithQuotes.length() - 1);
    inner = inner.replace("\\\\", "\\");
    inner = inner.replace("\\\"", "\"");
    return inner;
  }
%}

/* ==== Macros generales ==== */
ESP     = [ \t\f]+
NL      = \r\n|\r|\n
DIG     = [0-9]
INT     = 0|[1-9][0-9]*
IDCHAR  = [A-Za-zÁÉÍÓÚÜÑáéíóúüñ_]
IDREST  = [A-Za-zÁÉÍÓÚÜÑáéíóúüñ_\-]
ESC     = \\\\.
STRCHAR = [^\\\"\r\n]

%%

/* ===== COMENTARIOS (deben ir primero para ignorarlos) ===== */
"#"[^\r\n]*               { /* ignorar comentario tipo # */ }
"//"[^\r\n]*              { /* ignorar comentario tipo // */ }
"/*"([^*]|\*+[^*/])*\*+"/"  { /* ignorar bloque */ }

/* ===== ESPACIOS Y SALTOS (DEBEN ir DESPUÉS de comentarios pero ANTES de todo lo demás) ===== */
{ESP}    { /* ignorar espacios */ }
{NL}     { /* ignorar saltos de línea */ }

/* ===== PALABRAS CLAVE (secciones) ===== */
"RECETA"                    { return sym(sym.RECETA_KW); }
"INGREDIENTES"              { return sym(sym.INGREDIENTES_KW); }
"PASOS"                     { return sym(sym.PASOS_KW); }

"TIEMPO"                    { return sym(sym.TIEMPO_KW); }
"PORCIONES"                 { return sym(sym.PORCIONES_KW); }
"CALORÍAS" | "CALORIAS"     { return sym(sym.CALORIAS_KW); }
"CATEGORÍAS" | "CATEGORIAS" { return sym(sym.CATEGORIAS_KW); }

"ORIGEN"                    { return sym(sym.ORIGEN_KW); }
"DIFICULTAD"                { return sym(sym.DIFICULTAD_KW); }
"TIPO"                      { return sym(sym.TIPO_KW); }

"RECETAS" [ ]+ "RELACIONADAS" { return sym(sym.RELACIONADAS_KW); }
"OBS"                         { return sym(sym.OBS_KW); }

/* ===== PUNTUACIÓN / SÍMBOLOS ===== */
":"          { return sym(sym.COLON); }
","          { return sym(sym.COMMA); }
"["          { return sym(sym.LBRACK); }
"]"          { return sym(sym.RBRACK); }
"="          { return sym(sym.EQ); }

/* ===== STRINGS ENTRE COMILLAS ===== */
\"({ESC}|{STRCHAR})*\" {
    return sym(sym.STRING, unescapeMin(yytext()));
}

/* ===== CANTIDADES Y UNIDADES (orden CRÍTICO: más específicos primero) ===== */

/* Literal "a gusto" */
"a gusto"                  { return sym(sym.A_GUSTO); }

/* Tres puntos seguidos "..." */
"..."                     { return sym(sym.TRES_PUNTOS); }

/* Estrellas para dificultad (1 a 5 estrellas) */
\*{1,5}                   { return sym(sym.STARS, yytext()); }

/* Números de paso tipo "1.", "2.", "10." - DEBE ir ANTES de fracciones */
{INT}[.]                  {
    String num = yytext().substring(0, yytext().length()-1);
    return sym(sym.STEPNUM, Integer.parseInt(num));
}

/* Fracciones como 1/2, 3/4, 5/10 - DEBE ir ANTES de enteros y decimales */
{INT}"/"{INT}                { return sym(sym.FRAC, yytext()); }

/* Decimales: 3.5 o 2,5 (punto o coma) */
{INT}[.,]{DIG}+         { return sym(sym.DEC, yytext()); }

/* Enteros - debe ir DESPUÉS de decimales y fracciones */
{INT}                      { return sym(sym.INT, Integer.parseInt(yytext())); }

/* Unidades: g, kg, l, ml, taza(s), cuchara(s), cucharita(s), u, Kcal/kcal */
(g|kg|l|ml|taza(s)?|cuchara(s)?|cucharita(s)?|u|Kcal|kcal)  {
    return sym(sym.UNIT, yytext());
}

/* Punto final - debe ir después de STEPNUM y DEC */
[.]          { return sym(sym.DOT); }

/* ===== ETIQUETAS Y VALORES ===== */

/* Identificadores simples - solo letras y caracteres permitidos, SIN espacios */
/* NOTA: IDREST incluye dígitos, pero el ID debe detenerse en espacios porque los espacios */
/* se procesan ANTES en el orden de reglas */
{IDCHAR}{IDREST}*         { return sym(sym.ID, yytext()); }

/* ===== ERROR LÉXICO ===== */
. {
    throw new Error(
      "Carácter inesperado '" + yytext() +
      "' en línea " + (yyline+1) +
      ", columna " + (yycolumn+1)
    );
}
