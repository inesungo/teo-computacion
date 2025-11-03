package main;

import java.util.*;

/**
 * Clase auxiliar para agrupar campos opcionales de una receta.
 * PARTE 2 - Estructura de datos auxiliar
 */
public class CamposOpcionales {
    public String dificultad;
    public String origen;
    public String tipo;
    public String observaciones;
    public List<String> relacionadas;
    public Map<String, String> infoAdicional;
    
    public CamposOpcionales() {
        this.relacionadas = new ArrayList<>();
        this.infoAdicional = new HashMap<>();
    }
}

