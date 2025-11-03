package main;

import java.io.*;
import java_cup.runtime.*;
import cup.*;

/**
 * Clase principal del Analizador de Recetario Digital.
 * PARTE 1 y 2 - Analizador Léxico y Sintáctico
 */
public class Main {
    
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java Main <archivo_recetario>");
            System.exit(1);
        }
        
        String archivo = args[0];
        
        try {
            // PARTE 1 y 2 - Crear lexer y parser
            System.out.println("Analizando recetario: " + archivo);
            
            FileInputStream fis = new FileInputStream(archivo);
            InputStreamReader reader = new InputStreamReader(fis, "UTF-8");
            RecetarioLexer lexer = new RecetarioLexer(reader);
            RecetarioParser parser = new RecetarioParser(lexer);
            
            java_cup.runtime.Symbol resultado = parser.parse();
            Recetario recetario = (Recetario) resultado.value;
            
            if (recetario == null) {
                System.err.println("Error: No se pudo parsear el recetario");
                System.exit(1);
            }
            
            System.out.println("✓ Recetario parseado correctamente");
            System.out.println("Recetas procesadas: " + recetario.cantidadRecetas());
            System.out.println("\n" + recetario.toString());
            
        } catch (FileNotFoundException e) {
            System.err.println("Error: Archivo no encontrado: " + archivo);
        } catch (Exception e) {
            System.err.println("Error al parsear el archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

