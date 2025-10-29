public class Jogador {
    private int id;
    private String nome;
    private int nivelAutoconhecimento;
    private int ilhasDescobertas;
    
    public Jogador() {}
    
    public Jogador(String nome) {
        this.nome = nome;
        this.nivelAutoconhecimento = 0;
        this.ilhasDescobertas = 0;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public int getNivelAutoconhecimento() { return nivelAutoconhecimento; }
    public void setNivelAutoconhecimento(int nivelAutoconhecimento) { 
        this.nivelAutoconhecimento = nivelAutoconhecimento; 
    }
    
    public int getIlhasDescobertas() { return ilhasDescobertas; }
    public void setIlhasDescobertas(int ilhasDescobertas) { 
        this.ilhasDescobertas = ilhasDescobertas; 
    }
    
    public void incrementarIlhasDescobertas() {
        this.ilhasDescobertas++;
    }
    
    public void adicionarExperiencia(int pontos) {
        this.nivelAutoconhecimento += pontos;
    }
}