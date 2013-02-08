<?php
/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of Servidor
 *
 * @author forest
 */
require_once 'Respuesta.php';
require_once 'Impresora.php';
class Conexion {
    //put your code here
    var $procesado=false;
    var $socket;
    var $respuesta;
    var $parser;
    var $pila;
    var $pilaElemento;


    

    
    public function __construct(){
        $this->parser=xml_parser_create();
        $this->pila=array();
        $this->pilaElemento=array();
    }
    function conectar($maquina,$puerto){
        $this->socket= socket_create( AF_INET, SOCK_STREAM , SOL_TCP );
        if(socket_connect($this->socket, $maquina,$puerto)){
//            echo "\n**conectando\n";
        }
        socket_set_block($this->socket);

        xml_set_element_handler($this->parser, array($this,'elemento_iniciado'), array($this,'elemento_finalizado'));
        xml_set_character_data_handler($this->parser, array($this,'texto'));
        xml_parser_set_option($this->parser, XML_OPTION_CASE_FOLDING,false);
        $this->enviarComando("<sesion>\n");
    }

    function  enviarComando($comando,$esperar=1){
//        echo "cadena ".utf8_encode($comando)."\n";
        socket_write($this->socket, utf8_encode($comando))."\n";
        unset($this->respuesta);
        $this->procesado=false;

//        echo "esperar :$esperar\n";
//        $this->respuesta= new Respuesta();
        while($esperar&&!$this->procesado){
            $linea =socket_read($this->socket,6000,PHP_NORMAL_READ);
            if($err= socket_last_error()){
                error_log(socket_strerror($err)."\n");
                break;
            }

//            echo "datos en crudo :".$linea;
            if($linea){
//                echo "datos en crudo :".$linea;
                xml_parse($this->parser, $linea);
            }
            if($this->procesado){
//                echo "saliendo $this->procesado\n";
            }

        }
        if(substr($this->respuesta->getEstado(),0,1)=='4'){
                throw new Exception($this->respuesta->getMensaje(),$this->respuesta->getEstado());
        }
        return $this->respuesta;
    }

    function  getImpresora($nombre) {
        $impresora= new ImpresoraRemota($this);
        $impresora->setID($nombre);
        return $impresora;
    }
    function getImpresoras(){

    }

    function elemento_iniciado($parser ,$name , $attribs){
        if(strcasecmp($name,"RESPUESTA")==0){
            $this->respuesta= new Respuesta();
            $this->respuesta->setEstado($attribs["estado"]);
            $this->respuesta->setMensaje($attribs["mensaje"]);
            array_push($this->pilaElemento,$this->respuesta);
        }else{

            $elemento=(end($this->pilaElemento));
            $elemento->$name=new Elemento("");
//            echo "iniciando $name=".$this->respuesta->$name."\n";
            array_push($this->pila,$name);
            array_push($this->pilaElemento, $elemento->$name);
        }
    }

    function texto($parser ,$texto){
        $name=end($this->pila);
        $elemento=(end($this->pilaElemento));
//        if(isset ($elemento->$name)){
        if(!($elemento instanceof Respuesta)){
            $elemento->texto=$elemento->texto.$texto;
        }
//        }
//        echo $texto."\n";
        if(!($elemento instanceof Respuesta)){
            print_r($elemento);
        }
    }

    function elemento_finalizado($parser ,$name ){
        if(strcasecmp($name,"RESPUESTA")==0){
            $this->procesado=true;
//            echo "fin $name\n";
        }else{
             if(strcasecmp($name,end($this->pila))==0){
                 array_pop($this->pila);
                 array_pop($this->pilaElemento);
//                 echo "sacando de la pila $name";
             }
        }
//        echo "-$name-\n";
//        echo "procesado ".$this->procesado." \n ";
    }
    
}
?>
