// java
package main;

import java.util.*;

public final class MenuSemanal {
    private MenuSemanal() {}

    public static final class MenuDia {
        public Receta desayuno, almuerzo, merienda, cena;
        public int totalCalorias() {
            int s = 0;
            if (desayuno != null) s += Math.max(0, desayuno.getCalorias());
            if (almuerzo  != null) s += Math.max(0, almuerzo.getCalorias());
            if (merienda  != null) s += Math.max(0, merienda.getCalorias());
            if (cena      != null) s += Math.max(0, cena.getCalorias());
            return s;
        }
    }

    public static List<MenuDia> generarPorCategorias(Recetario recetario, int dias, int topeCaloriasDia, String tipoFiltro) {
        List<Receta> todas = new ArrayList<>(recetario.getRecetas());
        if (tipoFiltro != null && !tipoFiltro.trim().isEmpty()) {
            todas = filtrarPorTipo(todas, tipoFiltro);
        }

        List<Receta> desayunos   = filtrarPorCategoria(todas, "desayuno");
        List<Receta> meriendas   = filtrarPorCategoria(todas, "merienda");
        List<Receta> principales = filtrarPorCategoria(todas, "principal");

        if (desayunos.isEmpty())   desayunos.addAll(todas);
        if (meriendas.isEmpty())   meriendas.addAll(todas);
        if (principales.isEmpty()) principales.addAll(todas);

        Comparator<Receta> porCal = Comparator.comparingInt(Receta::getCalorias);
        desayunos.sort(porCal);
        meriendas.sort(porCal);
        principales.sort(porCal);

        List<MenuDia> semana = new ArrayList<>(dias);

        // indices y pasos diferentes para cada pool -> evita repetir exacto conjunto cada día
        int idxD = 0, idxA = 0, idxM = 0, idxC = 0;
        final int stepD = 1, stepA = 2, stepM = 3, stepC = 5;

        for (int d = 0; d < dias; d++) {
            int restante = (topeCaloriasDia <= 0) ? Integer.MAX_VALUE : Math.max(0, topeCaloriasDia);
            MenuDia dia = new MenuDia();
            Set<Receta> usados = new HashSet<>();

            dia.desayuno = pickGreedy(desayunos, restante, usados, idxD);
            restante -= cal(dia.desayuno);
            if (dia.desayuno != null) usados.add(dia.desayuno);

            dia.almuerzo = pickGreedy(principales, restante, usados, idxA);
            restante -= cal(dia.almuerzo);
            if (dia.almuerzo != null) usados.add(dia.almuerzo);

            dia.merienda = pickGreedy(meriendas, restante, usados, idxM);
            restante -= cal(dia.merienda);
            if (dia.merienda != null) usados.add(dia.merienda);

            dia.cena = pickGreedy(principales, restante, usados, idxC);
            if (dia.cena != null) usados.add(dia.cena);

            semana.add(dia);

            // avanzar índices con distinto paso para variar
            idxD = (idxD + stepD) % Math.max(1, desayunos.size());
            idxA = (idxA + stepA) % Math.max(1, principales.size());
            idxM = (idxM + stepM) % Math.max(1, meriendas.size());
            idxC = (idxC + stepC) % Math.max(1, principales.size());
        }

        return semana;
    }

    public static String imprimirPorCategorias(List<MenuDia> semana, int topeCaloriasDia) {
        String[] dias = {"Lunes","Martes","Miércoles","Jueves","Viernes","Sábado","Domingo"};
        StringBuilder sb = new StringBuilder();
        sb.append("=== Menú semanal ===\n");
        if (topeCaloriasDia > 0) sb.append("Tope diario: ").append(topeCaloriasDia).append(" Kcal\n");

        for (int i = 0; i < semana.size(); i++) {
            String nombreDia = dias[i % dias.length];
            MenuDia md = semana.get(i);
            int total = md.totalCalorias();
            sb.append(nombreDia).append(" = {")
              .append(md.desayuno == null ? "-" : md.desayuno.getNombre()).append(", ")
              .append(md.almuerzo == null ? "-" : md.almuerzo.getNombre()).append(", ")
              .append(md.merienda == null ? "-" : md.merienda.getNombre()).append(", ")
              .append(md.cena == null ? "-" : md.cena.getNombre()).append("}")
              .append("  [Total: ").append(total).append(" Kcal");
            if (topeCaloriasDia > 0 && total > topeCaloriasDia) {
                sb.append("  excede por ").append(total - topeCaloriasDia);
            }
            sb.append("]\n");
        }
        return sb.toString();
    }

    // Helpers

    private static List<Receta> filtrarPorCategoria(List<Receta> src, String cat) {
        String needle = cat.toLowerCase(Locale.ROOT);
        List<Receta> out = new ArrayList<>();
        for (Receta r : src) {
            if (r.getCategorias() == null) continue;
            for (String c : r.getCategorias()) {
                if (c != null && c.toLowerCase(Locale.ROOT).contains(needle)) {
                    out.add(r);
                    break;
                }
            }
        }
        return out;
    }

    private static List<Receta> filtrarPorTipo(List<Receta> src, String tipo) {
        String needle = tipo.toLowerCase(Locale.ROOT);
        List<Receta> out = new ArrayList<>();
        for (Receta r : src) {
            String t = r.getTipo();
            if (t != null && t.toLowerCase(Locale.ROOT).contains(needle)) out.add(r);
        }
        return out;
    }

    private static int cal(Receta r) { return r == null ? 0 : Math.max(0, r.getCalorias()); }

    private static Receta pickGreedy(List<Receta> pool, int restante, Set<Receta> usados, int startIdx) {
        int n = pool.size();
        if (n == 0) return null;

        // 1) buscar desde startIdx una ≤ restante y no usada
        for (int k = 0; k < n; k++) {
            Receta r = pool.get((startIdx + k) % n);
            if (usados.contains(r)) continue;
            if (cal(r) <= restante) return r;
        }

        // 2) si no hay <= restante, devolver la de menor caloría no usada
        Receta min = null;
        for (Receta r : pool) {
            if (usados.contains(r)) continue;
            if (min == null || cal(r) < cal(min)) min = r;
        }
        if (min != null) return min;

        // 3) todas usadas -> permitir repetir la de menor caloría absoluta
        Receta absolutoMin = pool.get(0);
        for (Receta r : pool) if (cal(r) < cal(absolutoMin)) absolutoMin = r;
        return absolutoMin;
    }
}