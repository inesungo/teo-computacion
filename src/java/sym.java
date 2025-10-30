// sym.java — STUB temporal para probar el lexer sin CUP
public final class sym {
    // EOF que usa el lexer al finalizar
    public static final int EOF = 0;

    // Palabras clave
    public static final int RECETA_KW = 1;
    public static final int INGREDIENTES_KW = 2;
    public static final int PASOS_KW = 3;
    public static final int TIEMPO_KW = 4;
    public static final int PORCIONES_KW = 5;
    public static final int CALORIAS_KW = 6;
    public static final int CATEGORIAS_KW = 7;
    public static final int ORIGEN_KW = 8;
    public static final int DIFICULTAD_KW = 9;
    public static final int TIPO_KW = 10;
    public static final int RELACIONADAS_KW = 11;
    public static final int OBS_KW = 12;

    // Puntuación / símbolos
    public static final int COLON = 20;
    public static final int COMMA = 21;
    public static final int LBRACK = 22;
    public static final int RBRACK = 23;
    public static final int EQ = 24;
    public static final int DOT = 25;

    // Literales
    public static final int STRING = 30;
    public static final int ID = 31;
    public static final int INT = 32;
    public static final int DEC = 33;
    public static final int FRAC = 34;
    public static final int UNIT = 35;
    public static final int STEPNUM = 36;
    public static final int A_GUSTO = 37;
}
