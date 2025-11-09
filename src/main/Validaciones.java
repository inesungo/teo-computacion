package main;

import java.util.*;
import java.util.regex.*;

public final class Validaciones {
    private Validaciones() {}

    public static List<String> validarRecetasRelacionadas(Recetario recetario) {
        List<String> errores = new ArrayList<>();
        if (recetario == null || recetario.getRecetas() == null) return errores;

        // normalizador: trim + lower + colapsar espacios + quitar comillas al borde
        java.util.function.Function<String,String> norm = s -> {
            if (s == null) return "";
            String t = s.trim();
            // quitar comillas simples/dobles alrededor si quedaron del parser
            if ((t.startsWith("\"") && t.endsWith("\"")) || (t.startsWith("'") && t.endsWith("'"))) {
                t = t.substring(1, t.length() - 1).trim();
            }
            // colapsar espacios múltiples internos
            t = t.replaceAll("\\s+", " ").toLowerCase(java.util.Locale.ROOT);
            return t;
        };

        // set de nombres existentes (normalizados)
        Set<String> nombres = new HashSet<>();
        for (Receta r : recetario.getRecetas()) {
            if (r.getNombre() != null) nombres.add(norm.apply(r.getNombre()));
        }

        for (Receta r : recetario.getRecetas()) {
            String propio = norm.apply(r.getNombre());
            List<String> rels = r.getRecetasRelacionadas();
            if (rels == null) continue;

            for (String rel : rels) {
                String k = norm.apply(rel);
                if (k.isEmpty()) continue;

                if (k.equals(propio)) {
                    errores.add("Receta '" + r.getNombre() + "' se relaciona consigo misma.");
                } else if (!nombres.contains(k)) {
                    errores.add("Receta relacionada '" + rel + "' no existe (referida desde '" + r.getNombre() + "').");
                }
            }
        }
        return errores;
    }


    public static List<String> validarUnidadesIngredientes(Recetario recetario) {
        List<String> errores = new ArrayList<>();
        for (Receta r : recetario.getRecetas()) {
            for (Ingrediente i : r.getIngredientes()) {
                String u = i.getUnidad();
                if (!Unidades.esUnidadSoportada(u)) {
                    errores.add("Unidad no soportada '" + u + "' en ingrediente '" + i.getNombre() + "' de '" + r.getNombre() + "'.");
                }
            }
        }
        return errores;
    }

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

}