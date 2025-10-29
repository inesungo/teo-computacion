package main;

import java.util.*;

/**
 * Clase que representa una receta del recetario digital.
 * PARTE 2 - Estructura de datos básica
 */
public class Receta {
    // Campos obligatorios
    private String nombre;
    private List<Ingrediente> ingredientes;
    private List<String> pasos;
    private String tiempo;
    private int porciones;
    private int calorias;
    private List<String> categorias;
    private String dificultad;
    
    // Campos opcionales
    private String origen;
    private String tipo;
    private String observaciones;
    private List<String> recetasRelacionadas;
    private Map<String, String> informacionAdicional;
    
    public Receta() {
        this.ingredientes = new ArrayList<>();
        this.pasos = new ArrayList<>();
        this.categorias = new ArrayList<>();
        this.recetasRelacionadas = new ArrayList<>();
        this.informacionAdicional = new HashMap<>();
    }
    
    // Getters y Setters
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }
    
    public void agregarIngrediente(Ingrediente ingrediente) {
        this.ingredientes.add(ingrediente);
    }
    
    public List<String> getPasos() {
        return pasos;
    }
    
    public void agregarPaso(String paso) {
        this.pasos.add(paso);
    }
    
    public String getTiempo() {
        return tiempo;
    }
    
    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }
    
    public int getPorciones() {
        return porciones;
    }
    
    public void setPorciones(int porciones) {
        this.porciones = porciones;
    }
    
    public int getCalorias() {
        return calorias;
    }
    
    public void setCalorias(int calorias) {
        this.calorias = calorias;
    }
    
    public List<String> getCategorias() {
        return categorias;
    }
    
    public void agregarCategoria(String categoria) {
        this.categorias.add(categoria);
    }
    
    public String getDificultad() {
        return dificultad;
    }
    
    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }
    
    public String getOrigen() {
        return origen;
    }
    
    public void setOrigen(String origen) {
        this.origen = origen;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public List<String> getRecetasRelacionadas() {
        return recetasRelacionadas;
    }
    
    public void agregarRecetaRelacionada(String receta) {
        this.recetasRelacionadas.add(receta);
    }
    
    public Map<String, String> getInformacionAdicional() {
        return informacionAdicional;
    }
    
    public void agregarInformacionAdicional(String clave, String valor) {
        this.informacionAdicional.put(clave, valor);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RECETA: ").append(nombre).append("\n");
        sb.append("Ingredientes: ").append(ingredientes.size()).append("\n");
        sb.append("Pasos: ").append(pasos.size()).append("\n");
        sb.append("Tiempo: ").append(tiempo).append("\n");
        sb.append("Porciones: ").append(porciones).append("\n");
        sb.append("Calorías: ").append(calorias).append("\n");
        return sb.toString();
    }
}

