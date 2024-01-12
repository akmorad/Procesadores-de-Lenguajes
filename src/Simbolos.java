public class Simbolos{
    private String nombre;
    private String tipo;
    private String tipoRet;
    private String tipoParam;
    private int ancho;
    private int pos;
    private int numLinea;
    private boolean esGlobal;

    public Simbolos(String regla, int numLinea, boolean esGlobal){
        this.nombre = regla;
        this.tipo= "vacio";
        this.tipoRet= "vacio";
        this.tipoParam = "vacio";
        this.ancho= 0;
        this.numLinea = numLinea;
        this.esGlobal = esGlobal;
    }

    public int getNumLinea(){
        return this.numLinea;
    }

    public String getTipoParametros(){
        return this.tipoParam;
    }

    public void setTipoParametros(String param){
        this.tipoParam = param;
    }

    public String getNombre(){
        return this.nombre;
    }

    public int getPos(){
        return this.pos;
    }

    public String getTipo(){
        return this.tipo;
    }

    public String getTipoRetorno(){
        return this.tipoRet;
    }

    public int getAncho(){
        return this.ancho;
    }

    public void setTipoRet(String tipoRet){
        this.tipoRet=tipoRet;
    }

    public void setTipo(String tipo){
        this.tipo=tipo;
    }

    public void setAncho(int ancho){
        this.ancho=ancho;
    }

    public void setPos(int pos){
        this.pos = pos;
    }

    public boolean getEsGlobal(){
        return this.esGlobal;
    }
}
