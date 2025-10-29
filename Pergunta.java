public class Pergunta {
    private int id;
    private int ilhaId;
    private String texto;
    private String tipo;
    
    public Pergunta() {}
    
    public Pergunta(int ilhaId, String texto, String tipo) {
        this.ilhaId = ilhaId;
        this.texto = texto;
        this.tipo = tipo;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getIlhaId() { return ilhaId; }
    public void setIlhaId(int ilhaId) { this.ilhaId = ilhaId; }
    
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}