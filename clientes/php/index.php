
<?php
require_once 'Conexion.php';
require_once 'Impresora.php';
require_once 'ImpresoraRemota.php';
 echo "probando";
 $conexion= new Conexion();
 $conexion->conectar("localhost", 2086);
/*
try{
 $impresora =$conexion->getImpresora("BIXOLON");
 $impresora->abrirFactura();
 $impresora->imprimirEncabezado("R.I.F./C.I.:  V-17854158",1); // quitar segundo parametro de linea
 $impresora->imprimirEncabezado("Nombre:  Eduardo Daniel Marcovich",2); // quitar segundo parametro de linea
 $impresora->imprimirEncabezado("Direccion:  Av. Falsa con calle 123",3); // quitar segundo parametro de linea
 $impresora->registrarArticulo(FISCAL_TASA1,5.0,250.0,"Hab. Matrimonial");
 $impresora->imprimirComentario("Hab. 32");
 $impresora->registrarArticulo(FISCAL_TASA2,5.0,250.0,"Hab. Matrimonial");
 $impresora->imprimirComentario("Hab. 32");
// $impresora->subtotalizar();
 $conexion->enviarComando("<comandoDesco/>");//puede ser un monto en especifico o un porcentaje
 $impresora->pagar("01"); // El medio de pago va e 01 a 16 y estan configurados en la impresora
}catch(Exception $e){
	print "Error Connection: " . $e->getMessage(); 
	$impresora->anular();
}

*/
//$propiedad="item1";
//
//$objeto=new object();
//$objeto->$propiedad="valor;";
//print_r($objeto);
$impresora =$conexion->getImpresora("COM1");
//
//
// $impresora =$conexion->getImpresora("BIXOLON");
//$impresora->textoNoFiscal("Primera linea no fiscal", FMT_NORMAL);
//$impresora->abrirGaveta();
//$impresora->mostrarMensajePantalla("probando");
//echo "\n\n**ESTADO s1\n";
//print_r($impresora->getS1());
//echo "\n\n*echo*ESTADO s2\n";
//print_r($impresora->getS2());
//echo "\n\n**ESTADO s3\n";
//print_r($impresora->getS3());
//echo "\n\n**ESTADO s5\n";
//print_r($impresora->getS5());
//$impresora->imprimirReporteZ();
//$impresora->setImpuesto(2, 12.0, 2, 8.0, 2, 19.0);
//$impresora->guardarImpuesto();
//$impresora->imprimirProgramacion();

 $impresora->abrirFactura();
 $impresora->imprimirEncabezado("R.I.F./C.I.:  V-17854158"); // quitar segundo parametro de linea
 $impresora->imprimirEncabezado("Nombre:  Eduardo Daniel Marcovich"); // quitar segundo parametro de linea
 $impresora->registrarArticulo(FISCAL_EXENTO,5.0,250.0,"Hab. Matrimonial");
 $impresora->imprimirComentario("Hab. 32");
//
 $impresora->pagar("01");
// echo "\n Numero de factura ".$numeroFactura=$impresora->getUltimoNumeroFiscal()+"\n";
//// sleep(3);
//// $impresora->imprimirReporteZ();
// sleep(3);
// echo "\n Numero de Fiscal (ReporteZ)".$numeroFactura=$impresora->getUltimoNumeroFiscal()+"\n";
//  $impresora->imprimirReporteZ();
//  sleep(3);
//  $impresora->imprimirReporteX();
//  $impresora->imprimirReporteZ("01/01/2012","16/01/2012");
//  sleep(3);
//$impresora->setNombreMedio("01", "Pago Total");
//$impresora->setEcabezadoPie("Linea 1 encabezado", 1);
//$impresora->setEcabezadoPie("Linea 2 encabezado", 2);
//$impresora->setEcabezadoPie("Linea 1 pie", 91);
//$impresora->setEcabezadoPie("Linea 2 pie", 92);
// print_r($impresora->getReporteZ());
?>