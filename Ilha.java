public class Ilha {
    private int id;
    private String nome;
    private String tema;
    private String descricao;
    private boolean descoberta;
    private int nivelRequerido;
    
    public Ilha() {}
    
    public Ilha(String nome, String tema, String descricao, int nivelRequerido) {
        this.nome = nome;
        this.tema = tema;
        this.descricao = descricao;
        this.nivelRequerido = nivelRequerido;
        this.descoberta = false;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public boolean isDescoberta() { return descoberta; }
    public void setDescoberta(boolean descoberta) { this.descoberta = descoberta; }
    
    public int getNivelRequerido() { return nivelRequerido; }
    public void setNivelRequerido(int nivelRequerido) { 
        this.nivelRequerido = nivelRequerido; 
    }
}