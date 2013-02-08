/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package servidorfiscal;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidorfiscal.excepciones.ExcepcionFiscal;
import servidorfiscal.excepciones.ExcepcionFiscalLongitudMaximaExcedida;
import tfhka.PrinterExeption;
import tfhka.ReportData;
import tfhka.S1PrinterData;
import tfhka.S2PrinterData;
import tfhka.S3PrinterData;
import tfhka.S4PrinterData;
import tfhka.S5PrinterData;
import tfhka.Tfhka;

/**
 *
 * @author forest
 */
public class ControladorBixolon implements Controlador{
    private Tfhka impresora;
    private String puerto;
    public ControladorBixolon(String puerto) {
            impresora= new Tfhka(puerto);
            this.puerto= puerto;
    }

    public void imprimirComentario(String comentario) throws ExcepcionFiscal{
        if(comentario.length()>20){
            throw new ExcepcionFiscalLongitudMaximaExcedida("El comentario es muy largo, la longitud maxima es de 20 caracteres");
        }
        try {
            getImpresora().SendCmd("@" + comentario);
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal(ex);
        }
    }
    public void mensajePantalla(String comentario) throws ExcepcionFiscal{
        if(comentario.length()>20){
            throw new ExcepcionFiscalLongitudMaximaExcedida("El comentario es muy largo, la longitud maxima es de 20 caracteres");
        }
        try {
            getImpresora().SendCmd("c" + comentario);
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal(ex);
        }
    }

