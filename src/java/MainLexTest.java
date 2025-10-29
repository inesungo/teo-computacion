import java.io.*;
import java_cup.runtime.Symbol;

public class MainLexTest {
    public static void main(String[] args) throws Exception {
        String file = (args.length > 0) ? args[0] : "test_receta.txt";

        try (Reader r = new InputStreamReader(new FileInputStream(file), "windows-1252")) {
            RecetarioLexer lx = new RecetarioLexer(r);
            while (true) {
                Symbol tok = lx.next_token();
                if (tok == null || tok.sym == sym.EOF) break;     // <- corte correcto

                String val = (tok.value == null) ? "" : " → " + tok.value;
                System.out.printf("[%3d:%-3d] %-18s%s%n", tok.left, tok.right, tokenName(tok.sym), val);
            }
        }
    }

    private static String tokenName(int s) {
        switch (s) {
            case sym.RECETA_KW: return "RECETA_KW";
            case sym.INGREDIENTES_KW: return "INGREDIENTES_KW";
            case sym.PASOS_KW: return "PASOS_KW";
            case sym.TIEMPO_KW: return "TIEMPO_KW";
            case sym.PORCIONES_KW: return "PORCIONES_KW";
            case sym.CALORIAS_KW: return "CALORIAS_KW";
            case sym.CATEGORIAS_KW: return "CATEGORIAS_KW";
            case sym.ORIGEN_KW: return "ORIGEN_KW";
            case sym.DIFICULTAD_KW: return "DIFICULTAD_KW";
            case sym.TIPO_KW: return "TIPO_KW";
            case sym.RELACIONADAS_KW: return "RELACIONADAS_KW";
            case sym.OBS_KW: return "OBS_KW";
            case sym.COLON: return "COLON";
            case sym.COMMA: return "COMMA";
            case sym.LBRACK: return "LBRACK";
            case sym.RBRACK: return "RBRACK";
            case sym.EQ: return "EQ";
            case sym.DOT: return "DOT";
            case sym.STRING: return "STRING";
            case sym.ID: return "ID";
            case sym.INT: return "INT";
            case sym.DEC: return "DEC";
            case sym.FRAC: return "FRAC";
            case sym.UNIT: return "UNIT";
            case sym.STEPNUM: return "STEPNUM";
            case sym.A_GUSTO: return "A_GUSTO";
            default: return "UNKNOWN";
        }
    }
}
