package main;

/**
 * Clase que representa un ingrediente de una receta.
 * PARTE 2 - Estructura de datos básica
 */
public class Ingrediente {
    private String nombre;
    private String cantidad;
    private String unidad;
    
    public Ingrediente() {
    }
    
    public Ingrediente(String nombre, String cantidad, String unidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.unidad = unidad;
    }
    
    // Getters y Setters
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getCantidad() {
        return cantidad;
    }
    
    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
    
    public String getUnidad() {
        return unidad;
    }
    
    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }
    
    /**
     * Convierte la cantidad a un valor numérico en una unidad base.
     * Útil para operaciones matemáticas (ej: calcular totales).
     */
    public double getCantidadEnUnidadBase() {
        Double val = main.Unidades.parseCantidad(this.cantidad);
        if (val == null) return Double.NaN; // "a gusto" o sin cantidad
        return main.Unidades.aUnidadBase(val, this.unidad);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(nombre);
        if (cantidad != null) {
            sb.append(" ").append(cantidad);
        }
        if (unidad != null) {
            sb.append(" ").append(unidad);
        }
        return sb.toString();
    }
}

