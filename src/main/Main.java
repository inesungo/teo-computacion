package main;

import java.io.*;
import java.util.*;
import jflex.*;
import cup.*;

/**
 * Clase principal del Analizador de Recetario Digital.
 * PARTE 3 - Interfaz principal y funcionalidades avanzadas
 */
public class Main {
    
    private static List<Receta> recetario = new ArrayList<>();
    
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java Main <archivo_recetario>");
            System.exit(1);
        }
        
        String archivo = args[0];
        
        try {
            // TODO: PARTE 1 y 2 - Crear lexer y parser
            // FileReader reader = new FileReader(archivo);
            // RecetarioLexer lexer = new RecetarioLexer(reader);
            // RecetarioParser parser = new RecetarioParser(lexer);
            // Recetario result = (Recetario) parser.parse().value;
            
            System.out.println("Analizando recetario: " + archivo);
            System.out.println("Recetas procesadas: " + recetario.size());
            
            // PARTE 3 - Menu interactivo
            mostrarMenu();
            
        } catch (FileNotFoundException e) {
            System.err.println("Error: Archivo no encontrado: " + archivo);
        } catch (Exception e) {
            System.err.println("Error procesando archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n=== ANALIZADOR DE RECETARIO DIGITAL ===");
            System.out.println("1. Validar recetas relacionadas (Desafío A)");
            System.out.println("2. Validar unidades de medida (Desafío B)");
            System.out.println("3. Carrito de compras (Desafío C)");
            System.out.println("4. Resumen de recetas (Desafío E)");
            System.out.println("5. Generar menú semanal (Desafío F)");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            
            int opcion = scanner.nextInt();
            
            switch (opcion) {
                case 1:
                    validarRecetasRelacionadas();
                    break;
                case 2:
                    validarUnidadesMedida();
                    break;
                case 3:
                    carritoCompras();
                    break;
                case 4:
                    resumenRecetas();
                    break;
                case 5:
                    menuSemanal();
                    break;
                case 6:
                    System.out.println("¡Hasta pronto!");
                    System.exit(0);
                default:
                    System.out.println("Opción inválida");
            }
        }
    }
    
    // TODO: Implementar métodos de los desafíos (PARTE 3)
    private static void validarRecetasRelacionadas() {
        System.out.println("\n=== Validación de Recetas Relacionadas ===");
        // Desafío A
    }
    
    private static void validarUnidadesMedida() {
        System.out.println("\n=== Validación de Unidades de Medida ===");
        // Desafío B
    }
    
    private static void carritoCompras() {
        System.out.println("\n=== Carrito de Compras ===");
        // Desafío C
    }
    
    private static void resumenRecetas() {
        System.out.println("\n=== Resumen de Recetas ===");
        // Desafío E
    }
    
    private static void menuSemanal() {
        System.out.println("\n=== Generación de Menú Semanal ===");
        // Desafío F
    }
}

