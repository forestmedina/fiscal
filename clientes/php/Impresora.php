<?php
/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of Impresora
 *
 * @author forest
 */

define("FISCAL_TASA1","IMPUESTO1");
define("FISCAL_TASA2","IMPUESTO2");
define("FISCAL_TASA3","IMPUESTO3");
define("FISCAL_EXENTO","EXENTO");

interface Impresora {
     


     /**
      * Comandos de Facturacion y Devolucion
      */
     public function abrirFactura();
     public function abrirDevolucion();
     public function iniciarCajero($clave);
     public function imprimirEncabezado($comentario,$linea=0);
     public function registrarArticulo($impuesto,$precio,$cantidad,$descripcion);
     public function imprimirComentario($comentario);
     public function subtotalizar();
     public function registrarDescuento($monto);
     public function registrarRecargo($monto);
     public function anularArticulo($impuesto,$precio,$cantidad,$descripcion);
     public function cancelarUltimaEntrada();
     public function pagar($medioPago);
     public function pagarParcial($medioPago,$monto);
     public function anular();
     public function getUltimoNumeroFiscal();

     /**
      * Comandos  para la Devolucion
      */



     public function imprimirReporteZ();
     public function imprimirReporteZFechas($fechaInicio,$fechaFinal);
     public function imprimirReporteX();
     
}



?>
