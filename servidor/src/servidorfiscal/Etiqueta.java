/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package servidorfiscal;

import org.xml.sax.Attributes;

/**
 *
 * @author forest
 */
public class Etiqueta {
    String nombre;
    Attributes atributos;
    String contenido;

    public Etiqueta() {
        contenido="";
    }

    public Attributes getAtributos() {
        return atributos;
    }

    public void setAtributos(Attributes atributos) {
        this.atributos = atributos;
    }
    public String get(String atributo){
         return atributos.getValue(atributo.toLowerCase());
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getContenido() {
        return contenido;
    }

    
    
}
