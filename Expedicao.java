import java.util.Date;

public class Expedicao {
    private int id;
    private int jogadorId;
    private int ilhaId;
    private Date dataInicio;
    private Date dataFim;
    private String status;
    private int respostasCompletas;
    
    public Expedicao() {}
    
    public Expedicao(int jogadorId, int ilhaId) {
        this.jogadorId = jogadorId;
        this.ilhaId = ilhaId;
        this.dataInicio = new Date();
        this.status = "ativa";
        this.respostasCompletas = 0;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getJogadorId() { return jogadorId; }
    public void setJogadorId(int jogadorId) { this.jogadorId = jogadorId; }
    
    public int getIlhaId() { return ilhaId; }
    public void setIlhaId(int ilhaId) { this.ilhaId = ilhaId; }
    
    public Date getDataInicio() { return dataInicio; }
    public void setDataInicio(Date dataInicio) { this.dataInicio = dataInicio; }
    
    public Date getDataFim() { return dataFim; }
    public void setDataFim(Date dataFim) { this.dataFim = dataFim; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public int getRespostasCompletas() { return respostasCompletas; }
    public void setRespostasCompletas(int respostasCompletas) { 
        this.respostasCompletas = respostasCompletas; 
    }
    
    public void incrementarRespostas() {
        this.respostasCompletas++;
        if (this.respostasCompletas > 3) {
            this.respostasCompletas = 3; // Limitar a 3 respostas
        }
    }
    
    @Override
    public String toString() {
        return "Expedicao{id=" + id + ", jogadorId=" + jogadorId + 
               ", ilhaId=" + ilhaId + ", respostas=" + respostasCompletas + "}";
    }
}