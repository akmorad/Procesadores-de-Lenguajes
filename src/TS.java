import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

public class TS {

        private Map<Integer, EntradaSimbolos> tabla = new HashMap<>();
        private Map<String, EntradaSimbolos> tablaAux = new HashMap<>();
        private int numElem;
        private int idTabla;
        private int desp;

        public TS(int idTabla, int numElem) {
            this.desp = 0;
            this.idTabla = idTabla;
            this.numElem = numElem;
        }

        public int getID(){
            return this.idTabla;
        }

        public String getName(int pos){
            return this.tabla.get(pos).getNombre();
        }

        public int getNumElem(){
            return this.numElem;
        }

        public int getDesplazamiento(){
            return this.desp;
        }

        public int insertarTS(EntradaSimbolos entry) {
            
            int pos = this.getNumElem();
        
            this.tabla.put(entry.getPos(), entry);

            this.tablaAux.put(entry.getNombre(), entry);

            this.numElem++;

            return pos;
        }

        public void insertarTSAtributos(int pos, String tipo, int desp, int numParam, String tipoParam, String tipoReturn, String etiq){
            
            if(this.tabla.containsKey(pos)){
                EntradaSimbolos entradaExistente = this.tabla.get(pos);
                if(tipo.equals("FUNCTION")){
                    entradaExistente.setTipo(tipo);
                    entradaExistente.setTipoParametros(tipoParam);
                    entradaExistente.setEtiqueta(etiq);
                    entradaExistente.setNumParametros(numParam);
                    entradaExistente.setTipoReturn(tipoReturn);
                } else {
                    entradaExistente.setTipo(tipo);
                    entradaExistente.setDesplazamiento(desp);
                }
                switch (tipo) {
                    case "INT":
                        this.desp+=1;
                        break;
                    case "STRING":
                        this.desp+=64;
                        break;
                    case "BOOLEAN":
                        this.desp+=1;
                        break;
                    default:
                        break;
                }
            } else {
                System.err.println("Error: Identificador no encontrado en ninguna TS");
            }
        }

        public void limpiarTabla() {
            tabla.clear();
            this.numElem = 0;
            this.idTabla ++;
        }

        public int buscarTS(String name) {

            return this.tablaAux.containsKey(name) ? this.tablaAux.get(name).getPos() : -1;
        }

        public String buscaTipoParametros(int pos){

            Integer aux = Integer.valueOf(pos);

            return this.tabla.containsKey(aux) ? this.tabla.get(aux).getTipoParametros() : "vacio";
        }

        public String buscaTipo(int pos){
            
            Integer aux = Integer.valueOf(pos);

            return this.tabla.containsKey(aux) ? this.tabla.get(aux).getTipo() : "vacio";
        }

        public String buscaTipoReturn(int pos){
            
            Integer aux = Integer.valueOf(pos);

            return this.tabla.containsKey(aux) ? this.tabla.get(aux).getTipoReturn() : "";
        }

        public String buscaTipoEtiqueta(int pos){
            
            Integer aux = Integer.valueOf(pos);

            return this.tabla.containsKey(aux) ? this.tabla.get(aux).getEtiqueta() : "";
        }

        public int buscaDesp(int pos){
            
            Integer aux = Integer.valueOf(pos);

            return this.tabla.containsKey(aux) ? this.tabla.get(aux).getDesplazamiento() : -1;
        }

        public int buscaNumParametros(int pos){
            
            Integer aux = Integer.valueOf(pos);

            return this.tabla.containsKey(aux) ? this.tabla.get(aux).getNumParametros() : -1;
        }

        public void ImprimirTS(FileWriter tokens) {

            try{
                tokens.write("TS # " + this.idTabla + " : \n" );

                Iterator<Integer> it = tabla.keySet().iterator();

                while (it.hasNext()){
                    EntradaSimbolos simbolo = tabla.get(it.next());

                    tokens.write(simbolo.escribir());

                    tokens.write("-------------------- \n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return getClass().getName() + "@" + Integer.toHexString(hashCode());
        }
    }
