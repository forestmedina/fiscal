/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package servidorfiscal;

import java.util.Date;
import servidorfiscal.excepciones.ExcepcionFiscal;
import tfhka.ReportData;
import tfhka.S1PrinterData;
import tfhka.S2PrinterData;
import tfhka.S3PrinterData;
import tfhka.S4PrinterData;
import tfhka.S5PrinterData;

/**
 *
 * @author forest
 */
public interface Controlador {
    public void imprimirComentario(String comentario) throws ExcepcionFiscal;
    public void mensajePantalla(String comentario) throws ExcepcionFiscal;
    public void facturarArticulo(Impuesto impuesto,Float precio,Float cantidad,String descripcion ) throws ExcepcionFiscal;
    public void pagar(String medioPago) throws ExcepcionFiscal;
    public void pagar(String medioPago,Float monto) throws ExcepcionFiscal;
    public void abrirFactura() throws ExcepcionFiscal;
     public void abrirDevolucion() throws ExcepcionFiscal;
     public void iniciarCajero( String clave) throws ExcepcionFiscal;
     public void imprimirEncabezado(String comentario,int linea) throws ExcepcionFiscal;
     public void devolverArticulo(Impuesto impuesto,Float precio,Float cantidad,String descripcion) throws ExcepcionFiscal;
     public void subtotalizar() throws ExcepcionFiscal;
     public void registrarDescuento(Float monto) throws ExcepcionFiscal;
     public void registrarDescuentoPorcentaje(Float monto) throws ExcepcionFiscal;
     public void registrarRecargo(Float monto) throws ExcepcionFiscal;
     public void registrarRecargoPorcentaje(Float monto) throws ExcepcionFiscal;
     public void anularArticulo(Impuesto impuesto,Float precio,Float cantidad,String descripcion) throws ExcepcionFiscal;
     public void anularDevolucion(Impuesto impuesto,Float precio,Float cantidad,String descripcion) throws ExcepcionFiscal;
     public void cancelarUltimaEntrada() throws ExcepcionFiscal;
     public void cerrar() throws ExcepcionFiscal;
     public void anular() throws ExcepcionFiscal;
     public long getUltimoNumeroFiscal() throws ExcepcionFiscal;

     public void imprimirTextoNoFiscal(String texto,FormatoTexto formato) throws ExcepcionFiscal;
     public void cerrarNoFiscal() throws ExcepcionFiscal;
     public void abrirGaveta() throws ExcepcionFiscal;
     public void setImpuestos(int tipo1,float tasa1,int tipo2,float tasa2, int tipo3,float tasa3) throws ExcepcionFiscal;
     public S1PrinterData getS1() throws ExcepcionFiscal;
     public S2PrinterData getS2() throws ExcepcionFiscal;
     public S3PrinterData getS3() throws ExcepcionFiscal;
     public S4PrinterData getS4() throws ExcepcionFiscal;
     public S5PrinterData getS5() throws ExcepcionFiscal;
     public void imprimirProgramacion() throws ExcepcionFiscal ;
     public void guardarImpuestos() throws ExcepcionFiscal;
     public void setEncabezadoPie(int linea, String texto) throws ExcepcionFiscal;
     public void setNombreMedio(String medio, String nombre) throws ExcepcionFiscal;




     /**
      * Comandos  para la Devolucion
      */



     public void imprimirReporteZ() throws ExcepcionFiscal;
     public void imprimirReporteZFechas(Date feInicio,Date feFinal) throws ExcepcionFiscal;
     public void imprimirReporteX() throws ExcepcionFiscal;
     public ReportData getReporteZ() throws ExcepcionFiscal;
}
