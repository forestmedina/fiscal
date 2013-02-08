/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package servidorfiscal;

import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author forest
 */
public class Impresora {
    private String id;
    private Controlador controlador;
    private String puerto;
    private String idSesion;
    private ReentrantLock lock;

    public Impresora() {
        lock=new ReentrantLock();

    }



    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the controlador
     */
    public Controlador getControlador() {
        return controlador;
    }

    /**
     * @param controlador the controlador to set
     */
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    public String getPuerto() {
        return puerto;
    }

    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

    public synchronized  String getIdSesion() {
        return idSesion;
    }

    public synchronized  void setIdSesion(String idSesion) {
        this.idSesion = idSesion;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public void setLock(ReentrantLock lock) {
        this.lock = lock;
    }


    
}

