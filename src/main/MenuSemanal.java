package main;

import java.util.*;

public final class MenuSemanal {
    private MenuSemanal() {}

    // ======= Versión simple que ya tenías (la dejo por compatibilidad) =======
    public static List<Receta> generar(Recetario recetario, int dias, int caloriasMaxDia) {
        List<Receta> candidatos = new ArrayList<>(recetario.getRecetas());
        candidatos.sort(Comparator.comparingInt(Receta::getCalorias));
        List<Receta> menu = new ArrayList<>();
        for (Receta r : candidatos) {
            if (menu.size() >= dias) break;
            if (caloriasMaxDia <= 0 || r.getCalorias() <= caloriasMaxDia) {
                menu.add(r);
            }
        }
        for (Receta r : candidatos) {
            if (menu.size() >= dias) break;
            if (!menu.contains(r)) menu.add(r);
        }
        return menu;
    }

    public static String imprimir(List<Receta> menu) {
        StringBuilder sb = new StringBuilder("=== Menú semanal (simple) ===\n");
        for (int i = 0; i < menu.size(); i++) {
            Receta r = menu.get(i);
            sb.append("Día ").append(i+1).append(": ").append(r.getNombre())
                    .append(" (").append(r.getCalorias()).append(" Kcal, ").append(r.getTiempo()).append(")\n");
        }
        return sb.toString();
    }

