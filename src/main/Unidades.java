package main;

import java.util.*;

public final class Unidades {
    private Unidades() {}

    public static Double parseCantidad(String cantidad) {
        if (cantidad == null) return null;
        String c = cantidad.trim().toLowerCase(Locale.ROOT);
        if (c.equals("a gusto")) return null;
        // fracción a/b
        if (c.contains("/")) {
            String[] p = c.split("/");
            if (p.length == 2) {
                try {
                    double a = Double.parseDouble(p[0].trim().replace(",", "."));
                    double b = Double.parseDouble(p[1].trim().replace(",", "."));
                    if (b == 0) return Double.NaN;
                    return a / b;
                } catch (NumberFormatException e) {
                    return Double.NaN;
                }
            }
        }
        // decimal o entero (coma o punto)
        try {
            return Double.parseDouble(c.replace(",", "."));
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    public static boolean esUnidadSoportada(String unidad) {
        if (unidad == null) return true; // permitir sin unidad
        String u = norm(unidad);
        return u.equals("g") || u.equals("kg") ||
               u.equals("ml") || u.equals("l") ||
               u.equals("taza") || u.equals("tazas") ||
               u.equals("cuchara") || u.equals("cucharas") ||
               u.equals("cucharita") || u.equals("cucharitas") ||
               u.equals("u");
    }

    public static String unidadBase(String unidad) {
        if (unidad == null) return null;
        String u = norm(unidad);
        if (u.equals("kg") || u.equals("g")) return "g";
        if (u.equals("l") || u.equals("ml") || u.equals("taza") || u.equals("tazas")
                || u.equals("cuchara") || u.equals("cucharas")
                || u.equals("cucharita") || u.equals("cucharitas")) return "ml";
        if (u.equals("u")) return "u";
        return u;
    }

    public static double aUnidadBase(double cantidad, String unidad) {
        if (Double.isNaN(cantidad)) return Double.NaN;
        if (unidad == null) return cantidad;
        String u = norm(unidad);
        switch (u) {
            // masa → g
            case "g": return cantidad;
            case "kg": return cantidad * 1000.0;

            // volumen → ml
            case "ml": return cantidad;
            case "l": return cantidad * 1000.0;
            case "taza":
            case "tazas": return cantidad * 240.0; // aprox. 240 ml
            case "cuchara":
            case "cucharas": return cantidad * 15.0; // aprox. 15 ml
            case "cucharita":
            case "cucharitas": return cantidad * 5.0; // aprox. 5 ml

            // unidades (piezas)
            case "u": return cantidad;

            default: return cantidad; // desconocida: dejar tal cual
        }
    }

    public static String norm(String unidad) {
        return unidad == null ? null : unidad.trim().toLowerCase(Locale.ROOT);
    }

    public static List<String> validarUnidadesPorIngrediente(Recetario recetario) {
        List<String> errores = new ArrayList<>();
        if (recetario == null || recetario.getRecetas() == null) return errores;

        // Diccionario de unidades permitidas por tipo de ingrediente (normalizado)
        Map<String, Set<String>> mapa = new HashMap<>();

        mapa.put("leche", Set.of("l", "ml", "taza", "tazas", "cuchara", "cucharas", "cucharita", "cucharitas"));
        mapa.put("harina", Set.of("g", "kg", "taza", "tazas"));
        mapa.put("azúcar", Set.of("g", "kg", "taza", "tazas"));
        mapa.put("azucar", Set.of("g", "kg", "taza", "tazas")); // sin tilde
        mapa.put("huevo", Set.of("u"));
        mapa.put("huevos", Set.of("u"));
        mapa.put("manteca", Set.of("g", "kg", "cucharas", "cuchara"));
        mapa.put("aceite", Set.of("ml", "l", "cucharas", "tazas"));
        mapa.put("sal", Set.of("g", "kg", "cucharita", "cucharitas", "a gusto"));
        mapa.put("pimienta", Set.of("g", "cucharita", "a gusto"));
        mapa.put("chocolate", Set.of("g", "taza", "ml"));
        mapa.put("vainilla", Set.of("ml", "cucharita", "cucharitas"));
        mapa.put("chorizo", Set.of("g", "kg", "u"));
        mapa.put("lentejas", Set.of("g", "kg", "taza", "tazas"));
        mapa.put("salsa tomate", Set.of("ml", "l", "taza", "tazas"));
        mapa.put("cebolla", Set.of("g", "kg", "u"));
        mapa.put("morrón", Set.of("u", "g", "kg"));
        mapa.put("papa", Set.of("u", "g", "kg"));
        mapa.put("pollo", Set.of("g", "kg", "u"));

        for (Receta r : recetario.getRecetas()) {
            for (Ingrediente ing : r.getIngredientes()) {
                String nombre = ing.getNombre() == null ? "" : ing.getNombre().toLowerCase(Locale.ROOT).trim();
                String unidad = ing.getUnidad() == null ? "" : ing.getUnidad().toLowerCase(Locale.ROOT).trim();

                // Si es “a gusto”, ignorar (si está en cantidad, no en unidad)
                if ("a gusto".equals(ing.getCantidad())) continue;

                // Si el ingrediente no está en el mapa → advertencia leve
                if (!mapa.containsKey(nombre)) {
                    continue; // opcional: errores.add("No se definieron unidades válidas para '" + nombre + "'");
                }

                // Si la unidad no pertenece al conjunto permitido
                if (!unidad.isEmpty() && !mapa.get(nombre).contains(unidad)) {
                    errores.add("Unidad '" + unidad + "' no válida para '" + nombre + "' en receta '" + r.getNombre() + "'. "
                            + "Permitidas: " + mapa.get(nombre));
                }
            }
        }
        return errores;
    }

}