package main;

import java.util.*;
import java.util.regex.*;

public final class Validaciones {
    private Validaciones() {}

    public static Integer tiempoEnMinutos(String texto) {
        if (texto == null) return null;
        String t = texto.trim().toLowerCase(Locale.ROOT);

        // HH:MM o H:MM
        if (t.matches("\\d{1,2}:\\d{2}")) {
            String[] p = t.split(":");
            int h = Integer.parseInt(p[0]);
            int m = Integer.parseInt(p[1]);
            return h * 60 + m;
        }

        // X h Y m / Xh Ym / X h / Xh
        Pattern phym = Pattern.compile("(?:(\\d+)\\s*h)?\\s*(?:(\\d+)\\s*m(?:in(?:utos)?)?)?");
        Matcher mhym = phym.matcher(t.replaceAll("\\s+", " "));
        if (mhym.matches()) {
            String hs = mhym.group(1);
            String ms = mhym.group(2);
            if (hs != null || ms != null) {
                int h = hs == null ? 0 : Integer.parseInt(hs);
                int m = ms == null ? 0 : Integer.parseInt(ms);
                return h * 60 + m;
            }
        }

        // X min / X minutos / Xm
        Pattern pmin = Pattern.compile("(\\d+)\\s*(m|min|minutos)");
        Matcher mmin = pmin.matcher(t);
        if (mmin.matches()) return Integer.parseInt(mmin.group(1));

        // "45" → minutos
        if (t.matches("\\d+")) return Integer.parseInt(t);

        // "1h 15 min"
        Pattern phm = Pattern.compile("(\\d+)\\s*h\\s*(\\d+)\\s*(?:m|min|minutos)");
        Matcher mhm = phm.matcher(t);
        if (mhm.matches()) {
            int h = Integer.parseInt(mhm.group(1));
            int m = Integer.parseInt(mhm.group(2));
            return h * 60 + m;
        }

        return null; // no interpretable
    }

    public static List<String> validarTiempos(Recetario recetario) {
        List<String> advertencias = new ArrayList<>();
        for (Receta r : recetario.getRecetas()) {
            Integer mins = tiempoEnMinutos(r.getTiempo());
            if (mins == null) {
                advertencias.add("Tiempo no reconocido en '" + r.getNombre() + "': " + r.getTiempo());
            } else if (mins <= 0) {
                advertencias.add("Tiempo no positivo en '" + r.getNombre() + "': " + r.getTiempo());
            }
        }
        return advertencias;
    }

    public static void assertSinErrores(List<String> mensajes, String titulo) {
        if (mensajes == null || mensajes.isEmpty()) return;
        StringBuilder sb = new StringBuilder(titulo).append(":");
        for (String e : mensajes) sb.append("\n - ").append(e);
        throw new RuntimeException(sb.toString());
    }

    public static List<String> validarRecetasRelacionadasAgrupado(Recetario recetario) {
        List<String> salida = new ArrayList<>();
        if (recetario == null || recetario.getRecetas() == null) return salida;

        // normalizador
        java.util.function.Function<String,String> norm = s -> {
            if (s == null) return "";
            String t = s.trim();
            if ((t.startsWith("\"") && t.endsWith("\"")) || (t.startsWith("'") && t.endsWith("'"))) {
                t = t.substring(1, t.length()-1).trim();
            }
            return t.replaceAll("\\s+", " ").toLowerCase(java.util.Locale.ROOT);
        };

        // set nombres existentes (normalizados)
        Map<String,String> displayPorKey = new LinkedHashMap<>(); // key normalizado -> primera forma vista
        Set<String> existentes = new HashSet<>();
        for (Receta r : recetario.getRecetas()) {
            String disp = r.getNombre() == null ? "" : r.getNombre().trim();
            String key  = norm.apply(disp);
            if (!key.isEmpty()) {
                existentes.add(key);
                displayPorKey.putIfAbsent(key, disp);
            }
        }

        // agrupadores
        Map<String, LinkedHashSet<String>> faltantesAReferentes = new LinkedHashMap<>();
        LinkedHashSet<String> autorefs = new LinkedHashSet<>();

        for (Receta r : recetario.getRecetas()) {
            String propioKey = norm.apply(r.getNombre());
            String propioDisp = r.getNombre();
            for (String rel : r.getRecetasRelacionadas()) {
                String relKey  = norm.apply(rel);
                String relDisp = rel == null ? "" : rel.trim();

                if (relKey.isEmpty()) continue;

                if (relKey.equals(propioKey)) {
                    autorefs.add(propioDisp);
                } else if (!existentes.contains(relKey)) {
                    // receta inexistente: agrupar referentes
                    faltantesAReferentes
                            .computeIfAbsent(relDisp, k -> new LinkedHashSet<>())
                            .add(propioDisp);
                }
            }
        }

        // construir salida
        if (!faltantesAReferentes.isEmpty()) {
            for (Map.Entry<String, LinkedHashSet<String>> e : faltantesAReferentes.entrySet()) {
                String faltante = e.getKey();
                String refs = String.join(", ", e.getValue());
                salida.add("La receta relacionada '" + faltante + "' no existe (referida desde: " + refs + ").");
            }
        }
        if (!autorefs.isEmpty()) {
            if (autorefs.size() == 1) {
                salida.add("La receta '" + autorefs.iterator().next() + "' se relaciona consigo misma.");
            } else {
                salida.add("Las recetas " + autorefs.stream()
                        .map(n -> "'" + n + "'").reduce((a,b)->a+", "+b).orElse("") + " se relacionan consigo mismas.");
            }
        }

        return salida;
    }

