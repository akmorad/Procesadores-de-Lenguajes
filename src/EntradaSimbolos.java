public class EntradaSimbolos {
        private String name;
        private String tipo;
        private int desplazamiento;
        private int numParametros;
        private String tipoParametros;
        private String tipoReturn;
        private String etiqueta;
        private int pos;
    
        public EntradaSimbolos(int pos, String name, String tipo, int desplazamiento, String tipoParametros, String tipoReturn, String etiqueta) {
            this.pos = pos;
            this.name = name;
            this.tipo = tipo;
            this.desplazamiento = desplazamiento;
            this.tipoParametros = tipoParametros;
            this.etiqueta = etiqueta;
        }
    
        public EntradaSimbolos(int pos, String name) {
            this.pos = pos;
            this.name = name;
        }
    
        public String getNombre(){
            return this.name;
        }
    
        public String getTipo() {
            return this.tipo;
        }
    
        public int getDesplazamiento() {
            return this.desplazamiento;
        }
    
        public String getTipoParametros() {
            return this.tipoParametros;
        }
    
        public int getNumParametros() {
            return this.numParametros;
        }
    
        public String getEtiqueta() {
            return this.etiqueta;
        }
    
        public String getTipoReturn(){
            return this.tipoReturn;
        }
    
        public int getPos(){
            return this.pos;
        }
    
        public String escribir(){
            String resultado= "   * LEXEMA : '"+ this.name + "'\n";
            
            if(this.tipo != null) {
                resultado = resultado + "     + tipo: '" + this.tipo +"'\n";

                if(this.tipo == "FUNCTION"){
                    resultado = resultado+ "     + numParam: " + this.numParametros +"\n";
                    resultado = resultado + "     + tipoParam: '" + this.tipoParametros +"'\n";
                    resultado = resultado + "     + tipoReturn: '" + this.tipoReturn +"'\n";
                    resultado = resultado + "     + etiqueta: '" + this.etiqueta +"'\n";
                }
                else{
                    resultado = resultado + "     + despl: " + this.desplazamiento +"\n";
                }
            }
            return resultado;
        }

        public void setName(String name) {
            this.name = name;
        }
    
        public void setTipo(String tipo) {
            this.tipo = tipo;
        }
    
        public void setDesplazamiento(int desplazamiento) {
            this.desplazamiento = desplazamiento;
        }
    
        public void setNumParametros(int numParametros) {
            this.numParametros = numParametros;
        }
    
        public void setTipoParametros(String tipoParametros) {
            this.tipoParametros = tipoParametros;
        }
    
        public void setTipoReturn(String tipoReturn) {
            this.tipoReturn = tipoReturn;
        }
    
        public void setEtiqueta(String etiqueta) {
            this.etiqueta = etiqueta;
        }
    
        public void setPos(int pos) {
            this.pos = pos;
        }
    }
