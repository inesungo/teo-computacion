/* ========= RecetarioLexer.flex ========= */
import java_cup.runtime.Symbol;

%%
%unicode
%cup
%line
%column
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
IDREST  = [A-Za-zÁÉÍÓÚÜÑáéíóúüñ_0-9\-]
ESC     = \\\\.
STRCHAR = [^\\\"\r\n]

%%

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
[.]          { return sym(sym.DOT); }

/* ===== STRINGS ENTRE COMILLAS ===== */
\"({ESC}|{STRCHAR})*\" {
    return sym(sym.STRING, unescapeMin(yytext()));
}

/* ===== CANTIDADES Y UNIDADES ===== */

/* Literal “a gusto” */
"a gusto"                  { return sym(sym.A_GUSTO); }

/* Fracciones como 1/2, 3/4, 5/10 */
{INT}/{INT}                { return sym(sym.FRAC, yytext()); }

/* Decimales: 3.5 o 2,5 (punto o coma) + enteros */
{INT}([.,]{DIG}+)?         {
    if (yytext().contains(".") || yytext().contains(","))
         return sym(sym.DEC, yytext());
    else return sym(sym.INT, Integer.parseInt(yytext()));
}

/* Unidades: g, kg, l, ml, taza(s), cuchara(s), cucharita(s), u */
(g|kg|l|ml|taza(s)?|cuchara(s)?|cucharita(s)?|u)  {
    return sym(sym.UNIT, yytext());
}

/* Números de paso tipo “1.”, “2.”, “10.” */
{INT}[.]                  {
    String num = yytext().substring(0, yytext().length()-1);
    return sym(sym.STEPNUM, Integer.parseInt(num));
}

/* ===== ETIQUETAS Y VALORES ===== */

/* Identificadores simples */
{IDCHAR}{IDREST}?         { return sym(sym.ID, yytext()); }

/* Valores con letras, dígitos y espacios (p.ej. "Muy facil", "Sin gluten") */
({IDCHAR}|{DIG}|[ ])+     { return sym(sym.ID, yytext().trim()); }

/* ===== COMENTARIOS Y FORMATO EXTRA ===== */

/* Comentarios de una línea: # o //  */
"#"[^\r\n]*               { /* ignorar comentario tipo # */ }
"//"[^\r\n]*              { /* ignorar comentario tipo // */ }

/* Comentarios de varias líneas estilo bloque:  /* ... */  */
"/*"([^*]|\*+[^*/])*\*+"/"  { /* ignorar bloque */ }

/* ===== ESPACIOS Y SALTOS ===== */
{ESP}    { /* ignorar espacios */ }
{NL}     { /* ignorar saltos de línea */ }

/* ===== ERROR LÉXICO ===== */
. {
    throw new Error(
      "Carácter inesperado '" + yytext() +
      "' en línea " + (yyline+1) +
      ", columna " + (yycolumn+1)
    );
}
