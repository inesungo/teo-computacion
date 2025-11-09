package main;

import java.util.*;

public final class ResumenRecetas {
    private ResumenRecetas() {}

    public static String generarResumen(Recetario recetario) {
        int total = recetario.cantidadRecetas();
        double sumaCal = 0;
        int conCal = 0;
        Map<String, Integer> porCategoria = new TreeMap<>();
        int sumaMin = 0, conMin = 0;

        for (Receta r : recetario.getRecetas()) {
            if (r.getCalorias() > 0) { sumaCal += r.getCalorias(); conCal++; }
            Integer m = Validaciones.tiempoEnMinutos(r.getTiempo());
            if (m != null && m > 0) { sumaMin += m; conMin++; }
            for (String c : r.getCategorias()) {
                String k = c == null ? "" : c.toLowerCase(Locale.ROOT);
                porCategoria.put(k, porCategoria.getOrDefault(k, 0) + 1);
            }
        }

        double promCal = conCal == 0 ? 0 : sumaCal / conCal;
        double promMin = conMin == 0 ? 0 : (double)sumaMin / conMin;

        StringBuilder sb = new StringBuilder();
        sb.append("=== Resumen de Recetario ===\n");
        sb.append("Total de recetas: ").append(total).append("\n");
        sb.append(String.format(Locale.ROOT, "Calorías promedio: %.1f Kcal\n", promCal));
        sb.append(String.format(Locale.ROOT, "Tiempo promedio: %.1f min\n", promMin));
        sb.append("Por categoría:\n");
        for (Map.Entry<String,Integer> e : porCategoria.entrySet()) {
            sb.append(" - ").append(e.getKey()).append(": ").append(e.getValue()).append("\n");
        }
        return sb.toString();
    }
}