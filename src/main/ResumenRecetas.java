package main;

import java.util.*;

public final class ResumenRecetas {
    private ResumenRecetas() {}

    public static String generarResumen(Recetario recetario) {
        int total = recetario.cantidadRecetas();

        double sumaCal = 0;
        int conCal = 0;

        Map<String, Integer> porCategoria = new java.util.TreeMap<>();
        Map<String, Integer> porTipo = new java.util.TreeMap<>();
        Map<String, Integer> porDificultad = new java.util.TreeMap<>();

        // Para promedios por clave: acumulamos suma de minutos y cantidad por clave
        Map<String, Integer> sumMinPorTipo = new java.util.HashMap<>();
        Map<String, Integer> cntPorTipo    = new java.util.HashMap<>();
        Map<String, Integer> sumMinPorDif  = new java.util.HashMap<>();
        Map<String, Integer> cntPorDif     = new java.util.HashMap<>();

        int sumaMin = 0, conMin = 0;

        for (Receta r : recetario.getRecetas()) {
            // Promedio calorías global
            if (r.getCalorias() > 0) { sumaCal += r.getCalorias(); conCal++; }

            // Tiempo en minutos
            Integer m = Validaciones.tiempoEnMinutos(r.getTiempo());
            if (m != null && m > 0) { sumaMin += m; conMin++; }

            // Categorías (pueden ser varias)
            for (String c : r.getCategorias()) {
                String k = (c == null ? "" : c.toLowerCase(java.util.Locale.ROOT).trim());
                if (!k.isEmpty()) {
                    porCategoria.put(k, porCategoria.getOrDefault(k, 0) + 1);
                }
            }

            // Tipo (uno solo)
            if (r.getTipo() != null && !r.getTipo().trim().isEmpty()) {
                String t = r.getTipo().trim();
                porTipo.put(t, porTipo.getOrDefault(t, 0) + 1);
                if (m != null && m > 0) {
                    sumMinPorTipo.put(t, sumMinPorTipo.getOrDefault(t, 0) + m);
                    cntPorTipo.put(t,  cntPorTipo.getOrDefault(t,  0) + 1);
                }
            }

            // Dificultad (texto o estrellas)
            if (r.getDificultad() != null && !r.getDificultad().trim().isEmpty()) {
                String d = r.getDificultad().trim();
                porDificultad.put(d, porDificultad.getOrDefault(d, 0) + 1);
                if (m != null && m > 0) {
                    sumMinPorDif.put(d, sumMinPorDif.getOrDefault(d, 0) + m);
                    cntPorDif.put(d,  cntPorDif.getOrDefault(d,  0) + 1);
                }
            }
        }

        double promCal = conCal == 0 ? 0 : sumaCal / conCal;
        double promMin = conMin == 0 ? 0 : (double) sumaMin / conMin;

        StringBuilder sb = new StringBuilder();
        sb.append("=== Resumen de Recetario ===\n");
        sb.append("Total de recetas: ").append(total).append("\n");
        sb.append(String.format(java.util.Locale.ROOT, "Calorías promedio: %.1f Kcal\n", promCal));
        sb.append(String.format(java.util.Locale.ROOT, "Tiempo promedio: %.1f min\n", promMin));

        // Cantidades por categoría
        sb.append("Por categoría:\n");
        if (porCategoria.isEmpty()) {
            sb.append(" (sin categorías)\n");
        } else {
            for (var e : porCategoria.entrySet()) {
                sb.append(" - ").append(e.getKey()).append(": ").append(e.getValue()).append("\n");
            }
        }

        // Cantidades por tipo
        sb.append("Por tipo:\n");
        if (porTipo.isEmpty()) {
            sb.append(" (sin tipo)\n");
        } else {
            for (var e : porTipo.entrySet()) {
                sb.append(" - ").append(e.getKey()).append(": ").append(e.getValue()).append("\n");
            }
        }

        // Cantidades por dificultad
        sb.append("Por dificultad:\n");
        if (porDificultad.isEmpty()) {
            sb.append(" (sin dificultad)\n");
        } else {
            for (var e : porDificultad.entrySet()) {
                sb.append(" - ").append(e.getKey()).append(": ").append(e.getValue()).append("\n");
            }
        }

        // Promedio de tiempo por tipo
        sb.append("Promedio de tiempo por tipo:\n");
        if (sumMinPorTipo.isEmpty()) {
            sb.append(" (sin datos)\n");
        } else {
            // Ordenamos por clave para salida estable
            java.util.List<String> claves = new java.util.ArrayList<>(sumMinPorTipo.keySet());
            java.util.Collections.sort(claves);
            for (String t : claves) {
                int s = sumMinPorTipo.getOrDefault(t, 0);
                int c = cntPorTipo.getOrDefault(t, 0);
                double prom = (c == 0) ? 0 : ((double) s / c);
                sb.append(String.format(java.util.Locale.ROOT, " - %s: %.1f min\n", t, prom));
            }
        }

        // Promedio de tiempo por dificultad
        sb.append("Promedio de tiempo por dificultad:\n");
        if (sumMinPorDif.isEmpty()) {
            sb.append(" (sin datos)\n");
        } else {
            java.util.List<String> claves = new java.util.ArrayList<>(sumMinPorDif.keySet());
            java.util.Collections.sort(claves);
            for (String d : claves) {
                int s = sumMinPorDif.getOrDefault(d, 0);
                int c = cntPorDif.getOrDefault(d, 0);
                double prom = (c == 0) ? 0 : ((double) s / c);
                sb.append(String.format(java.util.Locale.ROOT, " - %s: %.1f min\n", d, prom));
            }
        }

        return sb.toString();
    }
}