/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package servidorfiscal;

/**
 *
 * @author forest
 */
public enum FormatoTexto {
    NORMAL("0"),
    NEGRITA("*"),
    EXPANDIDO(">"),
    DOBLE_NEGRITA("$"),
    CENTRADO("!"),
    CENTRADO_NEGRITA("¡"),
    ;

    private String comando;

    private FormatoTexto(String comando) {
        this.comando = comando;
    }

    public String getComando() {
        return comando;
    }

    



}
