import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class AtlasView extends JFrame {
    private JogoController jogoController;
    private JPanel painelIlhas;
    
    public AtlasView(JogoController jogoController) {
        this.jogoController = jogoController;
        configurarJanela();
        inicializarComponentes();
        carregarIlhas();
    }
    
    private void configurarJanela() {
        setTitle("🗺️ Atlas Mental - Divertidamente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void inicializarComponentes() {
        // Painel principal com gradiente - CORRIGIDO
        JPanel painelPrincipal = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                // Gradiente mais suave e escuro
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(30, 30, 90), 
                    getWidth(), getHeight(), new Color(70, 0, 120)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Adicionar algumas estrelas sutis no fundo
                g2d.setColor(new Color(255, 255, 255, 50));
                for (int i = 0; i < 50; i++) {
                    int x = (int)(Math.random() * getWidth());
                    int y = (int)(Math.random() * getHeight());
                    int size = 1 + (int)(Math.random() * 2);
                    g2d.fillOval(x, y, size, size);
                }
            }
        };
        painelPrincipal.setOpaque(true); // IMPORTANTE: tornar opaco
        
        painelPrincipal.add(criarCabecalho(), BorderLayout.NORTH);
        painelPrincipal.add(criarAreaIlhas(), BorderLayout.CENTER);
        
        add(painelPrincipal);
    }
    
    private JPanel criarCabecalho() {
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(new Color(40, 40, 80, 255)); // Sólido
        cabecalho.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel titulo = new JLabel("🌌 SEU ARQUIPÉLAGO MENTAL");
        titulo.setFont(new Font("Arial", Font.BOLD, 32));
        titulo.setForeground(new Color(255, 215, 0));
        
        Jogador jogador = jogoController.getJogadorAtual();
        JPanel painelInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        painelInfo.setOpaque(false);
        
        JLabel labelJogador = new JLabel("👤 " + jogador.getNome());
        labelJogador.setFont(new Font("Arial", Font.BOLD, 16));
        labelJogador.setForeground(Color.WHITE);
        
        JLabel labelNivel = new JLabel("⭐ Nível " + (jogador.getNivelAutoconhecimento() / 100));
        labelNivel.setFont(new Font("Arial", Font.BOLD, 16));
        labelNivel.setForeground(Color.WHITE);
        
        JLabel labelIlhas = new JLabel("🏝️ " + jogador.getIlhasDescobertas() + "/3 Ilhas");
        labelIlhas.setFont(new Font("Arial", Font.BOLD, 16));
        labelIlhas.setForeground(Color.WHITE);
        
        painelInfo.add(labelJogador);
        painelInfo.add(labelNivel);
        painelInfo.add(labelIlhas);
        
        cabecalho.add(titulo, BorderLayout.WEST);
        cabecalho.add(painelInfo, BorderLayout.EAST);
        
        return cabecalho;
    }
    
    private JPanel criarAreaIlhas() {
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        container.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        JLabel instrucao = new JLabel("🎯 Continue sua jornada explorando a próxima ilha:", JLabel.CENTER);
        instrucao.setFont(new Font("Arial", Font.BOLD, 24));
        instrucao.setForeground(Color.WHITE);
        instrucao.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        
        painelIlhas = new JPanel(new GridLayout(1, 3, 30, 30));
        painelIlhas.setOpaque(false);
        
        container.add(instrucao, BorderLayout.NORTH);
        container.add(painelIlhas, BorderLayout.CENTER);
        
        return container;
    }
    
    private void carregarIlhas() {
        List<Ilha> ilhas = jogoController.getIlhasDisponiveis();
        
        System.out.println("🎯 Carregando ilhas no Atlas...");
        
        if (ilhas == null || ilhas.isEmpty()) {
            System.out.println("❌ Nenhuma ilha disponível");
            // Todas as ilhas completas
            for (int i = 0; i < 3; i++) {
                painelIlhas.add(criarCardCompleto());
            }
        } else {
            System.out.println("✅ " + ilhas.size() + " ilhas disponíveis");
            
            Jogador jogador = jogoController.getJogadorAtual();
            int nivelJogador = jogador.getNivelAutoconhecimento();
            int ilhasDescobertas = jogador.getIlhasDescobertas();
            
            // Mostrar sempre 3 cards
            for (int i = 0; i < 3; i++) {
                if (i < ilhas.size()) {
                    Ilha ilha = ilhas.get(i);
                    boolean ilhaDescoberta = (i < ilhasDescobertas);
                    boolean ilhaDisponivel = (ilha.getNivelRequerido() <= nivelJogador);
                    
                    if (ilhaDescoberta) {
                        painelIlhas.add(criarCardCompleto());
                    } else if (ilhaDisponivel) {
                        painelIlhas.add(criarCardIlha(ilha));
                    } else {
                        painelIlhas.add(criarCardBloqueado());
                    }
                } else {
                    painelIlhas.add(criarCardBloqueado());
                }
            }
        }
        
        painelIlhas.revalidate();
        painelIlhas.repaint();
    }
    
    private JPanel criarCardIlha(Ilha ilha) {
        // Painel principal do card - CORRIGIDO
        JPanel card = new JPanel(new BorderLayout());
        Color corIlha = getCorIlha(ilha.getTema());
        
        // Fundo sólido sem transparência
        card.setBackground(new Color(corIlha.getRed(), corIlha.getGreen(), corIlha.getBlue()));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(escurecerCor(corIlha), 4),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(300, 400));
        
        // Efeito hover melhorado
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.YELLOW, 4),
                    BorderFactory.createEmptyBorder(25, 25, 25, 25)
                ));
                card.setBackground(clarearCor(corIlha));
            }
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(escurecerCor(corIlha), 4),
                    BorderFactory.createEmptyBorder(25, 25, 25, 25)
                ));
                card.setBackground(corIlha);
            }
        });
        
        // Conteúdo do card
        JPanel conteudo = new JPanel(new BorderLayout(15, 15));
        conteudo.setOpaque(false);
        
        // Ícone
        JLabel labelIcone = new JLabel(getEmojiIlha(ilha.getTema()), JLabel.CENTER);
        labelIcone.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        labelIcone.setForeground(Color.WHITE);
        
        // Nome da ilha
        JLabel labelNome = new JLabel(ilha.getNome(), JLabel.CENTER);
        labelNome.setFont(new Font("Arial", Font.BOLD, 22));
        labelNome.setForeground(Color.WHITE);
        
        // Descrição
        JTextArea descricao = new JTextArea(ilha.getDescricao());
        descricao.setEditable(false);
        descricao.setLineWrap(true);
        descricao.setWrapStyleWord(true);
        descricao.setOpaque(false);
        descricao.setForeground(Color.WHITE);
        descricao.setFont(new Font("Arial", Font.PLAIN, 14));
        descricao.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        
        // Requisitos
        JLabel labelRequisito = new JLabel("Nível " + (ilha.getNivelRequerido() / 100) + " • 3 Perguntas", JLabel.CENTER);
        labelRequisito.setFont(new Font("Arial", Font.ITALIC, 12));
        labelRequisito.setForeground(new Color(255, 255, 200));
        
        // Botão
        JButton btnExplorar = new JButton("Continuar Jornada →");
        btnExplorar.setFont(new Font("Arial", Font.BOLD, 16));
        btnExplorar.setBackground(Color.WHITE);
        btnExplorar.setForeground(corIlha);
        btnExplorar.setFocusPainted(false);
        btnExplorar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            BorderFactory.createEmptyBorder(12, 25, 12, 25)
        ));
        btnExplorar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efeito hover no botão
        btnExplorar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnExplorar.setBackground(new Color(240, 240, 240));
            }
            public void mouseExited(MouseEvent e) {
                btnExplorar.setBackground(Color.WHITE);
            }
        });
        
        btnExplorar.addActionListener(e -> explorarIlha(ilha));
        
        // Painel superior (ícone, nome, requisito)
        JPanel painelSuperior = new JPanel(new BorderLayout(10, 10));
        painelSuperior.setOpaque(false);
        painelSuperior.add(labelIcone, BorderLayout.NORTH);
        painelSuperior.add(labelNome, BorderLayout.CENTER);
        painelSuperior.add(labelRequisito, BorderLayout.SOUTH);
        
        // Montagem do conteúdo
        conteudo.add(painelSuperior, BorderLayout.NORTH);
        conteudo.add(descricao, BorderLayout.CENTER);
        conteudo.add(btnExplorar, BorderLayout.SOUTH);
        
        card.add(conteudo, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel criarCardBloqueado() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(60, 60, 60)); // Cinza escuro sólido
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 3),
            BorderFactory.createEmptyBorder(40, 25, 40, 25)
        ));
        card.setPreferredSize(new Dimension(300, 400));
        
        JPanel conteudo = new JPanel(new BorderLayout(15, 15));
        conteudo.setOpaque(false);
        
        JLabel icone = new JLabel("🔒", JLabel.CENTER);
        icone.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        icone.setForeground(new Color(180, 180, 180));
        
        JLabel texto = new JLabel("Ilha Bloqueada", JLabel.CENTER);
        texto.setFont(new Font("Arial", Font.ITALIC, 20));
        texto.setForeground(new Color(200, 200, 200));
        
        JLabel info = new JLabel("Complete a ilha anterior", JLabel.CENTER);
        info.setFont(new Font("Arial", Font.PLAIN, 14));
        info.setForeground(new Color(150, 150, 150));
        
        conteudo.add(info, BorderLayout.NORTH);
        conteudo.add(icone, BorderLayout.CENTER);
        conteudo.add(texto, BorderLayout.SOUTH);
        
        card.add(conteudo, BorderLayout.CENTER);
        return card;
    }
    
    private JPanel criarCardCompleto() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(50, 150, 50)); // Verde sólido
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 200, 100), 3),
            BorderFactory.createEmptyBorder(40, 25, 40, 25)
        ));
        card.setPreferredSize(new Dimension(300, 400));
        
        JPanel conteudo = new JPanel(new BorderLayout(15, 15));
        conteudo.setOpaque(false);
        
        JLabel icone = new JLabel("✅", JLabel.CENTER);
        icone.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 64));
        icone.setForeground(Color.WHITE);
        
        JLabel texto = new JLabel("Ilha Concluída", JLabel.CENTER);
        texto.setFont(new Font("Arial", Font.BOLD, 20));
        texto.setForeground(Color.WHITE);
        
        JLabel info = new JLabel("Jornada completa!", JLabel.CENTER);
        info.setFont(new Font("Arial", Font.PLAIN, 14));
        info.setForeground(new Color(200, 255, 200));
        
        conteudo.add(info, BorderLayout.NORTH);
        conteudo.add(icone, BorderLayout.CENTER);
        conteudo.add(texto, BorderLayout.SOUTH);
        
        card.add(conteudo, BorderLayout.CENTER);
        return card;
    }
    
    private Color escurecerCor(Color cor) {
        return new Color(
            Math.max(cor.getRed() - 40, 0),
            Math.max(cor.getGreen() - 40, 0),
            Math.max(cor.getBlue() - 40, 0)
        );
    }
    
    private Color clarearCor(Color cor) {
        return new Color(
            Math.min(cor.getRed() + 20, 255),
            Math.min(cor.getGreen() + 20, 255),
            Math.min(cor.getBlue() + 20, 255)
        );
    }
    
    private String getEmojiIlha(String tema) {
        switch (tema) {
            case "memoria": return "📚";
            case "criatividade": return "🎨";
            case "emocao": return "💖";
            default: return "🏝️";
        }
    }
    
    private Color getCorIlha(String tema) {
        switch (tema) {
            case "memoria": return new Color(160, 82, 45);    // Marrom
            case "criatividade": return new Color(65, 105, 225); // Azul
            case "emocao": return new Color(220, 20, 60);     // Vermelho
            default: return new Color(70, 130, 180);
        }
    }
    
    private void explorarIlha(Ilha ilha) {
        dispose();
        new TelaExpedicao(jogoController, ilha).setVisible(true);
    }
}