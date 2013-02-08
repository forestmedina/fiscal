/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package servidorfiscal;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.comm.CommDriver;
import javax.net.ServerSocketFactory;

/**
 *
 * @author forest
 */
public class ServidorFiscal {
    int puerto;
    boolean ssl;
    InetAddress direccion;
    List<Thread> hilos;
    
    
    private static HashMap<String,Impresora> impresoras;


    {
        impresoras= new HashMap<String, Impresora>();
    }
    public ServidorFiscal() {
        hilos= new LinkedList<Thread>();

        
    }
    public static Impresora getImpresora(String id){
        Impresora impresora=impresoras.get(id);
        if(impresora==null){
            impresora = new Impresora();
            impresora.setPuerto(id);
            impresora.setId(id);
            impresora.setControlador(new ControladorBixolon(impresora.getPuerto()));
            impresoras.put(id, impresora);
        }
         return impresora;
    }

    public static List<Impresora> getImpresoras(String id){
        LinkedList<Impresora> lista=new LinkedList();
        for(Impresora imp:impresoras.values()){
            lista.add(imp);
        }
        return lista;
    }
    public static void addImpresora(Impresora i,String id){
        impresoras.put(id, i);

    }

    public void configurar(){
        try {
            String driverName = "com.sun.comm.Win32Driver";
            CommDriver commDriver;
            try {
                commDriver = (CommDriver) Class.forName(driverName).newInstance();
                commDriver.initialize();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServidorFiscal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(ServidorFiscal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(ServidorFiscal.class.getName()).log(Level.SEVERE, null, ex);
            }

            puerto = 2086;
            ssl = false;
            direccion = InetAddress.getByName("0.0.0.0");
            Properties propiedades=new Properties();
            try {
                propiedades.load(new FileInputStream("configuracion.properties"));
                puerto = Integer.valueOf(propiedades.getProperty("puerto",String.valueOf(puerto)));
            } catch (IOException ex) {
                Logger.getLogger(ServidorFiscal.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Logger.getLogger("servidorfiscal").addHandler(new FileHandler("ServidorFiscal.log"));
            } catch (IOException ex) {
                Logger.getLogger(ServidorFiscal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(ServidorFiscal.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (UnknownHostException ex) {
            Logger.getLogger(ServidorFiscal.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
    public void iniciar(){
        ServerSocketFactory socketFactory = ServerSocketFactory.getDefault();
        
        try {
            SystemTray tray=SystemTray.getSystemTray();
             Image imagen=Toolkit.getDefaultToolkit().getImage(getClass().getResource("/servidorfiscal/imagenes/imprimir16x16.png"));

             PopupMenu menu= new PopupMenu();
            MenuItem itemSalir= new MenuItem("salir");
            itemSalir.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            menu.add(itemSalir);

            TrayIcon icono=new TrayIcon(imagen,"Impresión Fiscal",menu);
            tray.add(icono);
            

        } catch (AWTException ex) {
            Logger.getLogger(ServidorFiscal.class.getName()).log(Level.SEVERE, null, ex);
        }catch(Exception ex){
            Logger.getLogger(ServidorFiscal.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            ServerSocket socket=socketFactory.createServerSocket(puerto);
            while(true){
                Socket socketCliente =socket.accept();
                ProcesadorCliente procesadorCliente= new ProcesadorCliente(socketCliente);
                Thread hilo=new Thread(procesadorCliente);
                hilos.add(hilo);
                hilo.start();
            }

        } catch (IOException ex) {
            Logger.getLogger(ServidorFiscal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
