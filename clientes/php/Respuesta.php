<?php
/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of Respuesta
 *
 * @author forest
 */
class Elemento{
      var $texto;
      public function __construct( $texto) {
          $this->texto=$texto;
      }
      function  __toString() {
        return (string)$this->texto;
    }

}
class Respuesta {
    //put your code here
    var $estado;
    var $mensaje;

    function getEstado(){
        return $this->estado;
    }
    function setEstado($estado){
        $this->estado=$estado;
    }

    function getMensaje(){
        return $this->mensaje;
    }
    function setMensaje($mensaje){
        $this->mensaje=$mensaje;
    }
}
?>
