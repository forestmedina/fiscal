/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package servidorfiscal;

/**
 *
 * @author forest
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ServidorFiscal servidor= new ServidorFiscal();
        servidor.configurar();
        servidor.iniciar();
    }

}