    // ======= Parte F: menú randómico con 4 comidas por día y tope de calorías =======
    public enum Comida { DESAYUNO, ALMUERZO, MERIENDA, CENA }

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
        public boolean completo() {
            return desayuno != null && almuerzo != null && merienda != null && cena != null;
        }
        public List<Receta> recetas() {
            List<Receta> l = new ArrayList<>(4);
            if (desayuno != null) l.add(desayuno);
            if (almuerzo  != null) l.add(almuerzo);
            if (merienda  != null) l.add(merienda);
            if (cena      != null) l.add(cena);
            return l;
        }
    }

    /** Genera N días, cada uno con 4 recetas (1 por comida), randómico y ≤ calorías/día si es posible.
     *  @param filtroTipo opcional (case-insensitive). Si es null/"" no filtra por tipo. */
    public static List<MenuDia> generarAvanzado(Recetario recetario, int dias, int caloriasMaxDia, String filtroTipo) {
        List<Receta> candidatos = filtrarCandidatos(recetario, filtroTipo);
        // Si no hay suficientes candidatos, usamos todo el recetario (fall-back)
        if (candidatos.isEmpty()) candidatos = new ArrayList<>(recetario.getRecetas());

        // Filtramos recetas con calorías > 0 para poder controlar el tope. Si no, igual se consideran.
        List<Receta> conCalorias = new ArrayList<>();
        List<Receta> sinCalorias = new ArrayList<>();
        for (Receta r : candidatos) {
            if (r.getCalorias() > 0) conCalorias.add(r);
            else sinCalorias.add(r);
        }

        // Ordenamos por calorías para que el greedy tenga sentido, pero luego randomizamos.
        conCalorias.sort(Comparator.comparingInt(Receta::getCalorias));
        Collections.shuffle(conCalorias, new Random());
        Collections.shuffle(sinCalorias, new Random());

        List<Receta> pool = new ArrayList<>();
        pool.addAll(conCalorias);
        pool.addAll(sinCalorias);

        Random rnd = new Random();
        List<MenuDia> semana = new ArrayList<>(dias);

        for (int d = 0; d < dias; d++) {
            MenuDia dia = armarDia(pool, caloriasMaxDia, rnd);
            semana.add(dia);
        }
        return semana;
    }

    private static List<Receta> filtrarCandidatos(Recetario recetario, String filtroTipo) {
        List<Receta> out = new ArrayList<>();
        boolean usarFiltro = filtroTipo != null && !filtroTipo.trim().isEmpty();
        String f = usarFiltro ? filtroTipo.trim().toLowerCase(Locale.ROOT) : null;
        for (Receta r : recetario.getRecetas()) {
            if (!usarFiltro) { out.add(r); continue; }
            String t = r.getTipo();
            if (t != null && t.toLowerCase(Locale.ROOT).contains(f)) out.add(r);
        }
        return out;
    }

    /** Heurística random-greedy: intenta armar 4 recetas únicas ≤ tope. Si no logra, devuelve lo “mejor bajo tope”. */
    private static MenuDia armarDia(List<Receta> pool, int caloriasMaxDia, Random rnd) {
        final int INTENTOS = 1500;  // intentos aleatorios por día
        MenuDia mejor = null;       // mejor combinación que no supere el tope (o la de menor exceso si no hay)
        int mejorScore = Integer.MAX_VALUE; // distancia al tope (si <=0) o exceso si > tope

        // Precomputar tamaño
        int n = pool.size();
        if (n == 0) return new MenuDia();

        for (int it = 0; it < INTENTOS; it++) {
            // escoger 4 distintas
            Receta a = pool.get(rnd.nextInt(n));
            Receta b = pool.get(rnd.nextInt(n));
            Receta c = pool.get(rnd.nextInt(n));
            Receta d = pool.get(rnd.nextInt(n));
            // evitar duplicados dentro del mismo día
            if (a == b || a == c || a == d || b == c || b == d || c == d) continue;

            int total = Math.max(0, a.getCalorias()) + Math.max(0, b.getCalorias())
                    + Math.max(0, c.getCalorias()) + Math.max(0, d.getCalorias());

            int score;
            if (caloriasMaxDia > 0) {
                // Objetivo: total <= tope y lo más cercano posible
                score = (total <= caloriasMaxDia) ? (caloriasMaxDia - total) : (total - caloriasMaxDia + 10_000);
            } else {
                // Sin tope: cualquier 4 está bien; preferimos menor total para que no explote
                score = total;
            }

            if (score < mejorScore) {
                MenuDia md = new MenuDia();
                // Asignación fija por orden
                md.desayuno = a; md.almuerzo = b; md.merienda = c; md.cena = d;
                mejor = md;
                mejorScore = score;
                // si clavamos exactamente el tope, es difícil mejorar
                if (caloriasMaxDia > 0 && (caloriasMaxDia - total) == 0) break;
            }
        }

        // si no logró 4 (pool chico), rellenamos como podamos
        if (mejor == null) {
            mejor = new MenuDia();
            List<Receta> copia = new ArrayList<>(pool);
            Collections.shuffle(copia, rnd);
            if (!copia.isEmpty()) mejor.desayuno = copia.remove(0);
            if (!copia.isEmpty()) mejor.almuerzo  = copia.remove(0);
            if (!copia.isEmpty()) mejor.merienda  = copia.remove(0);
            if (!copia.isEmpty()) mejor.cena      = copia.remove(0);
        }
        return mejor;
    }

    public static String imprimirAvanzado(List<MenuDia> semana, String titulo, int caloriasMaxDia) {
        String[] dias = {"Lunes","Martes","Miércoles","Jueves","Viernes","Sábado","Domingo"};
        StringBuilder sb = new StringBuilder();
        sb.append("=== Menú semanal ").append(titulo == null || titulo.isBlank()? "" : "(" + titulo + ") ").append("===\n");
        if (caloriasMaxDia > 0) sb.append("Tope de calorías por día: ").append(caloriasMaxDia).append("\n");
        for (int i = 0; i < semana.size(); i++) {
            String nombreDia = dias[i % dias.length];
            MenuDia md = semana.get(i);
            sb.append(nombreDia).append(" = {")
                    .append(md.desayuno  == null? "-" : md.desayuno.getNombre()).append(", ")
                    .append(md.almuerzo  == null? "-" : md.almuerzo.getNombre()).append(", ")
                    .append(md.merienda  == null? "-" : md.merienda.getNombre()).append(", ")
                    .append(md.cena      == null? "-" : md.cena.getNombre()).append("}")
                    .append("  [Total: ").append(md.totalCalorias()).append(" Kcal]\n");
        }
        return sb.toString();
    }
}
