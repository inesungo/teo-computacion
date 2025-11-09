package main;

import java.util.*;

public final class CarritoCompras {
    private CarritoCompras() {}

    public static Map<String, ItemCarrito> consolidar(List<Receta> recetas) {
        Map<String, ItemCarrito> mapa = new LinkedHashMap<>();
        for (Receta r : recetas) {
            for (Ingrediente ing : r.getIngredientes()) {
                String nombre = normalizarNombre(ing.getNombre());
                Double cantBase = Unidades.parseCantidad(ing.getCantidad());
                String base = Unidades.unidadBase(ing.getUnidad());
                double valor = (cantBase == null) ? Double.NaN : Unidades.aUnidadBase(cantBase, ing.getUnidad());

                String clave = nombre + "|" + (base == null ? "" : base);
                ItemCarrito item = mapa.get(clave);
                if (item == null) {
                    mapa.put(clave, new ItemCarrito(nombre, base, valor));
                } else {
                    if (Double.isNaN(item.getCantidad()) || Double.isNaN(valor)) {
                        // Si cualquiera es "a gusto", mantener "a gusto"
                        try {
                            java.lang.reflect.Field f = ItemCarrito.class.getDeclaredField("cantidad");
                            f.setAccessible(true);
                            f.set(item, Double.NaN);
                        } catch (Exception ignored) {}
                    } else {
                        item.agregar(valor);
                    }
                }
            }
        }
        return mapa;
    }

    private static String normalizarNombre(String nombre) {
        return nombre == null ? "" : nombre.trim().toLowerCase(java.util.Locale.ROOT);
    }

    public static String imprimir(Map<String, ItemCarrito> carro) {
        StringBuilder sb = new StringBuilder("=== Carrito de compras ===\n");
        for (ItemCarrito it : carro.values()) sb.append(" - ").append(it).append("\n");
        return sb.toString();
    }


    public static Map<String, ItemCarrito> consolidarConPersonas(List<Receta> recetas, int personas) {
        Map<String, ItemCarrito> mapa = new LinkedHashMap<>();
        for (Receta r : recetas) {
            double factor = (r.getPorciones() > 0) ? ((double) personas / r.getPorciones()) : 1.0;

            for (Ingrediente ing : r.getIngredientes()) {
                String nombre = normalizarNombre(ing.getNombre());
                Double cantBase = Unidades.parseCantidad(ing.getCantidad());
                String base = Unidades.unidadBase(ing.getUnidad());
                double valor = (cantBase == null) ? Double.NaN : Unidades.aUnidadBase(cantBase, ing.getUnidad());
                if (!Double.isNaN(valor)) valor *= factor;

                String clave = nombre + "|" + (base == null ? "" : base);
                ItemCarrito item = mapa.get(clave);
                if (item == null) {
                    mapa.put(clave, new ItemCarrito(nombre, base, valor));
                } else {
                    if (Double.isNaN(item.getCantidad()) || Double.isNaN(valor)) {
                        try {
                            var f = ItemCarrito.class.getDeclaredField("cantidad");
                            f.setAccessible(true);
                            f.set(item, Double.NaN);
                        } catch (Exception ignored) {}
                    } else {
                        item.agregar(valor);
                    }
                }
            }
        }
        return mapa;
    }

}