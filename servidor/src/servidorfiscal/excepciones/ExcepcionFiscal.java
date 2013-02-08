/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package servidorfiscal.excepciones;

/**
 *
 * @author forest
 */
public class ExcepcionFiscal extends Exception{

    public ExcepcionFiscal(Throwable cause) {
        super(cause);
    }

    public ExcepcionFiscal(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcepcionFiscal(String message) {
        super(message);
    }

    public ExcepcionFiscal() {
    }

}
