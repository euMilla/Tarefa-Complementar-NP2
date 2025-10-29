import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static boolean usarBanco = false;
    private static final String URL = "jdbc:mysql://localhost:3306/divertidamente";
    private static final String USER = "root";
    private static final String PASSWORD = "4321";
    
    static {
        testarConexao();
    }
    
    public static Connection getConnection() throws SQLException {
        if (!usarBanco) {
            throw new SQLException("Modo offline ativo");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static boolean testarConexao() {
        try {
            // Tenta carregar o driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Testa a conexão
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            
            System.out.println("✅✅✅ MYSQL CONECTADO COM SUCESSO!");
            System.out.println("📊 Banco: divertidamente");
            System.out.println("👤 Usuário: root");
            System.out.println("🔗 URL: " + URL);
            
            // Verificar se as tabelas existem E têm dados
            if (verificarTabelasComDados(conn)) {
                System.out.println(" Todas as tabelas estão criadas e com dados!");
            } else {
                System.out.println(" Tabelas existem mas podem estar vazias");
            }
            
            conn.close();
            usarBanco = true;
            return true;
            
        } catch (Exception e) {
            System.out.println(" Modo OFFLINE ativado - MySQL não disponível");
            System.out.println("❌ Erro: " + e.getMessage());
            usarBanco = false;
            return false;
        }
    }
    
    private static boolean verificarTabelasComDados(Connection conn) {
        String[] tabelas = {"jogadores", "ilhas", "perguntas", "respostas"};
        try {
            for (String tabela : tabelas) {
                var stmt = conn.prepareStatement("SELECT COUNT(*) as total FROM " + tabela);
                var rs = stmt.executeQuery();
                if (rs.next()) {
                    int total = rs.getInt("total");
                    System.out.println("✅ Tabela " + tabela + " existe com " + total + " registros");
                    
                    // Se for ilhas ou perguntas e estiver vazia, inserir dados
                    if (total == 0 && (tabela.equals("ilhas") || tabela.equals("perguntas"))) {
                        System.out.println(" Inserindo dados iniciais na tabela " + tabela);
                        inserirDadosIniciais(conn, tabela);
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erro ao verificar tabelas: " + e.getMessage());
            return false;
        }
    }
    
    private static void inserirDadosIniciais(Connection conn, String tabela) {
        try {
            Statement stmt = conn.createStatement();
            
            if (tabela.equals("ilhas")) {
                stmt.executeUpdate(
                    "INSERT INTO ilhas (id, nome, tema, descricao, nivel_requerido) VALUES " +
                    "(1, 'Ilha da Memória', 'memoria', 'Explore suas lembranças e experiências passadas que moldaram quem você é.', 0), " +
                    "(2, 'Ilha da Criatividade', 'criatividade', 'Descubra seu potencial criativo e imagine futuras possibilidades.', 60), " +
                    "(3, 'Ilha das Emoções', 'emocao', 'Navegue pelo mar das suas emoções e aprenda a entendê-las melhor.', 120)"
                );
                System.out.println("✅ Dados inseridos na tabela ilhas");
            }
            else if (tabela.equals("perguntas")) {
                stmt.executeUpdate(
                    "INSERT INTO perguntas (id, ilha_id, texto, tipo) VALUES " +
                    "(1, 1, 'Qual é a sua memória mais feliz da infância? Descreva esse momento especial.', 'texto'), " +
                    "(2, 1, 'Que experiência do passado mais te marcou e por quê?', 'texto'), " +
                    "(3, 1, 'Qual pessoa foi mais importante na sua formação? O que você aprendeu com ela?', 'texto'), " +
                    "(4, 2, 'O que você gostaria de criar se não houvesse limites? Descreva sua ideia.', 'texto'), " +
                    "(5, 2, 'Como você expressa sua criatividade no dia a dia?', 'texto'), " +
                    "(6, 2, 'Qual é o seu projeto dos sonhos? Algo que você gostaria de realizar.', 'texto'), " +
                    "(7, 3, 'Como você lida com sentimentos difíceis como tristeza ou frustração?', 'texto'), " +
                    "(8, 3, 'Qual emoção você sente com mais frequência no seu dia a dia?', 'texto'), " +
                    "(9, 3, 'O que te faz sentir gratidão atualmente?', 'texto')"
                );
                System.out.println("✅ Dados inseridos na tabela perguntas");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao inserir dados iniciais: " + e.getMessage());
        }
    }
    
    public static boolean isUsandoBanco() {
        return usarBanco;
    }
}