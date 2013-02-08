<?php
require_once 'Conexion.php';
require_once 'Respuesta.php';
define("FMT_NORMAL","NORMAL");
define("FMT_NEGRITA","NEGRITA");
define("FMT_EXPANDIDO","EXPANDIDO");
define("FMT_DOBLE_NEGRITA","DOBLE_NEGRITA");
define("FMT_CENTRADO","CENTRADO");
define("FMT_CENTRADO_NEGRITA","CENTRADO_NEGRITA");

/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of ImpresoraRemota
 *
 * @author forest
 */
class ImpresoraRemota implements Impresora{
     var $conexion;
     var $id;
     var $ultimaLinea;

     public function setID($id){
         $this->id=$id;
     }

     public function getID(){
         return $this->id;
     }
     public function  __construct(Conexion $conexion) {
        return $this->conexion= $conexion;
        $lineaImpresion=$this->ultimaLinea=0;
    }
     public function imprimirComentario($comentario){
        return $this->conexion->enviarComando("<comentario impresora='$this->id'><![CDATA[$comentario]]></comentario>");
     }
     public function abrirFactura(){
         $this->ultimaLinea=0;
         return $this->conexion->enviarComando("<factura impresora='$this->id'/>");
     }
     public function abrirDevolucion(){
         $this->ultimaLinea=0;
         return $this->conexion->enviarComando("<devolucion impresora='$this->id'/>");
     }

     public function iniciarCajero($clave){
         return $this->conexion->enviarComando("<iniciarCajero impresora='$this->id' clave='$clave'/>");
     }
     public function imprimirEncabezado($texto,$linea=-1){
         $lineaImpresion=$linea;
         if($linea==-1){
             $lineaImpresion=$this->ultimaLinea;
             $this->ultimaLinea++;
         }
         return $this->conexion->enviarComando("<texto impresora='$this->id' linea='$lineaImpresion'><![CDATA[$texto]]></texto>");
     }
     public function registrarArticulo($impuesto,$precio,$cantidad,$descripcion){
         return $this->conexion->enviarComando("<articulo  impresora='$this->id' impuesto='$impuesto' precio='$precio' cantidad='$cantidad' ><![CDATA[$descripcion]]></articulo>");
     }
     public function subtotalizar(){
         return $this->conexion->enviarComando("<subtotalizar impresora='$this->id' />");
		 
     }
     public function registrarDescuento($monto){
         return $this->conexion->enviarComando("<descuento impresora='$this->id' monto='$monto'/>");
     }
     public function registrarRecargo($monto){
         return $this->conexion->enviarComando("<recargo impresora='$this->id' monto='$monto'/>");
     }
     public function anularArticulo($impuesto,$precio,$cantidad,$descripcion){
         return $this->conexion->enviarComando("<anular impresora='$this->id' impuesto='$impuesto' precio='$precio' cantidad='$cantidad' ><![CDATA[$descripcion]]></articulo>");
     }
     public function cancelarUltimaEntrada(){
         return $this->conexion->enviarComando("<anularEntrada impresora='$this->id' />");
     }
     public function pagar($medioPago){
         return $this->conexion->enviarComando("<pagar impresora='$this->id' medio='$medioPago'/>");
     }
     public function pagarParcial($medioPago,$monto){
         return $this->conexion->enviarComando("<pagar impresora='$this->id' medio='$medio' monto='$monto'/>");
     }
     public function anular(){
         return $this->conexion->enviarComando("<anularDocumento impresora='$this->id' />");
     }
     public function getUltimoNumeroFiscal(){
         $respuesta = $this->conexion->enviarComando("<numeroFiscal impresora='$this->id' />\n");
         return intval((string)$respuesta->numeroFiscal);
     }

     /**
      * Comandos  para la Devolucion
      */



     public function imprimirReporteZ(){
         return $this->conexion->enviarComando("<reporteZ impresora='$this->id'  />");
     }
     public function imprimirReporteZFechas($fechaInicio,$fechaFinal){
        return  $this->conexion->enviarComando("<reporteZ impresora='$this->id' fechaInicio='$fechaInicio' fechaFinal='$fechaFinal' />");
     }
     public function imprimirReporteX(){
        return  $this->conexion->enviarComando("<reporteX impresora='$this->id'  />");
     }

     public function getReporteZ(){
        return  $this->conexion->enviarComando("<getReporteZ impresora='$this->id'  />");
     }
     public function mostrarMensajePantalla($mensaje){
        return  $this->conexion->enviarComando("<mensajePantalla impresora='$this->id'  ><![CDATA[$mensaje]]></mensajePantalla>");
     }
     public function textoNoFiscal($texto,$formato){
         return $this->conexion->enviarComando("<textoNoFiscal impresora='$this->id' formato='$formato' ><![CDATA[$texto]]></textoNoFiscal>");
     }
     public function cerrarNoFiscal(){
         return $this->conexion->enviarComando("<cerrarNoFiscal impresora='$this->id' />");
     }
     public function abrirGaveta(){
         return $this->conexion->enviarComando("<abrirGaveta impresora='$this->id' />");
     }
     public function setImpuesto($tipo1,$tasa1,$tipo2,$tasa2,$tipo3,$tasa3){
         return $this->conexion->enviarComando("<setImpuestos impresora='$this->id' tipo1='$tipo1' tasa1='$tasa1'  tipo2='$tipo2' tasa2='$tasa2' tipo3='$tipo3' tasa3='$tasa3' />");
     }
     public function setNombreMedio($medio,$nombre){
         return $this->conexion->enviarComando("<setNombreMedio impresora='$this->id' medio='$medio' nombre='$nombre' />");
     }
     public function setEcabezadoPie($texto,$linea){
         return $this->conexion->enviarComando("<setEncabezadoPie impresora='$this->id' linea='$linea'><![CDATA[$texto]]></setEncabezadoPie>");
     }
     
     public function guardarImpuesto(){
         return $this->conexion->enviarComando("<guardarImpuesto impresora='$this->id' />");
     }
     public function imprimirProgramacion(){
         return $this->conexion->enviarComando("<imprimirProgramacion impresora='$this->id' />");
     }

     public function getS1(){
         return $this->conexion->enviarComando("<s1 impresora='$this->id' />");
     }
     public function getS2(){
         return $this->conexion->enviarComando("<s2 impresora='$this->id' />");
     }
     public function getS3(){
         return $this->conexion->enviarComando("<s3 impresora='$this->id' />");
     }
     public function getS5(){
         return $this->conexion->enviarComando("<s5 impresora='$this->id' />");
     }

}
?>
