package main;

import java.util.*;

/**
 * Clase que representa un recetario completo (colección de recetas).
 * PARTE 2 - Estructura de datos
 */
public class Recetario {
    private List<Receta> recetas;
    
    public Recetario() {
        this.recetas = new ArrayList<>();
    }
    
    public Recetario(List<Receta> recetas) {
        this.recetas = recetas != null ? recetas : new ArrayList<>();
    }
    
    public List<Receta> getRecetas() {
        return recetas;
    }
    
    public void agregarReceta(Receta receta) {
        this.recetas.add(receta);
    }
    
    public int cantidadRecetas() {
        return recetas.size();
    }
    
    /**
     * Busca una receta por nombre (case-insensitive)
     */
    public Receta buscarReceta(String nombre) {
        for (Receta r : recetas) {
            if (r.getNombre() != null && r.getNombre().equalsIgnoreCase(nombre)) {
                return r;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RECETARIO ===\n");
        sb.append("Total de recetas: ").append(recetas.size()).append("\n\n");
        for (Receta r : recetas) {
            sb.append(r.toString()).append("\n");
        }
        return sb.toString();
    }
}

