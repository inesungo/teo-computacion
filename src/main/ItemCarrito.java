package main;

public class ItemCarrito {
    private final String nombreNormalizado;
    private final String unidadBase; // "g" | "ml" | "u" | otro
    private double cantidad;

    public ItemCarrito(String nombreNormalizado, String unidadBase, double cantidad) {
        this.nombreNormalizado = nombreNormalizado;
        this.unidadBase = unidadBase;
        this.cantidad = cantidad;
    }

    public void agregar(double delta) { this.cantidad += delta; }

    public String getNombreNormalizado() { return nombreNormalizado; }
    public String getUnidadBase() { return unidadBase; }
    public double getCantidad() { return cantidad; }

    @Override public String toString() {
        String cant = Double.isNaN(cantidad) ? "a gusto" : String.format(java.util.Locale.ROOT, "%.2f", cantidad);
        return nombreNormalizado + ": " + cant + (Double.isNaN(cantidad) ? "" : " " + (unidadBase == null ? "" : unidadBase));
    }
}