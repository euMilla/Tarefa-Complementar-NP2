import java.sql.*;
import java.util.*;

public class DatabaseController {
    private List<Ilha> ilhasCache;
    private Map<Integer, List<Pergunta>> perguntasCache;
    
    public DatabaseController() {
        this.ilhasCache = new ArrayList<>();
        this.perguntasCache = new HashMap<>();
        System.out.println("Inicializando controlador do banco...");
        
        // SEMPRE tentar carregar do MySQL primeiro
        if (DatabaseConnection.isUsandoBanco()) {
            System.out.println("🗃️  MODO MYSQL ATIVO");
            if (!carregarDadosDoBanco()) {
                System.out.println("🔌 Fallback para modo offline");
                carregarDadosCache();
            }
        } else {
            System.out.println("🔌 MODO OFFLINE");
            carregarDadosCache();
        }
    }
    
    private boolean carregarDadosDoBanco() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("📥 Carregando dados do MySQL...");
            
            // Carregar ilhas
            String sqlIlhas = "SELECT * FROM ilhas ORDER BY nivel_requerido";
            PreparedStatement stmtIlhas = conn.prepareStatement(sqlIlhas);
            ResultSet rsIlhas = stmtIlhas.executeQuery();
            
            ilhasCache.clear();
            while (rsIlhas.next()) {
                Ilha ilha = new Ilha(
                    rsIlhas.getString("nome"),
                    rsIlhas.getString("tema"),
                    rsIlhas.getString("descricao"),
                    rsIlhas.getInt("nivel_requerido")
                );
                ilha.setId(rsIlhas.getInt("id"));
                ilhasCache.add(ilha);
            }
            
            // Carregar perguntas
            String sqlPerguntas = "SELECT * FROM perguntas ORDER BY ilha_id, id";
            PreparedStatement stmtPerguntas = conn.prepareStatement(sqlPerguntas);
            ResultSet rsPerguntas = stmtPerguntas.executeQuery();
            
            perguntasCache.clear();
            while (rsPerguntas.next()) {
                int ilhaId = rsPerguntas.getInt("ilha_id");
                Pergunta pergunta = new Pergunta(
                    ilhaId,
                    rsPerguntas.getString("texto"),
                    rsPerguntas.getString("tipo")
                );
                pergunta.setId(rsPerguntas.getInt("id"));
                
                if (!perguntasCache.containsKey(ilhaId)) {
                    perguntasCache.put(ilhaId, new ArrayList<>());
                }
                perguntasCache.get(ilhaId).add(pergunta);
            }
            