    public void facturarArticulo(Impuesto impuesto,Float precio,Float cantidad,String descripcion ) throws ExcepcionFiscal{
        try {
            String comando;
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            DecimalFormat fmt;
            comando = getCodigoImpuesto(impuesto);
            fmt = new DecimalFormat("00000000.00");
            fmt.setDecimalFormatSymbols(simbolos);
            comando += fmt.format(precio).replace(".", "");
            fmt = new DecimalFormat("00000.000 ");
            fmt.setDecimalFormatSymbols(simbolos);
            comando += fmt.format(cantidad).replace(".", "");
            comando += descripcion;
            getImpresora().SendCmd(comando);
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }

    private String getCodigoImpuestoAnulacion(Impuesto impuesto){
        switch(impuesto){
            case EXENTO:
                return String.valueOf((char)0xA);
            case IMPUESTO1:
                return "¡";
            case IMPUESTO2:
                return "¢";
            case IMPUESTO3:
                return "£";
        }
        return "!";
    }
    private String getCodigoImpuesto(Impuesto impuesto){
        switch(impuesto){
            case EXENTO:
                return " ";
            case IMPUESTO1:
                return "!";
            case IMPUESTO2:
                return "\"";
            case IMPUESTO3:
                return "#";
        }
        return "!";
    }
    private String getCodigoImpuestoDevolucion(Impuesto impuesto){
        switch(impuesto){
            case EXENTO:
                return "0";
            case IMPUESTO1:
                return "1";
            case IMPUESTO2:
                return "2";
            case IMPUESTO3:
                return "3";
        }
        return "0";
    }

    public void pagar(String medioPago) {
        try {
            
            getImpresora().SendCmd("1" + medioPago);
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void pagar(String pago,float monto) {
        try {

            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            DecimalFormat fmt;
            fmt = new DecimalFormat("0000000000.00");
            fmt.setDecimalFormatSymbols(simbolos);
            getImpresora().SendCmd("2" + monto+fmt.format(monto).replace(".", ""));
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void pagar(String medioPago, Float monto) {
        try {
            getImpresora().SendCmd("1" + medioPago);
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void abrirFactura() {
        
    }

    public void abrirDevolucion() {
        
    }

    public void iniciarCajero(String clave) throws ExcepcionFiscal {
        if(clave.matches("[0-9]{5}")){
            try {
                getImpresora().SendCmd("5" + clave);
            } catch (PrinterExeption ex) {
                Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
                   throw new ExcepcionFiscal(ex.getMessage(),ex);
            }
        }else{
            throw  new ExcepcionFiscal("La clave debe ser de 5 digitos numericos");
        }
    }

    public void imprimirEncabezado(String comentario, int linea) throws ExcepcionFiscal{
        DecimalFormat fmt= new DecimalFormat("00");
        try {
            getImpresora().SendCmd("i" + fmt.format(linea) + comentario);
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }

    public void devolverArticulo(Impuesto impuesto, Float precio, Float cantidad, String descripcion) throws  ExcepcionFiscal{
         try {
            String comando;
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            DecimalFormat fmt;
            comando="d";
            comando += getCodigoImpuestoDevolucion(impuesto);
            fmt = new DecimalFormat("00000000.00");
            fmt.setDecimalFormatSymbols(simbolos);
            comando += fmt.format(precio).replace(".", "");
            fmt = new DecimalFormat("00000.000 ");
            fmt.setDecimalFormatSymbols(simbolos);
            comando += fmt.format(cantidad).replace(".", "");
            comando += descripcion;
            getImpresora().SendCmd(comando);
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }

    public void subtotalizar() throws ExcepcionFiscal{
        try {
            getImpresora().SendCmd("3");
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }

    public void registrarDescuento(Float monto) throws ExcepcionFiscal{
        try {
            if(monto<0){
                throw new ExcepcionFiscal("El monto debe ser positivo");
            }
            DecimalFormat fmt= new DecimalFormat("0000000.00");
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            fmt.setDecimalFormatSymbols(simbolos);
            getImpresora().SendCmd("q-"+fmt.format(monto).replace(".", ""));
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }
    public void registrarDescuentoPorcentaje(Float monto) throws ExcepcionFiscal{
        try {
            if(monto<0){
                throw new ExcepcionFiscal("El monto debe ser positivo");
            }
            DecimalFormat fmt= new DecimalFormat("00.00");
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            fmt.setDecimalFormatSymbols(simbolos);
            getImpresora().SendCmd("p-"+fmt.format(monto).replace(".", ""));
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }

    public void registrarRecargo(Float monto) throws ExcepcionFiscal{
        try {
            if(monto<0){
                throw new ExcepcionFiscal("El monto debe ser positivo");
            }
            DecimalFormat fmt= new DecimalFormat("0000000.00");
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            fmt.setDecimalFormatSymbols(simbolos);
            getImpresora().SendCmd("q+"+fmt.format(monto).replace(".", ""));
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal("Error:"+ex.getMessage(),ex);

    }
    }
    public void registrarRecargoPorcentaje(Float monto) throws ExcepcionFiscal{
        try {
            if(monto<0){
                throw new ExcepcionFiscal("El monto debe ser positivo");
            }
            DecimalFormat fmt= new DecimalFormat("00.00");
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            fmt.setDecimalFormatSymbols(simbolos);
            getImpresora().SendCmd("p+"+fmt.format(monto).replace(".", ""));
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal("Error:"+ex.getMessage(),ex);

    }
    }

    public void anularArticulo(Impuesto impuesto, Float precio, Float cantidad, String descripcion)throws ExcepcionFiscal {
        try {
            String comando;
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            DecimalFormat fmt;
            comando = getCodigoImpuestoAnulacion(impuesto);
            fmt = new DecimalFormat("00000000.00");
            fmt.setDecimalFormatSymbols(simbolos);
            comando += fmt.format(precio).replace(".", "");
            fmt = new DecimalFormat("00000.000 ");
            fmt.setDecimalFormatSymbols(simbolos);
            comando += fmt.format(cantidad).replace(".", "");
            comando += descripcion;
            getImpresora().SendCmd(comando);
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }

    public void anularDevolucion(Impuesto impuesto, Float precio, Float cantidad, String descripcion) throws ExcepcionFiscal{
         try {
            String comando;
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            DecimalFormat fmt;
            comando="d";
            comando += getCodigoImpuestoDevolucion(impuesto);
            fmt = new DecimalFormat("00000000.00");
            fmt.setDecimalFormatSymbols(simbolos);
            comando += fmt.format(precio).replace(".", "");
            fmt = new DecimalFormat("00000.000 ");
            fmt.setDecimalFormatSymbols(simbolos);
            comando += fmt.format(cantidad).replace(".", "");
            comando += descripcion;
            getImpresora().SendCmd(comando);
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }

    public void cancelarUltimaEntrada() throws  ExcepcionFiscal{
        try {
            getImpresora().SendCmd("k");
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }

    public void cerrar() {
    }

    public void anular() throws ExcepcionFiscal {
         try {
            getImpresora().SendCmd("7");
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }

    public long getUltimoNumeroFiscal() throws ExcepcionFiscal{
        try {
            return getImpresora().getS1PrinterData().getLastInvoiceNumber();
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw  new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }

    public void imprimirReporteZ() throws ExcepcionFiscal{
        try {
            getImpresora().printZReport();
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw  new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }

    public void imprimirReporteZFechas(Date feInicio, Date feFinal)throws  ExcepcionFiscal {
        try {
            getImpresora().printZReport(feInicio, feFinal);
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw  new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }

    public void imprimirReporteX() throws  ExcepcionFiscal {
        try {
            getImpresora().printXReport();
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw  new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }

    public ReportData getReporteZ() throws ExcepcionFiscal{
        try {
            return getImpresora().getZReport(new Date(),new Date())[0];
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw  new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }

    public void imprimirTextoNoFiscal(String texto,FormatoTexto formato) throws ExcepcionFiscal{
        try {
            getImpresora().SendCmd("80"+formato.getComando()+texto);
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }

    public void abrirGaveta() throws ExcepcionFiscal {
        try {
            getImpresora().SendCmd("w");
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }

    public void cerrarNoFiscal() throws ExcepcionFiscal {
        try {
            getImpresora().SendCmd("810");
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }
    public void imprimirProgramacion() throws ExcepcionFiscal {
        try {
            getImpresora().SendCmd("D");
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }
    public void guardarImpuestos() throws ExcepcionFiscal {
        try {
            getImpresora().SendCmd("Pt");
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }
    public S1PrinterData getS1() throws ExcepcionFiscal{
        try {
            return getImpresora().getS1PrinterData();
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }
    public S2PrinterData getS2() throws ExcepcionFiscal{
        try {
            return getImpresora().getS2PrinterData();
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }
    public S3PrinterData getS3() throws ExcepcionFiscal{
        try {
            return getImpresora().getS3PrinterData();
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }
    public S4PrinterData getS4() throws ExcepcionFiscal{
        try {
            return getImpresora().getS4PrinterData();
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }
    public S5PrinterData getS5() throws ExcepcionFiscal{
        try {
            return getImpresora().getS5PrinterData();
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }

    public Tfhka getImpresora() {
       if(!impresora.CheckFprinter()){
           impresora.OpenFpctrl(puerto);
       }
        return impresora;
    }
    public void setImpuestos(int tipo1,float tasa1,int tipo2,float tasa2, int tipo3,float tasa3) throws ExcepcionFiscal{
        try {

            String comando;
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            DecimalFormat fmt;
            comando="PT";
            fmt = new DecimalFormat("00.00");
            fmt.setDecimalFormatSymbols(simbolos);
            comando+=tipo1;
            comando += fmt.format(tasa1).replace(".", "");
            comando+=tipo2;
            comando += fmt.format(tasa2).replace(".", "");
            comando+=tipo3;
            comando += fmt.format(tasa3).replace(".", "");
            impresora.SendCmd(comando);
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }

    public void setEncabezadoPie(int linea, String texto) throws ExcepcionFiscal {
        try {
            String comando;
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            DecimalFormat fmt;
            comando="PH";
            fmt = new DecimalFormat("00");
            fmt.setDecimalFormatSymbols(simbolos);
            comando+=fmt.format(linea);
            comando+=texto;
            impresora.SendCmd(comando);
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }

    public void setNombreMedio(String medio, String nombre) throws ExcepcionFiscal {
        try {
            String comando;
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            DecimalFormat fmt;
            comando="PE";
            fmt = new DecimalFormat("00");
            fmt.setDecimalFormatSymbols(simbolos);
            comando+=medio;
            comando+=nombre;
            impresora.SendCmd(comando);
        } catch (PrinterExeption ex) {
            Logger.getLogger(ControladorBixolon.class.getName()).log(Level.SEVERE, null, ex);
            throw new ExcepcionFiscal("Error:"+ex.getMessage(),ex);
        }
    }


}
