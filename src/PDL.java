import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class PDL{

    private static String[] separadores = { ",", ";", "(", ")", "{", "}" };
    private static String[] palabrasReservadas = { "boolean", "function", "get", "if", "int", "let", "put", "return", "string", "void", "while" };
    private static String[] tokenSeparador = {"COMA", "PCOMA", "PIZQ", "PDRCH", "LLIZQ", "LLDRCH"};
    private static boolean zona_declarativa;
    private static TS TSG;
    private static TS TSF;
    private static TS TSActiva;
    private static int numTablas = 0;
    private static int numParam = 0;
    private static int numLinea = 1;
    private static boolean esGlobal = true;

    private static int currentChar= -9;

    private static BufferedReader reader = null;
    private static GestorErrores gE= null;
    private static FileWriter writerTokens = null;
    private static FileWriter writerError = null;
    private static FileWriter writerTS = null;
    private static FileWriter parse = null;

    private static Stack<Simbolos> AUX = new Stack<Simbolos>();
    
    private static Stack<Simbolos> pila = new Stack<Simbolos>();
    private static String terminales[] = {"!", "(", ")", "+", "++", ",", ";", "=", "==", "boolean", "cadena", "ent", "function", "get", "id", "if", "int", "let", "put", "return", "string", "void", "while", "{","}", "$"};
    private static Map<String, int[]> tabla = new HashMap<String, int[]>();
    private static String[] drchReglas = {
        "F P {28} {28}",
        "B P {28} {28}",
        "lambda {2}",
        "{3} function id H {4} ( A ) {5} { C } {6}",
        "R id K {7}",
        "void {28}",
        ", R id K {7}",
        "lambda",
        "B C {8}",
        "lambda ",
        "R {9}",
        "void",
        "if ( E ) S {10}",
        "{3} let id R {11} ; {28}",
        "S {9}",
        "while ( E ) { C } {10}",
        "int {12} {13}",
        "boolean {14} {15}",
        "string {16} {17}",
        "id Z {18}",
        "put E ; {9}",
        "get id ; {19}",
        "return X ; {20}",
        "= E ; {9}",
        "( L ) ; {9}",
        "E Q {21}",
        "lambda",
        ", E Q {21}",
        "lambda",
        "E {9}",
        "lambda",
        "N M {22}",
        "== N M {23}",
        "lambda",
        "T G {24}",
        "+ T G {24}",
        "lambda",
        "J D {25}",
        "! J {26}",
        "id W {27}",
        "( E ) {9}",
        "ent {12}",
        "cadena {16}",
        "( L ) {9}",
        "lambda",
        "++ {29}",
        "lambda",
        "D ; {9}"
    };

    private static int indice(String str){
        int i = 0;

        for(; i < terminales.length; i++){
            if(terminales[i].equals(str)){return i;}
        }

        return i;
    }

    private static void init(){
        int[] A = {-1,-1,-1,-1,-1,-1,-1,-1,-1, 4,-1,-1,-1,-1,-1,-1, 4,-1,-1,-1, 4, 5,-1,-1,-1,-1};
        tabla.put("A", A);
        int[] B = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,14,14,12,-1,13,14,14,-1,-1,15,-1,-1,-1};
        tabla.put("B", B);
        int[] C = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1, 8, 8, 8,-1, 8, 8, 8,-1,-1, 8,-1, 9,-1};
        tabla.put("C", C);
        int[] D = {-1,-1,46,46,45,46,46,-1,46,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        tabla.put("D", D);
        int[] E = {31,31,-1,-1,-1,-1,-1,-1,-1,-1,31,31,-1,-1,31,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        tabla.put("E", E);
        int[] F = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1, 3,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        tabla.put("F", F);
        int[] G = {-1,-1,36,35,-1,36,36,-1,36,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        tabla.put("G", G);
        int[] H = {-1,-1,-1,-1,-1,-1,-1,-1,-1,10,-1,-1,-1,-1,-1,-1,10,-1,-1,-1,10,11,-1,-1,-1,-1};
        tabla.put("H", H);
        int[] J = {-1,40,-1,-1,-1,-1,-1,-1,-1,-1,42,41,-1,-1,39,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        tabla.put("J", J);
        int[] K = {-1,-1, 7,-1,-1, 6,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        tabla.put("K", K);
        int[] L = {25,25,26,-1,-1,-1,-1,-1,-1,-1,25,25,-1,-1,25,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        tabla.put("L", L);
        int[] M = {-1,-1,33,-1,-1,33,33,-1,32,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        tabla.put("M", M);
        int[] N = {34,34,-1,-1,-1,-1,-1,-1,-1,-1,34,34,-1,-1,34,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        tabla.put("N", N);
        int[] P = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,0,1,1,1,-1,1,1,1,-1,-1,1,-1,-1,2};
        tabla.put("P", P);
        int[] Q = {-1,-1,28,-1,-1,27,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        tabla.put("Q", Q);
        int[] R = {-1,-1,-1,-1,-1,-1,-1,-1,-1,17,-1,-1,-1,-1,-1,-1,16,-1,-1,-1,18,-1,-1,-1,-1,-1};
        tabla.put("R", R);
        int[] S = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,21,19,-1,-1,-1,20,22,-1,-1,-1,-1,-1,-1};
        tabla.put("S", S);
        int[] T = {38,37,-1,-1,-1,-1,-1,-1,-1,-1,37,37,-1,-1,37,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        tabla.put("T", T);
        int[] W = {-1,43,44,44,44,44,44,-1,44,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        tabla.put("W", W);
        int[] X = {29,29,-1,-1,-1,-1,30,-1,-1,-1,29,29,-1,-1,29,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        tabla.put("X", X);
        int[] Z = {-1,24,-1,-1,47,-1,47,23,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        tabla.put("Z", Z);

        pila.push(new Simbolos("P", numLinea, esGlobal));
        pila.push(new Simbolos("{1}", numLinea, esGlobal));
    }

    private static String codigo (String token){
        if(token.charAt(0)=='$')return token;
        int i=1;
        String res="";
        while( token.charAt(i) != ','){
            res+= token.charAt(i++);
        }
        String[] arr= {"LOGICO","PIZQ","PDRCH","OPBINARIO", "OPUNOARIO", "COMA", "PCOMA","ASIGNACION","RELACIONAL","BOOLEAN","CADENA","ENTERO","FUNCTION","GET","ID","IF","INT","LET","PUT","RETURN","STRING","VOID","WHILE","LLIZQ", "LLDRCH"};
        boolean encontrado=false;
        for(i=0; i<arr.length && !encontrado ;i++){
            encontrado= res.equals(arr[i]);
        }
        return terminales[i-1];
    }

    private static int idPos(String token){
        String res=""; 
        for (int i=5; token.charAt(i)!='>';i++){
            res+=token.charAt(i);
        }
        return Integer.valueOf(res);
    }

    private static void sintactico(FileWriter writerError, FileWriter parse){
        try{
            String token=analisisLexico();
            String a= null;
            int indice;
            int[] array;

            while(!pila.empty()) {
                a= codigo(token);
                array= tabla.get(pila.peek().getNombre());
                indice= indice(a);
                if (pila.peek().getNombre().equals(a)) {
                    if(pila.peek().getNombre().equals("id")){
                        pila.peek().setPos(idPos(token));
                    }
                    AUX.push(pila.pop());
                    token=analisisLexico();
                } else if(array != null && array[indice] != -1) {
                    AUX.push(pila.pop());
                    String[] regla = drchReglas[array[indice]].split(" ");
                    for(int i = regla.length-1; i >= 0; i--){
                        if(!regla[i].equals("lambda")){
                            pila.push(new Simbolos(regla[i], numLinea, esGlobal));
                        }
                    }
                    parse.write(String.valueOf(array[indice] + 1) + " ");
                } else if(pila.peek().getNombre().charAt(0)=='{'){
                    ejecutarAccionSemantica(pila.pop());
                } else {
                    gE.gestionarError(5,String.valueOf(numLinea),a);
                    return ;
                }
            }
            if(token != "$" || !AUX.peek().getNombre().equals("P")) {
                gE.gestionarError(6,"","");
            }
            return ;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ;
    }

    private static void numPops(int num){
        for(int i = 0; i<num; i++){
            AUX.pop();
        }
    }

    private static void ejecutarAccionSemantica(Simbolos simbolo) throws IOException{
        Simbolos s, aux, parentI, parentD, aux2;
        String res;
        switch (simbolo.getNombre()) {
            case "{1}":
                TSG = new TS(0, 0);
                TSActiva = TSG;
                zona_declarativa = false;
            break;
            case "{2}":
                TSG.ImprimirTS(writerTS);
                TSG.limpiarTabla();
            break;
            case "{3}":
                zona_declarativa = true;
            break;
            case "{4}":
                numTablas+=1;
                TSF = new TS(numTablas, 0);
                TSActiva = TSF;
                s = AUX.pop(); //Tiene H o void
                if(s.getNombre().equals("void")){s = AUX.pop(); s.setTipo("VOID");}
                aux = AUX.pop(); //tiene el id
                res = "etiq_" + TSG.getName(aux.getPos());
                TSG.insertarTSAtributos(aux.getPos(), "FUNCTION", 0, 0, "", s.getTipo(), res);
                AUX.push(aux);
                AUX.push(s);
            break;
            case "{5}":
                parentD = AUX.pop();
                s = AUX.pop(); //Tiene A
                parentI = AUX.pop();
                aux2 = AUX.pop(); //Tiene H
                aux = AUX.pop(); //Tiene el id
                res = "etiq_" + TSG.getName(aux.getPos());
                TSG.insertarTSAtributos(aux.getPos(), "FUNCTION", 0, numParam, s.getTipo(), aux2.getTipo(), res);
                numParam = 0;
                zona_declarativa = false;
                AUX.push(aux);
                AUX.push(aux2);
                AUX.push(parentI);
                AUX.push(s);
                AUX.push(parentD);
            break;
            case "{6}":
                AUX.pop(); //Sacamos "}"
                s = AUX.pop(); //Tiene C
                numPops(4);
                aux2 = AUX.pop(); // Sacamos la H
                aux = AUX.pop(); //Sacamos id
                AUX.pop(); //Sacamos function
                if(!aux2.getTipo().equals(s.getTipoRetorno()) && !s.getTipoRetorno().equals("vacio")){
                    gE.gestionarError(7, Integer.toString(s.getNumLinea()), (s.getTipoRetorno()+ " no coincide con el valor esperado " + aux2.getTipo()));
                } else {
                    TSF.ImprimirTS(writerTS);
                }
                TSF.limpiarTabla();
                TSActiva = TSG;
            break;
            case "{7}":
                aux2 = AUX.pop(); //Pilla la K
                aux = AUX.pop(); //Pilla el id
                s = AUX.pop(); //Pilla la R
                TSActiva.insertarTSAtributos(aux.getPos(), s.getTipo(), TSActiva.getDesplazamiento(), 0, "", "", "");
                numParam++;
                if(AUX.peek().getNombre().equals(",")){AUX.pop();}
                if(!aux2.getTipo().equals("vacio")){res = s.getTipo() + "x" + aux2.getTipo(); AUX.peek().setTipo(res);}
                else{AUX.peek().setTipo(s.getTipo());}
            break;
            case "{8}":
            s = AUX.pop(); //Tiene C
            aux = AUX.pop(); //Tiene B
            if(aux.getTipo().equals("tipo_ok")){
                if(s.getTipoRetorno().equals(aux.getTipoRetorno())) { AUX.peek().setTipoRet(aux.getTipoRetorno());}
                    else{
                        if(s.getTipoRetorno().equals("vacio")){AUX.peek().setTipoRet(aux.getTipoRetorno());}
                        else{
                            if(aux.getTipoRetorno().equals("vacio")){AUX.peek().setTipoRet(s.getTipoRetorno());}
                            else{AUX.peek().setTipoRet("tipo_error");}
                        }
                    }
                AUX.peek().setTipo(aux.getTipo());
            }
            else{AUX.peek().setTipo("tipo_error");}
            break;
            case "{9}":
                while(indice(AUX.peek().getNombre())!=26){
                    AUX.pop();
                }
                String ret= AUX.peek().getTipoRetorno();
                res=AUX.pop().getTipo();
                while(indice(AUX.peek().getNombre())!=26){
                    AUX.pop();
                }
                AUX.peek().setTipoRet(ret);
                AUX.peek().setTipo(res);
            break;
            case "{10}":
                if (AUX.peek().getNombre().equals("}")) AUX.pop();
                s = AUX.pop(); //Tiene la C
                if (AUX.peek().getNombre().equals("{")) AUX.pop();
                AUX.pop();
                aux = AUX.pop(); //Sacamos E
                numPops(2);
                if(aux.getTipo().equals("BOOLEAN")){
                    AUX.peek().setTipoRet(s.getTipoRetorno());;
                    AUX.peek().setTipo(s.getTipo());
                } else{
                    AUX.peek().setTipo("tipo_error"); 
                    gE.gestionarError(15, String.valueOf(aux.getNumLinea()), ("Error: Se esperaba que el tipo de la condicion fuese BOOLEANO, pero se encontro " + aux.getTipo()));
                }
            break;
            case "{11}":
                s = AUX.pop(); //Saca R
                int pos = AUX.pop().getPos(); //Sacas id pos
                TSActiva.insertarTSAtributos(pos, s.getTipo(), TSActiva.getDesplazamiento(), 0, "", "", "");
                AUX.pop(); //Sacas let
                zona_declarativa = false;
            break;
            case "{12}":
                AUX.pop();
                AUX.peek().setTipo("INT");
            break;
            case "{13}":
                AUX.peek().setAncho(1);
            break;
            case "{14}":
                AUX.pop();
                AUX.peek().setTipo("BOOLEAN");
            break;
            case "{15}":
                AUX.peek().setAncho(1);
            break;
            case "{16}":
                AUX.pop();
                AUX.peek().setTipo("STRING");
            break;
            case "{17}":
                AUX.peek().setAncho(64);
            break;
            case "{18}":
                String tipoZ= AUX.pop().getTipo();
                aux = AUX.pop();
                int idPos = aux.getPos();
                if(idPos != -1){
                    if(!TSG.buscaTipo(idPos).equals("FUNCTION")){
                        if(tipoZ.equals("tipo_ok")){
                            if(!aux.getEsGlobal()){
                                if(TSActiva.buscaTipo(idPos).equals("INT")){res = "tipo_ok";}
                                else{res = "tipo_error"; gE.gestionarError(16, String.valueOf(aux.getNumLinea()), TSActiva.buscaTipo(idPos));}
                            } else {
                                if(TSG.buscaTipo(idPos).equals("INT")){res = "tipo_ok";}
                                else{res = "tipo_error"; gE.gestionarError(16, String.valueOf(aux.getNumLinea()), TSG.buscaTipo(idPos));}
                            }
                        } else {
                            if(!aux.getEsGlobal()){
                                if(TSActiva.buscaTipo(idPos).equals(tipoZ)){ res="tipo_ok";}
                                else {res="tipo_error"; gE.gestionarError(13, String.valueOf(aux.getNumLinea()), ("El valor del tipo del identificador " + '"' + TSActiva.getName(idPos) + '"' + " difiere con el valor asignado " + '"' + tipoZ + '"' + " se esperaba un tipo " + '"' + TSActiva.buscaTipo(idPos) + '"'));}
                            } else {
                                if(TSG.buscaTipo(idPos).equals(tipoZ)){res="tipo_ok";}
                                else{res="tipo_error"; gE.gestionarError(13, String.valueOf(aux.getNumLinea()), ("El valor del tipo del identificador " + '"' + TSG.getName(idPos) + '"' + " difiere con el valor asignado " + '"' + tipoZ + '"' + " se esperaba un tipo " + '"' + TSG.buscaTipo(idPos) + '"'));}
                            }
                        }
                    } else if(tipoZ.equals(TSG.buscaTipoParametros(idPos))){ res="tipo_ok";
                    } else {res="tipo_error"; gE.gestionarError(14, String.valueOf(aux.getNumLinea()), ("El tipo de parametros del identificador difiere con el valor asignado " + '"' + tipoZ + '"' + " se esperaba un tipo de parametros " + '"' + TSG.buscaTipoParametros(idPos) + '"'));}
                } else {res = "tipo_error";}
                AUX.peek().setTipo(res);
            break;
            case "{19}":
                AUX.pop();
                res=TSActiva.buscaTipo(AUX.pop().getPos());
                AUX.pop();
                AUX.peek().setTipo(res);
            break;
            case "{20}":
                AUX.pop();
                aux = AUX.pop();
                res= aux.getTipo(); //Tiene X
                AUX.pop(); //Quitamos return
                AUX.peek().setTipoRet(res); //Establece el tipoRet
                if(TSActiva.equals(TSF)){
                    if(!res.equals("tipo_error")){ AUX.peek().setTipo("tipo_ok");}
                    else{ AUX.peek().setTipo("tipo_error"); gE.gestionarError(12, String.valueOf(aux.getNumLinea()), res);}
                } else {
                    AUX.peek().setTipo("tipo_error");
                    gE.gestionarError(17, String.valueOf(aux.getNumLinea()), ("Error: Se ha encontrado un return fuera de una funcion"));
                }
                
            break;
            case "{21}":
                res= AUX.pop().getTipo();
                if(!res.equals("vacio")) res= AUX.pop().getTipo() +"x"+res;
                else res= AUX.pop().getTipo();
                if(AUX.peek().getNombre().equals(",")) AUX.pop();
                AUX.peek().setTipo(res);
            break;
            case "{22}":
                aux = AUX.pop();
                aux2 = AUX.pop();
                String mTipo= aux.getTipo();
                String nTipo= aux2.getTipo();
                if(!mTipo.equals("vacio")) {
                    if(mTipo.equals(nTipo)){res="BOOLEAN"; }
                    else { 
                        res="tipo_error"; 
                        mTipo = !mTipo.equals("") ? mTipo : "vacio";
                        gE.gestionarError(8, String.valueOf(aux.getNumLinea()), ("Se estan comparando dos variables con distintos tipos " + nTipo + ", " + mTipo));
                    }
                } else { res= nTipo; }
                AUX.peek().setTipo(res);
            break;
            case "{23}":
                aux = AUX.pop();
                String mtipo=aux.getTipo();
                if( mtipo.equals("vacio") || mtipo.equals(AUX.peek().getTipo()) ) {res= AUX.peek().getTipo();}
                else{   
                    res= "tipo_error"; 
                    mtipo = !mtipo.equals("") ? mtipo : "vacio";
                    gE.gestionarError(8, String.valueOf(aux.getNumLinea()), ("Se estan comparando dos variables con distintos tipos " + AUX.peek().getTipo() + ", " + mtipo));
                }
                numPops(2);
                AUX.peek().setTipo(res);
            break;
            case "{24}":
                aux = AUX.pop();
                aux2 = AUX.pop();
                String tipo2= aux.getTipo(); //Tiene el tipo de G
                String tipo1= aux2.getTipo(); //Tiene el tipo de T
                if(!tipo2.equals("vacio")){
                    if(tipo1.equals("INT") && tipo2.equals("INT")){ res = tipo1; }
                    else { res= "tipo_error"; gE.gestionarError(9, String.valueOf(aux.getNumLinea()), ("Se esperaba dos variables del tipo INT, pero se encontro  " + tipo1 + ", " + tipo2));}
                } else { res= tipo1; }
                if(AUX.peek().getNombre().equals("+")){ AUX.pop(); }
                AUX.peek().setTipo(res);
            break;
            case "{25}":
                aux = AUX.pop();
                if(aux.getTipo().equals("tipo_ok") && !AUX.peek().getTipo().equals("INT") ){ res="tipo_error"; AUX.pop(); gE.gestionarError(16, String.valueOf(numLinea), aux.getTipo());}
                else res = AUX.pop().getTipo();
                AUX.peek().setTipo(res);
            break;
            case "{26}":
                if(AUX.peek().getTipo().equals("BOOLEAN")){
                    res= AUX.peek().getTipo();
                } else { res = "tipo_error"; gE.gestionarError(10, String.valueOf(AUX.peek().getNumLinea()), AUX.peek().getTipo());}
                AUX.pop();
                AUX.pop();
                AUX.peek().setTipo(res);
            break;
            case "{27}":
                aux = AUX.pop(); //Tiene W
                if(aux.getTipo().equals(TSG.buscaTipoParametros(AUX.peek().getPos())) && TSG.buscaTipo(AUX.peek().getPos()).equals("FUNCTION") ){
                    res = TSG.buscaTipoReturn(AUX.peek().getPos());
                } else if(aux.getTipo().equals("vacio")){
                    if(AUX.peek().getEsGlobal()){
                        res = TSG.buscaTipo(AUX.peek().getPos());
                    } else {
                        res = TSActiva.buscaTipo(AUX.peek().getPos());
                    }
                } else {
                    res="tipo_error"; gE.gestionarError(11, String.valueOf(aux.getNumLinea()), TSG.buscaTipoParametros(AUX.peek().getPos()));
                }
                AUX.pop();
                AUX.peek().setTipo(res);
            break;
            case "{28}":
                AUX.pop();
            break;
            case "{29}":
               AUX.pop();
               AUX.peek().setTipo("tipo_ok"); 
            break;
            default:
                break;
        }
    }
    
    /*private static void pilas(){
            Stack<Simbolos> pila2 = new Stack<Simbolos>();
            System.out.println("PILA----------------");
            while(!pila.empty()){
                System.out.println(pila.peek().getNombre());
                pila2.push(pila.pop());
            }
            while(!pila2.empty()){
                pila.push(pila2.pop());
            }
            System.out.println("AUX----------------");
            while(!AUX.empty()){
                System.out.println(AUX.peek().getNombre());
                pila2.push(AUX.pop());
            }
            while(!pila2.empty()){
                AUX.push(pila2.pop());
            }
    }*/
    
    public static void main(String[] args ){
        if (args.length != 1) {
            System.err.println("Debe proporcionar un archivo como argumento.\n");
            System.exit(-1);
        }

        try {
            reader = new BufferedReader(new FileReader(args[0]));
            writerTokens = new FileWriter("tokens.txt");
            writerError = new FileWriter("errores.txt");
            writerTS = new FileWriter("TS.txt");
            parse = new FileWriter("parse.txt");
            gE= new GestorErrores(writerError);

            numLinea = 1;
            parse.write("Descendente ");
            init();
            sintactico(writerError, parse);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) reader.close();
                if (writerTokens != null) writerTokens.close();
                if (writerError != null) writerError.close();
                if (writerTS != null) writerTS.close();
                if(parse != null) parse.close();
            } catch
                (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static String analisisLexico() {
        try{
            String cadena = "";
            int estado = 0;
            int valor = 0;
            int contador = 0;
            String token=null;
            if (currentChar == -9) currentChar = reader.read();

            //Loop hasta que lleguemos al final del fichero
            while (token==null) {

                //cast de int -- > char
                char c = (char) currentChar;

                if(c == '\n'){numLinea++;}
                
                if( currentChar == -1 ) {token="$"; estado= 9;}
                
                switch (estado){
                    //Estado inicial
                    case 0:
                        int i;

                        //Si es separador nos quedamos en este estado y generamos token
                        if ((i = esSeparador(c)) < 6) { 
                            token= genToken(tokenSeparador[i], "", writerTokens, writerError, parse, numLinea);
                            currentChar = reader.read();
                        } else if (c == '=') { //si es =, puede ser token asignacion o token relacional
                            currentChar = reader.read();
                            estado = 6;
                        } else if (c == '+') { //si es +, puede ser token op unoario o op binario
                            currentChar = reader.read();
                            estado = 7;
                        } else if (c == '\"') { //si es ", es el iniciio de una cadena
                            currentChar = reader.read();
                            estado = 8;
                        } else if (Character.isDigit(c)) { //si es un digito, es el inicio de una constante entera
                            valor = Integer.parseInt(String.valueOf(c));
                            currentChar = reader.read();
                            estado = 5;
                        } else if (Character.isLetter(c) || c == '_') { //si es un caracter, puede ser o palabra reservada o variable
                            cadena = cadena + c;
                            currentChar = reader.read();
                            estado = 4;
                        } else if (c == '/') { //si es una /, es el inicio de un comentario
                            estado = 1;
                            currentChar = reader.read();
                        } else if (esDelimitador(c)) { //si es delimitador, nos quedamos en este estado hasta leer otro caracter distinto
                            currentChar = reader.read();
                        } else if(c == '!'){
                            token= genToken("LOGICO", "negacion", writerTokens, writerError, parse, numLinea);
                            currentChar = reader.read(); 
                        } else { //si no es nada de lo antarior ERROR
                            gE.gestionarError(0,String.valueOf(numLinea),String.valueOf(c));
                            currentChar = reader.read();
                        }
                        break;

                    case 1: //Estado para comentarios 
                        if (c == '*') {
                            estado = 2;
                            currentChar = reader.read();
                        } else if (c == '/'){
                            gE.gestionarError(0,String.valueOf(numLinea),String.valueOf(c));
                            currentChar = reader.read();
                            estado = 0;
                        }
                        break;

                    case 2: //Estado para comentarios 
                        if (c == '*') {
                            estado = 3;
                        } 
                        currentChar = reader.read();
                        break;

                    case 3: //Estado para comentarios 
                        if (c == '/') {
                            estado = 0;
                        } else if (c == '*') {
                            estado = 3;
                        } else {
                            estado = 2;
                        }
                        currentChar = reader.read();
                        break;

                    case 4: //Estado para palabra reservada o variable 
                        if (Character.isLetterOrDigit(c) || c == '_') { //segumos leyendo hasta que llegue algo distinto de caracter, numero o _
                            cadena = cadena + c;
                            currentChar = reader.read();
                        } else {
                            int p = 0;

                                if((i = esPalabraReservada(cadena)) < 11){

                                    token= genToken(palabrasReservadas[i].toUpperCase(),"", writerTokens, writerError, parse, numLinea);

                                } else {
                                    if(zona_declarativa){
                                        p = TSActiva.buscarTS(cadena);                                        
                                        if(p != -1){
                                            gE.gestionarError(1,String.valueOf(numLinea),cadena);
                                            token = genToken("ID", String.valueOf(p), writerTokens, writerError, parse, numLinea);
                                        } else {
                                            TSActiva.insertarTS(new EntradaSimbolos(TSActiva.getNumElem(), cadena));
                                            token = genToken("ID", String.valueOf(TSActiva.getNumElem()-1), writerTokens, writerError, parse, numLinea);
                                        }
                                    } else {
                                        p = TSActiva.buscarTS(cadena);
                                        if(p == -1){
                                            p= TSG.buscarTS(cadena);
                                            if( p== -1){
                                                gE.gestionarError(2,String.valueOf(numLinea),cadena);
                                            }
                                            esGlobal = true;
                                            token=genToken("ID", String.valueOf(p), writerTokens, writerError, parse, numLinea);
                                        } else{if(!TSActiva.equals(TSG)){esGlobal = false;}else{esGlobal = true;} token=genToken("ID", String.valueOf(p), writerTokens, writerError, parse, numLinea);}                                  
                                    }
                                }
                            cadena = "";
                            estado = 0;
                        }
                        break;

                    case 5: //estado para digitos
                        if (Character.isDigit(c)) { //Concatenamos caracteres hasta que leeamos otra cosa que no sea digito    
                            valor = valor*10 + Integer.parseInt(String.valueOf(c));
                            currentChar = reader.read();
                        } else if (valor < 32768) { //Cuando leemos otra cosa que no sea digito, calculamos el valor entero de la cadena
                            token=genToken("ENTERO", Integer.toString(valor), writerTokens, writerError, parse, numLinea); 
                            valor = 0;
                            estado = 0;
                        } else { //ERROR
                            gE.gestionarError(3,String.valueOf(numLinea),Integer.toString(valor));
                            token=genToken("ENTERO", Integer.toString(valor), writerTokens, writerError, parse, numLinea); 
                            valor = 0;
                            estado = 0;
                        }
                        break;

                    case 6: //estado asignacion o relacional
                        if (c == '=') {
                            token=genToken("RELACIONAL", "equal", writerTokens, writerError, parse, numLinea);
                            currentChar = reader.read();
                        } else {
                            token=genToken("ASIGNACION", "asign", writerTokens, writerError, parse, numLinea);
                        }
                        estado = 0;
                        break;

                    case 7: //estado op unoario y op binario
                        if (c == '+') {
                            token=genToken("OPUNOARIO", "inc", writerTokens, writerError, parse, numLinea);
                            currentChar = reader.read();
                        } else {
                            token=genToken("OPBINARIO", "suma", writerTokens, writerError, parse, numLinea);
                        }
                        estado = 0;
                        break;

                    case 8: //estado cadena de caracteres
                        if (c == '\"') {
                            if (contador <= 64) {
                                token=genToken("CADENA",('"'+cadena+'"'), writerTokens, writerError, parse, numLinea);
                                currentChar = reader.read();
                            } else {
                                gE.gestionarError(4,String.valueOf(numLinea),cadena);
                                token=genToken("CADENA",('"'+cadena+'"'), writerTokens, writerError, parse, numLinea);
                                currentChar = reader.read();
                            }
                            cadena = "";
                            estado = 0;
                            contador=0;
                        } else {
                            cadena = cadena + c;
                            currentChar = reader.read();
                            contador++;
                        }
                    break;
                }
            }
        return token;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    //Metodo para comprobar si es separador
    private static int esSeparador(char lex) {

        String sep = String.valueOf(lex);

        for (int i = 0; i < separadores.length; i++) {
            if (separadores[i].equals(sep)) {
                return i;
            }
        }

        return separadores.length;
    }

    //Metodo para comprobar si es palabra reservada
    private static int esPalabraReservada(String lex) {

        for (int i = 0; i < palabrasReservadas.length; i++) {
            if (palabrasReservadas[i].equals(lex)) {
                return i;
            }
        }

        return palabrasReservadas.length;
    }

    //Metodo para generar Token Delimitador
    private static boolean esDelimitador(char lexema) {

    boolean esDelimitador = false;
    char[] delimitadores = { ' ', '\n', '\t', '\r'};

    for (int i = 0; i < delimitadores.length && !esDelimitador; i++) {
        esDelimitador = delimitadores[i]==lexema;
    }

    return esDelimitador;
    }

    private static String genToken (String codigo, String atributo, FileWriter f, FileWriter writerError, FileWriter parse, int numLinea) throws IOException {
        String token= ("<"+codigo + ", "+atributo+">\n");
        writerTokens.write(token);
        return token;
        //sintactico(token, writerError, parse, numLinea);
    }
}