            System.out.println("✅ Dados carregados do MySQL: " + ilhasCache.size() + " ilhas, " + 
                contarTotalPerguntas() + " perguntas");
            return true;
                
        } catch (SQLException e) {
            System.out.println("❌ Erro ao carregar dados do MySQL: " + e.getMessage());
            return false;
        }
    }
    
    private void carregarDadosCache() {
        System.out.println("📥 Carregando dados offline...");
        
        ilhasCache.clear();
        perguntasCache.clear();
        
        // Criar ilhas
        Ilha[] ilhas = {
            new Ilha("Ilha da Memória", "memoria", "Explore suas lembranças...", 0),
            new Ilha("Ilha da Criatividade", "criatividade", "Descubra seu potencial...", 60),
            new Ilha("Ilha das Emoções", "emocao", "Navegue pelo mar das suas emoções...", 120)
        };
        
        for (int i = 0; i < ilhas.length; i++) {
            ilhas[i].setId(i + 1);
            ilhasCache.add(ilhas[i]);
        }
        
        // Criar perguntas
        criarTodasPerguntas();
        System.out.println("📋 Dados offline carregados: " + ilhasCache.size() + " ilhas, " + 
            contarTotalPerguntas() + " perguntas");
    }
    
    private void criarTodasPerguntas() {
    // Ilha 1 - Memória 
    List<Pergunta> memoria = Arrays.asList(
        new Pergunta(1, "Qual experiência da sua vida te transformou profundamente e por quê? Como essa vivência mudou sua perspectiva sobre a vida?", "texto"),
        new Pergunta(1, "Se você pudesse conversar com sua versão mais jovem, qual conselho daria baseado no que aprendeu até hoje? O que essa pessoa precisava ouvir naquela época?", "texto"),
        new Pergunta(1, "Que valores e crenças você herdou da sua família ou comunidade? Quais você mantém e quais decidiu transformar ao longo do tempo?", "texto")
    );
    
    // Ilha 2 - Criatividade 
    List<Pergunta> criatividade = Arrays.asList(
        new Pergunta(2, "Se você tivesse recursos ilimitados e não pudesse falhar, que projeto ou sonho realizaria para impactar positivamente o mundo ou sua comunidade?", "texto"),
        new Pergunta(2, "Como você expressa sua singularidade no dia a dia? De que forma sua maneira única de ser se manifesta nas pequenas escolhas e ações cotidianas?", "texto"),
        new Pergunta(2, "Qual é a visão que você tem para o seu futuro? Descreva como seria um dia perfeito daqui a 5 anos, incluindo como você se sentiria e o que estaria realizando.", "texto")
    );
    
    // Ilha 3 - Emoções 
    List<Pergunta> emocao = Arrays.asList(
        new Pergunta(3, "Como você lida com momentos de incerteza ou medo? Quais estratégias internas você desenvolveu para navegar por emoções desafiadoras?", "texto"),
        new Pergunta(3, "O que verdadeiramente te faz feliz além das conquistas externas? Descreva aqueles momentos simples que trazem profunda satisfação e paz interior.", "texto"),
        new Pergunta(3, "Que lições sobre relacionamentos e conexões humanas você carrega consigo? O que aprendeu sobre dar e receber amor, estabelecer limites e cultivar amizades?", "texto")
    );
    
    // Configurar IDs
    configurarPerguntas(1, memoria, 1);
    configurarPerguntas(2, criatividade, 4);
    configurarPerguntas(3, emocao, 7);
}
    
    private void configurarPerguntas(int ilhaId, List<Pergunta> perguntas, int startId) {
        for (int i = 0; i < perguntas.size(); i++) {
            Pergunta p = perguntas.get(i);
            p.setId(startId + i);
            p.setIlhaId(ilhaId);
        }
        perguntasCache.put(ilhaId, new ArrayList<>(perguntas));
    }
    
    private int contarTotalPerguntas() {
        int total = 0;
        for (List<Pergunta> perguntas : perguntasCache.values()) {
            total += perguntas.size();
        }
        return total;
    }
    
    public int salvarJogador(Jogador jogador) {
        if (!DatabaseConnection.isUsandoBanco()) {
            System.out.println("👤 Jogador criado (offline): " + jogador.getNome());
            jogador.setId(1);
            return 1;
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO jogadores (nome, nivel_autoconhecimento, ilhas_descobertas) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, jogador.getNome());
            stmt.setInt(2, jogador.getNivelAutoconhecimento());
            stmt.setInt(3, jogador.getIlhasDescobertas());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    jogador.setId(id);
                    System.out.println("✅ Jogador SALVO NO MYSQL: " + jogador.getNome() + " (ID: " + id + ")");
                    return id;
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao salvar jogador: " + e.getMessage());
        }
        
        jogador.setId(1);
        return 1;
    }
    
    public List<Ilha> getIlhasDisponiveis(int nivelJogador) {
        List<Ilha> disponiveis = new ArrayList<>();
        for (Ilha ilha : ilhasCache) {
            if (ilha.getNivelRequerido() <= nivelJogador) {
                disponiveis.add(ilha);
            }
        }
        return disponiveis;
    }
    
    public List<Pergunta> getPerguntasPorIlha(int ilhaId) {
        List<Pergunta> perguntas = perguntasCache.get(ilhaId);
        if (perguntas == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(perguntas);
    }
    
    public void salvarResposta(Jogador jogador, Pergunta pergunta, String resposta) {
        if (!DatabaseConnection.isUsandoBanco()) {
            System.out.println("💾 Resposta salva (offline)");
            return;
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO respostas (jogador_id, pergunta_id, resposta_texto) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, jogador.getId());
            stmt.setInt(2, pergunta.getId());
            stmt.setString(3, resposta);
            
            stmt.executeUpdate();
            System.out.println("✅ Resposta SALVA NO MYSQL - Jogador: " + jogador.getNome());
                
        } catch (SQLException e) {
            System.out.println("❌ Erro ao salvar resposta: " + e.getMessage());
        }
    }
    
    public void atualizarJogador(Jogador jogador) {
        if (!DatabaseConnection.isUsandoBanco()) {
            System.out.println("📊 Jogador atualizado (offline)");
            return;
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE jogadores SET nivel_autoconhecimento = ?, ilhas_descobertas = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, jogador.getNivelAutoconhecimento());
            stmt.setInt(2, jogador.getIlhasDescobertas());
            stmt.setInt(3, jogador.getId());
            
            stmt.executeUpdate();
            System.out.println("✅ Jogador ATUALIZADO NO MYSQL - " + jogador.getNome());
                
        } catch (SQLException e) {
            System.out.println("❌ Erro ao atualizar jogador: " + e.getMessage());
        }
    }
}