package main;

import java.io.*;
import java.util.*;

import cup.*;
public class Main {

    private static final Scanner SC = new Scanner(System.in);

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java -cp output main.Main <archivo_recetario>");
            System.exit(1);
        }
        String archivo = args[0];

        try (InputStream fis = new FileInputStream(archivo);
             InputStreamReader reader = new InputStreamReader(fis, "UTF-8")) {

            System.out.println("Analizando recetario: " + archivo);

            RecetarioLexer lexer = new RecetarioLexer(reader);
            RecetarioParser parser = new RecetarioParser(lexer);
            java_cup.runtime.Symbol resultado = parser.parse();
            Recetario recetario = (Recetario) resultado.value;

            if (recetario == null) {
                System.err.println("Error: No se pudo parsear el recetario");
                System.exit(1);
            }

            System.out.println("Recetario parseado correctamente");
            System.out.println("Recetas procesadas: " + recetario.cantidadRecetas());

            menu(recetario);

        } catch (FileNotFoundException e) {
            System.err.println("Error: Archivo no encontrado: " + archivo);
        } catch (Exception e) {
            System.err.println("Error al parsear el archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void menu(Recetario recetario) {
        while (true) {
            System.out.println("\n=== MENÚ ===");
            System.out.println("1) Resumen del recetario");
            System.out.println("2) Validar recetas relacionadas");
            System.out.println("3) Validar unidades de ingredientes");
            System.out.println("4) Generar carrito de compras");
            System.out.println("5) Generar menu semanal");
            System.out.println("6) Validar tiempos");
            System.out.println("0) Salir");
            System.out.print("Opción: ");

            String op = SC.nextLine().trim();
            switch (op) {
                case "1":
                    System.out.println(ResumenRecetas.generarResumen(recetario));
                    break;

                case "2": {
                    List<String> errores = Validaciones.validarRecetasRelacionadasAgrupado(recetario);
                    if (errores.isEmpty()) {
                        System.out.println("OK: Todas las referencias existen y no hay auto-referencias.");
                    } else {
                        System.out.println("Validación de relacionadas:");
                        errores.forEach(s -> System.out.println(" - " + s));
                    }
                    break;
                }

                case "3": {
                    List<String> mensajes = Validaciones.validarUnidadesPorIngrediente(recetario);
                    if (mensajes.isEmpty()) {
                        System.out.println("OK: Todas las unidades son coherentes con los ingredientes.");
                    } else {
                        System.out.println("Resultados de validación de unidades:");
                        mensajes.forEach(s -> System.out.println(" - " + s));
                    }
                    break;
                }

                case "4": {
                    System.out.println("Recetas disponibles:");
                    for (Receta r : recetario.getRecetas()) System.out.println(" - " + r.getNombre());
                    System.out.println("Ingrese nombres de recetas separados por coma:");
                    String linea = SC.nextLine();
                    int personas = leerEntero("¿Para cuantas personas? ");
                    List<Receta> elegidas = seleccionarRecetas(recetario, linea);
                    Map<String, ItemCarrito> mapa = CarritoCompras.consolidarConPersonas(elegidas, personas);
                    System.out.println(CarritoCompras.imprimir(mapa));
                    break;
                }


                case "5": {
                    int dias = leerEntero("Cantidad de días (1..7 recomendado): ");
                    int maxCal = leerEntero("Calorías máximas por día (0 = sin tope): ");
                    System.out.print("Filtro de tipo (ej. light) [Enter = ninguno]: ");
                    String filtro = SC.nextLine().trim();
                    if (filtro.isEmpty()) filtro = null;

                    List<MenuSemanal.MenuDia> menu = MenuSemanal.generarPorCategorias(recetario, dias, maxCal, filtro);
                    System.out.println(MenuSemanal.imprimirPorCategorias(menu, maxCal));
                    break;
                }


                case "6": {
                    List<String> adv = Validaciones.validarTiempos(recetario);
                    if (adv.isEmpty()) {
                        System.out.println("OK: Todos los tiempos fueron reconocidos.");
                    } else {
                        System.out.println("Advertencias de tiempos:");
                        adv.forEach(s -> System.out.println(" - " + s));
                    }
                    break;
                }


                case "0":
                    return;

                default:
                    System.out.println("Opción inválida.");
            }
        }
    }


    private static int leerEntero(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = SC.nextLine().trim();
            if (s.matches("-?\\d+")) return Integer.parseInt(s);
            System.out.println("Ingrese un número entero válido.");
        }
    }

    private static List<Receta> seleccionarRecetas(Recetario recetario, String linea) {
        List<Receta> out = new ArrayList<>();
        if (linea == null || linea.trim().isEmpty()) return out;

        String[] partes = linea.split(",");
        for (String p : partes) {
            String q = p.trim();
            if (q.isEmpty()) continue;

            Receta match = null;

            // 1) igualdad case-insensitive
            for (Receta r : recetario.getRecetas()) {
                if (r.getNombre() != null && r.getNombre().equalsIgnoreCase(q)) {
                    match = r; break;
                }
            }
            // 2) empieza con (case-insensitive)
            if (match == null) {
                for (Receta r : recetario.getRecetas()) {
                    if (r.getNombre() != null && r.getNombre().toLowerCase().startsWith(q.toLowerCase())) {
                        match = r; break;
                    }
                }
            }
            // 3) contiene (case-insensitive)
            if (match == null) {
                for (Receta r : recetario.getRecetas()) {
                    if (r.getNombre() != null && r.getNombre().toLowerCase().contains(q.toLowerCase())) {
                        match = r; break;
                    }
                }
            }

            if (match == null) {
                System.out.println("! Aviso: receta no encontrada: \"" + q + "\" (omitida)");
            } else {
                out.add(match);
            }
        }
        return out;
    }

}

