package main;

import java.util.*;

public final class MenuSemanal {
    private MenuSemanal() {}

    public static List<Receta> generar(Recetario recetario, int dias, int caloriasMaxDia) {
        List<Receta> candidatos = new ArrayList<>(recetario.getRecetas());
        // Heurística simple: ordenar por calorías asc.
        candidatos.sort(Comparator.comparingInt(Receta::getCalorias));
        List<Receta> menu = new ArrayList<>();
        for (Receta r : candidatos) {
            if (menu.size() >= dias) break;
            if (caloriasMaxDia <= 0 || r.getCalorias() <= caloriasMaxDia) {
                menu.add(r);
            }
        }
        // Si no alcanzó, completar sin restricción
        for (Receta r : candidatos) {
            if (menu.size() >= dias) break;
            if (!menu.contains(r)) menu.add(r);
        }
        return menu;
    }

    public static String imprimir(List<Receta> menu) {
        StringBuilder sb = new StringBuilder("=== Menú semanal ===\n");
        for (int i = 0; i < menu.size(); i++) {
            Receta r = menu.get(i);
            sb.append("Día ").append(i+1).append(": ").append(r.getNombre())
              .append(" (").append(r.getCalorias()).append(" Kcal, ").append(r.getTiempo()).append(")\n");
        }
        return sb.toString();
    }
}