    public static List<String> validarUnidadesPorIngrediente(Recetario recetario) {
        List<String> mensajes = new ArrayList<>();
        if (recetario == null || recetario.getRecetas() == null) return mensajes;

        // Mapa de unidades válidas por ingrediente (normalizados)
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
        // --- Nuevos ingredientes detectados en tus advertencias ---
        mapa.put("agua", Set.of("ml", "l", "taza", "tazas", "cuchara", "cucharas"));
        mapa.put("zanahoria", Set.of("u", "g", "kg", "taza", "tazas"));
        mapa.put("zapallito", Set.of("u", "g", "kg"));
        mapa.put("queso", Set.of("g", "kg", "taza", "tazas", "cuchara", "cucharas"));
        mapa.put("ajo", Set.of("u", "g", "cucharita", "cucharitas"));
        mapa.put("romero", Set.of("g", "cucharita", "cucharitas"));
        mapa.put("lechuga", Set.of("u", "g"));
        mapa.put("pan", Set.of("u", "g"));
        mapa.put("queso parmesano", Set.of("g", "taza", "tazas", "cuchara", "cucharas"));
        mapa.put("jugo limón", Set.of("ml", "l", "cuchara", "cucharas"));
        mapa.put("mostaza", Set.of("ml", "cuchara", "cucharas", "cucharita", "cucharitas"));
        mapa.put("calabaza", Set.of("u", "g", "kg", "taza", "tazas"));
        mapa.put("crema", Set.of("ml", "l", "taza", "tazas"));
        mapa.put("caldo", Set.of("ml", "l", "taza", "tazas"));
        mapa.put("nuez moscada", Set.of("g", "cucharita", "cucharitas"));
        mapa.put("manzana", Set.of("u", "g"));
        mapa.put("canela", Set.of("g", "cucharita", "cucharitas"));


        for (Receta r : recetario.getRecetas()) {
            for (Ingrediente i : r.getIngredientes()) {
                String nombre = i.getNombre() == null ? "" : i.getNombre().toLowerCase(Locale.ROOT).trim();
                String unidad = i.getUnidad() == null ? "" : i.getUnidad().toLowerCase(Locale.ROOT).trim();
                String cantidad = i.getCantidad() == null ? "" : i.getCantidad().toLowerCase(Locale.ROOT).trim();

                // Ignorar "a gusto" en cantidad
                if ("a gusto".equals(cantidad)) continue;

                // Si no está en el mapa
                if (!mapa.containsKey(nombre)) {
                    mensajes.add("Advertencia: No se definieron unidades válidas para '"
                            + i.getNombre() + "' en receta '" + r.getNombre() + "'.");
                    continue;
                }

                // Verificar unidad incoherente
                if (!unidad.isEmpty() && !mapa.get(nombre).contains(unidad)) {
                    mensajes.add("Error: Unidad '" + unidad + "' no válida para '" + i.getNombre()
                            + "' en receta '" + r.getNombre() + "'. Permitidas: " + mapa.get(nombre));
                }
            }
        }

        return mensajes;
    }



}