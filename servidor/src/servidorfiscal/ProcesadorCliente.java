/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package servidorfiscal;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import servidorfiscal.excepciones.ExcepcionFiscal;
import tfhka.ReportData;
import tfhka.S1PrinterData;
import tfhka.S2PrinterData;
import tfhka.S3PrinterData;
import tfhka.S5PrinterData;

/**
 *
 * @author forest
 */
public class ProcesadorCliente extends DefaultHandler implements Runnable{
    private static final int EST_FACTURA=1;
    private static final int EST_DEVOLUCION=2;
    private static final int EST_OCIOSO=3;

    Socket socketCliente;
    Writer escritorSockeCliente;
    Attributes atributosActual;
    String comando;
    Controlador controlador;
    LinkedList<Etiqueta> pila;
    String  idSesion;
    int estado;

    public void setIdSesion(String idSesion) {
        this.idSesion = idSesion;
    }

    public String getIdSesion() {
        return idSesion;
    }

    
    public void cambiarCliente(Socket socketCliente){
        this.socketCliente=socketCliente;
    }
    public ProcesadorCliente(Socket socketCliente) {
        this.socketCliente = socketCliente;
        estado=EST_OCIOSO;
        pila= new LinkedList<Etiqueta>();
        try {
            escritorSockeCliente = new OutputStreamWriter(socketCliente.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(this);
            reader.parse(new InputSource(socketCliente.getInputStream()));
            socketCliente.close();
            System.out.println("cerrado");
        } catch (SAXException ex) {
            Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
        }

           

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        Etiqueta etiqueta;
        etiqueta= new Etiqueta();
        pila.push(etiqueta);
        etiqueta.setAtributos(attributes);
        etiqueta.setNombre(qName);
        if(qName.compareTo("sesion")==0){
            try {
                escritorSockeCliente.write("<sesion>\n");
                escritorSockeCliente.write("<respuesta estado='101' mensaje='Sesion Iniciada'/>\n");
                escritorSockeCliente.flush();
            } catch (IOException ex) {
                Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        Etiqueta e= pila.peek();
        e.setContenido(e.getContenido().concat(String.copyValueOf(ch, start, length)));
    }



    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        Etiqueta e;
        e=pila.pop();
        if(e.getNombre().compareTo("factura")==0){
                abrirFactura(e);
        }else
        if(e.getNombre().compareTo("devolucion")==0){
                abrirDevolucion(e);
        }else
        if(e.getNombre().compareTo("iniciarCajero")==0){
            iniciarCajero(e);
        }else
        if(e.getNombre().compareTo("texto")==0){
            imprimirTexto(e,socketCliente);
        }else
        if(e.getNombre().compareTo("articulo")==0){
            registrarArticulo(e,socketCliente);
        }else
        if(e.getNombre().compareTo("subtotalizar")==0){
            subtotalizar(e,socketCliente);
        }else
        if(e.getNombre().compareTo("descuento")==0){
            descuento(e,socketCliente);
        }else
        if(e.getNombre().compareTo("recargo")==0){
            recargo(e,socketCliente);
        }else
        if(e.getNombre().compareTo("anular")==0){
            anular(e,socketCliente);
        }else
        if(e.getNombre().compareTo("anularEntrada")==0){
            anularEntrada(e,socketCliente);
        }else
        if(e.getNombre().compareTo("pagar")==0){
            pagar(e,socketCliente);
        }else
        if(e.getNombre().compareTo("numeroFiscal")==0){
            numeroFiscal(e,socketCliente);
        }else
        if(e.getNombre().compareTo("anularDocumento")==0){
            anularDocumento(e,socketCliente);
        }else
        if(e.getNombre().compareTo("guardarImpuesto")==0){
            guardarImpuesto(e,socketCliente);
        }else
        if(e.getNombre().compareTo("imprimirProgramacion")==0){
            imprimirProgramacion(e,socketCliente);
        }else
        if(e.getNombre().compareTo("reporteZ")==0){
            reporteZ(e,socketCliente);
        }else
        if(e.getNombre().compareTo("getReporteZ")==0){
            getReporteZ(e,socketCliente);
        }else
        if(e.getNombre().compareTo("reporteX")==0){
            reporteX(e,socketCliente);
        }else
        if(e.getNombre().compareTo("comentario")==0){
            comentario(e,socketCliente);
        }else if(e.getNombre().compareTo("mensajePantalla")==0){
            mensajePantalla(e,socketCliente);
        }else if(e.getNombre().compareTo("textoNoFiscal")==0){
            textoNoFiscal(e,socketCliente);
        }else if(e.getNombre().compareTo("cerrarNoFiscal")==0){
            cerrarNoFiscal(e,socketCliente);
        }else if(e.getNombre().compareTo("abrirGaveta")==0){
            abrirGaveta(e,socketCliente);
        }else if(e.getNombre().compareTo("setImpuestos")==0){
            setImpuestos(e,socketCliente);
        }else if(e.getNombre().compareTo("setNombreMedio")==0){
            setNombreMedio(e,socketCliente);
        }else if(e.getNombre().compareTo("setEncabezadoPie")==0){
            setEncabezadoPie(e,socketCliente);
        }else if(e.getNombre().compareTo("s1")==0){
            s1(e,socketCliente);
        }else if(e.getNombre().compareTo("s2")==0){
            s2(e,socketCliente);
        }else if(e.getNombre().compareTo("s3")==0){
            s3(e,socketCliente);
        }else if(e.getNombre().compareTo("s5")==0){
            s5(e,socketCliente);
        }else{
            desconocido();
        }
    }

    public void abrirFactura(Etiqueta e){
        if(estado==EST_OCIOSO){
            try {
                Impresora impresora;
                impresora = ServidorFiscal.getImpresora(e.get("impresora"));
//                if(impresora.getLock().tryLock(60, TimeUnit.SECONDS)){
                    estado=EST_FACTURA;
//                }
                enviarRespuestaExito(socketCliente);
            } catch (Exception ex) {
                Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
                enviarRespuestaError(socketCliente, ex);
            }
        }else{

        }

    }
    public void abrirDevolucion(Etiqueta e){
        if(estado==EST_OCIOSO){
            try {
                Impresora impresora;
                impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
//                if(impresora.getLock().tryLock(60, TimeUnit.SECONDS)){
                    estado=EST_DEVOLUCION;
//                }
                enviarRespuestaExito(socketCliente);
            } catch (Exception ex) {
                Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
                enviarRespuestaError(socketCliente, ex);
            }
        }else{

        }

    }

    public void iniciarCajero(Etiqueta e){
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
        switch(estado){
            case EST_DEVOLUCION:
            case EST_FACTURA:
                try {
                    impresora.getControlador().iniciarCajero(e.get("CLAVE"));
                    enviarRespuestaExito(socketCliente);
                } catch (ExcepcionFiscal ex) {
                    Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
                    enviarRespuestaError(socketCliente, ex);
                }
                break;
        }
    }

    private void imprimirTexto(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
        switch(estado){
            case EST_DEVOLUCION:
            case EST_FACTURA:
                try {
                    int linea= Integer.valueOf(e.get("LINEA"));
                    impresora.getControlador().imprimirEncabezado(e.getContenido(),linea);
                    enviarRespuestaExito(socketCliente);
                } catch (Exception ex) {
                    enviarRespuestaError(socketCliente, ex);
                    Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
        }
    }

    private void registrarArticulo(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
        switch(estado){
            case EST_DEVOLUCION:
                try {
                    float precio=Float.valueOf(e.get("PRECIO"));
                    float cantidad=Float.valueOf(e.get("CANTIDAD"));
                    Impuesto impuesto=Impuesto.valueOf(e.get("IMPUESTO"));
                    impresora.getControlador().devolverArticulo(impuesto, precio, cantidad, e.contenido);
                    enviarRespuestaExito(socketCliente);
                } catch (ExcepcionFiscal ex) {
                    Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
                    enviarRespuestaError(socketCliente,ex);
                }
                break;
            case EST_FACTURA:
               try {
                    float precio=Float.valueOf(e.get("PRECIO"));
                    float cantidad=Float.valueOf(e.get("CANTIDAD"));
                    Impuesto impuesto=Impuesto.valueOf(e.get("IMPUESTO"));
                    impresora.getControlador().facturarArticulo(impuesto, precio, cantidad, e.contenido);
                    enviarRespuestaExito(socketCliente);
                } catch (ExcepcionFiscal ex) {
                    Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
                    enviarRespuestaError(socketCliente,ex);
                }
                break;
        }
    }

    private void enviarRespuestaExito(Socket socketCliente) {
        try {
            escritorSockeCliente.write("<respuesta estado='101' mensaje='ok'/>\n");
            escritorSockeCliente.flush();
        } catch (IOException ex) {
            Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void enviarRespuestaExito(Socket socketClientem,String xmlAdicional) {
        try {
            escritorSockeCliente.write("<respuesta estado='101' mensaje='ok'>\n");
            escritorSockeCliente.write(xmlAdicional+"\n");
            escritorSockeCliente.write("</respuesta>\n");
            escritorSockeCliente.flush();
        } catch (IOException ex) {
            Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void enviarRespuestaError(Socket socketCliente, Exception ex) {
        try {
            escritorSockeCliente.write("<respuesta estado='401' mensaje='"+ex.getMessage()+"'/>\n");
            escritorSockeCliente.flush();
        } catch (IOException e) {
            Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void subtotalizar(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
        switch(estado){
            case EST_DEVOLUCION:
            case EST_FACTURA:
                try {
                    impresora.getControlador().subtotalizar();
                    enviarRespuestaExito(socketCliente);
                } catch (ExcepcionFiscal ex) {
                    Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
                    enviarRespuestaError(socketCliente, ex);
                }
                break;
        }
    }

    private void descuento(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
        switch(estado){
            case EST_DEVOLUCION:
            case EST_FACTURA:
                try {
                    String cadenaMonto= e.get("monto");
                    if(cadenaMonto.matches("[0-9]{1,2}([.,][0-9]{1,2})?[%]")){
                        float monto=Float.valueOf(cadenaMonto.replace("%", "").replace(",","."));
                        impresora.getControlador().registrarDescuentoPorcentaje(monto);
                    }else{
                        float monto=Float.valueOf(cadenaMonto);
                        impresora.getControlador().registrarDescuento(monto);
                    }
                    
                    
                    enviarRespuestaExito(socketCliente);
                } catch (ExcepcionFiscal ex) {
                    Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
                    enviarRespuestaError(socketCliente, ex);
                }
                break;
        }
    }

    private void recargo(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
        switch(estado){
            case EST_DEVOLUCION:
            case EST_FACTURA:
                try {
                    String cadenaMonto= e.get("monto");
                    if(cadenaMonto.matches("[0-9]{1,2}([.,][0-9]{1,2})?[%]")){
                        float monto=Float.valueOf(cadenaMonto.replace("%", "").replace(",","."));
                        impresora.getControlador().registrarRecargoPorcentaje(monto);
                    }else{
                        float monto=Float.valueOf(cadenaMonto);
                        impresora.getControlador().registrarRecargo(monto);
                    }
                    enviarRespuestaExito(socketCliente);
                } catch (ExcepcionFiscal ex) {
                    Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
                    enviarRespuestaError(socketCliente, ex);
                }
                break;
        }
    }

    private void anular(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
        switch(estado){
            case EST_DEVOLUCION:
            case EST_FACTURA:
                try {
                    impresora.getControlador().anular();
                    enviarRespuestaExito(socketCliente);
                } catch (ExcepcionFiscal ex) {
                    Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
                    enviarRespuestaError(socketCliente, ex);
                }
                break;
        }
    }

    private void anularEntrada(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
        switch(estado){
            case EST_DEVOLUCION:
            case EST_FACTURA:
                try {
                    impresora.getControlador().cancelarUltimaEntrada();
                    enviarRespuestaExito(socketCliente);
                } catch (ExcepcionFiscal ex) {
                    Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
                    enviarRespuestaError(socketCliente, ex);
                }
                break;
        }
    }

    private void pagar(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
        switch(estado){
            case EST_DEVOLUCION:
            case EST_FACTURA:
                try {

                    if(e.get("MONTO")!=null){
                        float monto=Float.valueOf(e.get("MONTO"));
                        impresora.getControlador().pagar(e.get("MEDIO"), monto);
                    }else{
                        impresora.getControlador().pagar(e.get("MEDIO"));
                    }
                    enviarRespuestaExito(socketCliente);
                } catch (ExcepcionFiscal ex) {
                    Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
                    enviarRespuestaError(socketCliente, ex);
                }
                break;
        }
    }

    private void anularDocumento(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
        switch(estado){
            case EST_DEVOLUCION:
            case EST_FACTURA:
            default:
                try {
                    impresora.getControlador().anular();
                    enviarRespuestaExito(socketCliente);
                } catch (ExcepcionFiscal ex) {
                    Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
                    enviarRespuestaError(socketCliente, ex);
                }
                break;
        }
    }

    private void reporteZ(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
                try{
                    if(e.get("fechaInicio")!=null){
                        SimpleDateFormat df= new SimpleDateFormat("dd/MM/YYYY");
                        Date fechaInicio=df.parse(e.get("fechaInicio"));
                        Date fechaFinal=df.parse(e.get("fechaFinal"));
                        impresora.getControlador().imprimirReporteZFechas(fechaInicio,fechaFinal);
                        enviarRespuestaExito(socketCliente);
                    }else{
                        impresora.getControlador().imprimirReporteZ();
                        enviarRespuestaExito(socketCliente);
                    }
                    
                }catch (ExcepcionFiscal ex){
                    Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
                    enviarRespuestaError(socketCliente, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
                    enviarRespuestaError(socketCliente,new Exception("Error: Formato de Fecha incorrecto. (dd/MM/YYYY)"));
                }catch(Exception ex){
                    Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
                    enviarRespuestaError(socketCliente,new Exception("Error Interno"));
                }
    }

    private void reporteX(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
                try {
                    impresora.getControlador().imprimirReporteX();
                    enviarRespuestaExito(socketCliente);
                } catch (ExcepcionFiscal ex) {
                    Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
                    enviarRespuestaError(socketCliente, ex);
                }
    }

    private void comentario(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
                try {
                    impresora.getControlador().imprimirComentario(e.getContenido());
                    enviarRespuestaExito(socketCliente);
                } catch (Exception ex) {
                    Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
                    enviarRespuestaError(socketCliente, ex);
                }
    }

    private void desconocido() {
        enviarRespuestaError(socketCliente,new Exception("Solicitud Desconocida"));
    }

    private void numeroFiscal(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
        try {
            long numero =impresora.getControlador().getUltimoNumeroFiscal();
            enviarRespuestaExito(socketCliente,"<numeroFiscal >"+numero+"</numeroFiscal>\n");
        } catch (Exception ex) {
            Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
            enviarRespuestaError(socketCliente, ex);
        }
    }

    private void getReporteZ(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
        try {
            ReportData reportData =impresora.getControlador().getReporteZ();
            String xml="<reporte >"
                     + "   <fecha>"+reportData.getZReportDate()+"</fecha>"
                     + "   <exento>"
                     + "      <ventas>"+reportData.getFreeSalesTax()+"</ventas>"
                     + "      <devolucion>"+reportData.getFreeTaxDevolution()+"</devolucion>"
                     + "   </exento>"
                     + "   <impuesto1>"
                     + "      <ventas>"+reportData.getGeneralRate1Sale()+"</ventas>"
                     + "      <impuestoVentas>"+reportData.getGeneralRate1Tax()+"</impuestoVentas>"
                     + "      <devolucion>"+reportData.getGeneralRateDevolution()+"</devolucion>"
                     + "      <impuestoDevolucion>"+reportData.getGeneralRateTaxDevolution()+"</impuestoDevolucion>"
                     + "   </impuesto1>"
                     + "   <impuesto2>"
                     + "      <ventas>"+reportData.getReducedRate2Sale()+"</ventas>"
                     + "      <impuestoVentas>"+reportData.getReducedRate2Tax()+"</impuestoVentas>"
                     + "      <devolucion>"+reportData.getReducedRateDevolution()+"</devolucion>"
                     + "      <impuestoDevolucion>"+reportData.getReducedRateTaxDevolution()+"</impuestoDevolucion>"
                     + "   </impuesto2>"
                     + "   <impuesto3>"
                     + "      <ventas>"+reportData.getAdditionalRate3Sal()+"</ventas>"
                     + "      <impuestoVentas>"+reportData.getAdditionalRate3Tax()+"</impuestoVentas>"
                     + "      <devolucion>"+reportData.getAdditionalRateDevolution()+"</devolucion>"
                     + "      <impuestoDevolucion>"+reportData.getAdditionalRateTaxDevolution()+"</impuestoDevolucion>"
                     + "   </impuesto3>"
                     + "</reporte>";

            enviarRespuestaExito(socketCliente,xml);
        } catch (Exception ex) {
            Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
            enviarRespuestaError(socketCliente, ex);
        }
    }

    private void mensajePantalla(Etiqueta e, Socket socketCliente) {
            Impresora impresora;
            impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
            try {
                impresora.getControlador().mensajePantalla(e.getContenido());
                enviarRespuestaExito(socketCliente);
            } catch (Exception ex) {
                Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
                enviarRespuestaError(socketCliente, ex);
            }
    }

    private void textoNoFiscal(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
        try {
            FormatoTexto formato= FormatoTexto.valueOf(e.get("formato"));
            impresora.getControlador().imprimirTextoNoFiscal(e.getContenido(),formato);
            enviarRespuestaExito(socketCliente);
        } catch (Exception ex) {
            Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
            enviarRespuestaError(socketCliente, ex);
        }
    }

    private void abrirGaveta(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
        try {
            impresora.getControlador().abrirGaveta();
            enviarRespuestaExito(socketCliente);
        } catch (Exception ex) {
            Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
            enviarRespuestaError(socketCliente, ex);
        }
    }

    private void cerrarNoFiscal(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
        try {
            impresora.getControlador().cerrarNoFiscal();
            enviarRespuestaExito(socketCliente);
        } catch (Exception ex) {
            Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
            enviarRespuestaError(socketCliente, ex);
        }
    }

    private void s1(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
        try {
            S1PrinterData s1data =impresora.getControlador().getS1();
            String xml="<s1>"
                     + "   <rif>"+s1data.getRIF()+"</rif>"
                     + "   <numeroMaquina>"+s1data.getRegisteredMachineNumber()+"</numeroMaquina>"
                     + "   <totalVentasDiarias>"+s1data.getTotalDailySales()+"</totalVentasDiarias>"
                     + "   <utlimaFactura>"+s1data.getLastInvoiceNumber()+"</utlimaFactura>"
                     + "   <cantFacturasDelDia>"+s1data.getQuantityOfInvoicesToday()+"</cantFacturasDelDia>"
                     + "   <cantDocumentosNoFiscalesDelDia>"+s1data.getQuantityNonFiscalDocuments()+"</cantDocumentosNoFiscalesDelDia>"
                     + "   <ultimoDocumentoNoFiscal>"+s1data.getNumberNonFiscalDocuments()+"</ultimoDocumentoNoFiscal>"
                     + "   <numeroCajero>"+s1data.getCashierNumber()+"</numeroCajero>"
                     + "   <contadorCierresDiarios>"+s1data.getDailyClosureCounter()+"</contadorCierresDiarios>"
                     + "   <contadorReportesAuditoria>"+s1data.getAuditReportsCounter()+"</contadorReportesAuditoria>"
                     + "</s1>"
                     ;

            enviarRespuestaExito(socketCliente,xml);
        } catch (Exception ex) {
            Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
            enviarRespuestaError(socketCliente, ex);
        }
    }
    private void s2(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
        try {
            S2PrinterData s2data =impresora.getControlador().getS2();
            String xml="<s2>"
                     + "   <subTotalBase>"+s2data.getSubTotalBases()+"</subTotalBase>"
                     + "   <subTotalIVA>"+s2data.getSudTotalTax()+"</subTotalIVA>"
                     + "   <numeroPagos>"+s2data.getNumberPaymentsMade()+"</numeroPagos>"
                     + "   <montoPorPagar>"+s2data.getAmountPayable()+"</montoPorPagar>"
                     + "   <condicion>"+s2data.getCondition()+"</condicion>"
                     + "</s2>"
                     ;

            enviarRespuestaExito(socketCliente,xml);
        } catch (Exception ex) {
            Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
            enviarRespuestaError(socketCliente, ex);
        }
    }
    private void s3(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
        try {
            S3PrinterData s3data =impresora.getControlador().getS3();
            String xml="<s3>"
                     + "   <tasa1>"
                     + "        <valor>"+s3data.getTax1()+"</valor>"
                     + "        <tipo>"+s3data.getTypeTax1()+"</tipo>"
                     + "   </tasa1>"
                     + "   <tasa2>"
                     + "        <valor>"+s3data.getTax2()+"</valor>"
                     + "        <tipo>"+s3data.getTypeTax2()+"</tipo>"
                     + "   </tasa2>"
                     + "   <tasa3>"
                     + "        <valor>"+s3data.getTax3()+"</valor>"
                     + "        <tipo>"+s3data.getTypeTax3()+"</tipo>"
                     + "   </tasa3>"
                     + "</s3>"
                     ;

            enviarRespuestaExito(socketCliente,xml);
        } catch (Exception ex) {
            Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
            enviarRespuestaError(socketCliente, ex);
        }
    }
//    private void s4(Etiqueta e, Socket socketCliente) {
//        Impresora impresora;
//        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
//        try {
//            S1PrinterData s1data =impresora.getControlador().getS1();
//            String xml="<s1>"
//                     + "   <rif>"+s1data.getRIF()+"</rif>"
//                     + "   <numeroMaquina>"+s1data.getRegisteredMachineNumber()+"</numeroMaquina>"
//                     + "   <totalVentasDiarias>"+s1data.getTotalDailySales()+"</totalVentasDiarias>"
//                     + "   <utlimaFactura>"+s1data.getLastInvoiceNumber()+"</utlimaFactura>"
//                     + "   <cantFacturasDelDia>"+s1data.getQuantityOfInvoicesToday()+"</cantFacturasDelDia>"
//                     + "   <cantDocumentosNoFiscalesDelDia>"+s1data.getQuantityNonFiscalDocuments()+"</cantDocumentosNoFiscalesDelDia>"
//                     + "   <ultimoDocumentoNoFiscal>"+s1data.getNumberNonFiscalDocuments()+"</ultimoDocumentoNoFiscal>"
//                     + "   <numeroCajero>"+s1data.getCashierNumber()+"</numeroCajero>"
//                     + "   <contadorCierresDiarios>"+s1data.getDailyClosureCounter()+"</contadorCierresDiarios>"
//                     + "   <contadorReportesAuditoria>"+s1data.getAuditReportsCounter()+"</contadorReportesAuditoria>"
//                     + "</s1>"
//                     ;
//
//            enviarRespuestaExito(socketCliente,xml);
//        } catch (Exception ex) {
//            Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
//            enviarRespuestaError(socketCliente, ex);
//        }
//    }
    private void s5(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
        try {
            S5PrinterData s5data =impresora.getControlador().getS5();
            String xml="<s5>"
                     + "   <rif>"+s5data.getRIF()+"</rif>"
                     + "   <serialMaquina>"+s5data.getRegisteredMachineNumber()+"</serialMaquina>"
                     + "   <memoriaTotal>"+s5data.getCapacityTotalMemoryAudit()+"</memoriaTotal>"
                     + "   <memoriaDisponible>"+s5data.getDisponyCapacityMemoryAudit()+"</memoriaDisponible>"
                     + "   <documentosRegistrados>"+s5data.getNumberDocumentRegisters()+"</documentosRegistrados>"
                     + "   <numeroMemoriaAuditoria>"+s5data.getNumberMemoryAudit()+"</numeroMemoriaAuditoria>"
                     + "</s5>"
                     ;
            enviarRespuestaExito(socketCliente,xml);
        } catch (Exception ex) {
            Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
            enviarRespuestaError(socketCliente, ex);
        }
    }

    private void setImpuestos(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
        try {
            impresora.getControlador()
                      .setImpuestos(Integer.valueOf(e.get("tipo1")),
                                    Float.valueOf(e.get("tasa1")),
                                    Integer.valueOf(e.get("tipo2")),
                                    Float.valueOf(e.get("tasa2")),
                                    Integer.valueOf(e.get("tipo3")),
                                    Float.valueOf(e.get("tasa3")));
            enviarRespuestaExito(socketCliente);
        } catch (Exception ex) {
            Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
            enviarRespuestaError(socketCliente, ex);
        }
    }

    private void guardarImpuesto(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
        try {
            impresora.getControlador().guardarImpuestos();
            enviarRespuestaExito(socketCliente);
        } catch (Exception ex) {
            Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
            enviarRespuestaError(socketCliente, ex);
        }
    }

    private void imprimirProgramacion(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
        try {
            impresora.getControlador().imprimirProgramacion();
            enviarRespuestaExito(socketCliente);
        } catch (Exception ex) {
            Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
            enviarRespuestaError(socketCliente, ex);
        }
    }

    private void setNombreMedio(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
        try {
            impresora.getControlador().setNombreMedio(e.get("medio"),e.get("nombre"));
            enviarRespuestaExito(socketCliente);
        } catch (Exception ex) {
            Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
            enviarRespuestaError(socketCliente, ex);
        }
    }

    private void setEncabezadoPie(Etiqueta e, Socket socketCliente) {
        Impresora impresora;
        impresora = ServidorFiscal.getImpresora(e.get("IMPRESORA"));
        try {
            impresora.getControlador().setEncabezadoPie(Integer.valueOf(e.get("linea")),e.getContenido());
            enviarRespuestaExito(socketCliente);
        } catch (Exception ex) {
            Logger.getLogger(ProcesadorCliente.class.getName()).log(Level.SEVERE, null, ex);
            enviarRespuestaError(socketCliente, ex);
        }
    }
}
