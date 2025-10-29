import java.util.List;

public class JogoController {
    private DatabaseController dbController;
    private Jogador jogadorAtual;
    private Expedicao expedicaoAtual;
    
    public JogoController() {
        System.out.println("🎮 Inicializando Divertidamente...");
        this.dbController = new DatabaseController();
    }
    
    public boolean criarJogador(String nome) {
        if (nome == null || nome.trim().isEmpty()) return false;
        
        this.jogadorAtual = new Jogador(nome.trim());
        // Começar com nível 0 para ver apenas a primeira ilha
        jogadorAtual.setNivelAutoconhecimento(0);
        jogadorAtual.setIlhasDescobertas(0);
        
        int id = dbController.salvarJogador(jogadorAtual);
        jogadorAtual.setId(id);
        
        System.out.println("🎯 Jogador criado: " + nome + " (ID: " + id + ")");
        return true;
    }
    
    public List<Ilha> getIlhasDisponiveis() {
        if (jogadorAtual == null) {
            System.out.println("❌ Nenhum jogador atual");
            return null;
        }
        
        List<Ilha> ilhas = dbController.getIlhasDisponiveis(jogadorAtual.getNivelAutoconhecimento());
        System.out.println("🏝️ Ilhas disponíveis para nível " + jogadorAtual.getNivelAutoconhecimento() + ": " + 
            (ilhas != null ? ilhas.size() : 0));
        
        if (ilhas != null) {
            for (Ilha ilha : ilhas) {
                System.out.println("   - " + ilha.getNome() + " (Nível req: " + ilha.getNivelRequerido() + ")");
            }
        }
        
        return ilhas;
    }
    
    public boolean iniciarExpedicao(Ilha ilha) {
        if (jogadorAtual == null || ilha == null) return false;
        
        this.expedicaoAtual = new Expedicao(jogadorAtual.getId(), ilha.getId());
        System.out.println("🚀 Expedição iniciada: " + ilha.getNome() + " (ID: " + ilha.getId() + ")");
        return true;
    }
    
    public List<Pergunta> getPerguntasIlhaAtual() {
        if (expedicaoAtual == null) {
            System.out.println("❌ Nenhuma expedição ativa");
            return null;
        }
        return dbController.getPerguntasPorIlha(expedicaoAtual.getIlhaId());
    }
    
    public void processarResposta(Pergunta pergunta, String resposta) {
        if (jogadorAtual == null || expedicaoAtual == null || pergunta == null) {
            System.out.println("❌ Dados inválidos para processar resposta");
            return;
        }
        
        // Ganhar experiência por resposta
        jogadorAtual.adicionarExperiencia(20); // Aumentei para 20 por resposta
        expedicaoAtual.incrementarRespostas();
        
        dbController.salvarResposta(jogadorAtual, pergunta, resposta);
        
        System.out.println("📝 Resposta processada - Exp: +20, Total: " + 
            jogadorAtual.getNivelAutoconhecimento() + ", Respostas: " + 
            expedicaoAtual.getRespostasCompletas() + "/3");
        
        // Completar expedição após 3 respostas
        if (expedicaoAtual.getRespostasCompletas() >= 3) {
            finalizarExpedicao();
        }
    }
    
    private void finalizarExpedicao() {
        // Ganhar experiência extra por completar a ilha
        jogadorAtual.adicionarExperiencia(40); // +40 extra (total 100 por ilha)
        jogadorAtual.incrementarIlhasDescobertas();
        
        System.out.println("🏆 EXPEDIÇÃO FINALIZADA!");
        System.out.println("📊 Antes - Nível: " + (jogadorAtual.getNivelAutoconhecimento() - 40) + 
            ", Ilhas: " + (jogadorAtual.getIlhasDescobertas() - 1));
        System.out.println("📊 Depois - Nível: " + jogadorAtual.getNivelAutoconhecimento() + 
            ", Ilhas: " + jogadorAtual.getIlhasDescobertas());
        
        dbController.atualizarJogador(jogadorAtual);
        
        // Mostrar próximas ilhas disponíveis
        List<Ilha> proximasIlhas = getIlhasDisponiveis();
        if (proximasIlhas != null && proximasIlhas.size() > 1) {
            System.out.println("🔓 Próximas ilhas disponíveis: " + (proximasIlhas.size() - 1));
        }
    }
    
    public Jogador getJogadorAtual() {
        return jogadorAtual;
    }
    
    public boolean isJogoCompleto() {
        return jogadorAtual != null && jogadorAtual.getIlhasDescobertas() >= 3;
    }
}