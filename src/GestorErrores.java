import java.io.FileWriter;
import java.io.IOException;

public class GestorErrores {

    private FileWriter writerError;

    public GestorErrores(FileWriter writerError){
        this.writerError = writerError;
    }

    public void gestionarError(int caso, String numLinea, String p2) throws IOException{

        switch (caso) {
            case 0:
                writerError.write("Caracter " + p2 + " no válido en la linea " + numLinea + "\n");
                break;
            case 1:
                writerError.write("Error: La variable " + p2 + " de la linea " + numLinea + " ya ha sido declarada\n");
                break;
            case 2:
                writerError.write("Error: Se esta usando la variable " + p2 +  " que no esta declarada previamente, en la linea " + numLinea + "\n");
                break;
            case 3:
                writerError.write("Número " + p2 + " de la linea " + numLinea + " fuera del rango permitido(32728)\n");
                break;
            case 4:
                writerError.write("Exceso de caracteres en la cadena " + '"' + p2 + '"' +  " de la linea " + numLinea + "\n");
                break;
            case 5:
                writerError.write("Error en el caracter: " + p2 + " en la linea " + numLinea + "\n");
                break;
            case 6:
                writerError.write("Error en el analisis sintactico\n");
                break;
            case 7:
                writerError.write("Error: El valor de retorno de la funcion " + p2 + ", en la linea " + numLinea + "\n");
                break;
            case 8:
                writerError.write("Error: " + p2 + " en la linea " + numLinea + "\n");              
            break;
            case 9:
                writerError.write("Error: " + p2 + " en la linea " + numLinea + "\n");
            break;
            case 10:
                writerError.write("Error: Se esperaba una variable de tipo BOOLEAN, pero se encontro una variable de tipo " + '"' + p2 + '"' + "\n");
            break;
            case 11:
                writerError.write("Error: Se debe llamar a la funcion de la linea " + numLinea + " con los siguientes parametros " + p2 + "\n");
            break;
            case 12:
                writerError.write("Error: Se ha encontrado un tipo de retorno " + p2 + " que no se esperaba en la linea " + numLinea + "\n");
            break;
            case 13:
                writerError.write("Error: " + p2 + " en la linea " + numLinea + "\n");
            break;
            case 14:
                writerError.write("Error: " + p2 + " en la linea " + numLinea + "\n");
                break;
            case 15:
                writerError.write(p2 + " en la linea " + numLinea + "\n");
                break;
            case 16:
                writerError.write("Error: La operacion ++ solo se puede realizar con tipos enteros, el tipo de la variable usada es " + p2 + " en la linea " + numLinea + "\n");
            break;
            case 17:
                writerError.write(p2 + " en la linea " + numLinea + "\n");
            break;
        }
    }
    
}
