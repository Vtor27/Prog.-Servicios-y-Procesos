package es.florida.mp2.controller;

import java.io.File;
import java.io.FilenameFilter;

public class FiltroExtension implements FilenameFilter{
    String extension;
    /**
     * Método constructor para el filtro por extensiones.
     * 
     * @param extension String con el nombre de la extensión (p.ej: .txt)
     */
    FiltroExtension(String extension){
        this.extension=extension;
    }
    
    /**
     * Método que devuelve true si alguno de los elementos del directorio tiene la extensión indicada para el filtro.
     */
    public boolean accept(File dir, String name){
        return name.endsWith(extension);
    }
}