import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class TelaExpedicao extends JFrame {
    private JogoController jogoController;
    private Ilha ilhaAtual;
    private List<Pergunta> perguntas;
    private int perguntaAtualIndex;
    private JLabel labelTitulo, labelContador;
    private JTextArea areaPergunta, areaResposta;
    private JButton btnProxima, btnVoltar;
    private JPanel painelProgresso;
    
    public TelaExpedicao(JogoController jogoController, Ilha ilha) {
        this.jogoController = jogoController;
        this.ilhaAtual = ilha;
        
        jogoController.iniciarExpedicao(ilha);
        this.perguntas = jogoController.getPerguntasIlhaAtual();
        this.perguntaAtualIndex = 0;
        
        if (perguntas == null || perguntas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Erro: Nenhuma pergunta encontrada.");
            voltarParaAtlas();
            return;
        }
        
        configurarJanela();
        inicializarComponentes();
        mostrarPerguntaAtual();
        setVisible(true);
    }
    
    private void configurarJanela() {
        setTitle("Expedição - " + ilhaAtual.getNome());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void inicializarComponentes() {
        // Painel principal com gradiente
        JPanel painelPrincipal = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(30, 30, 90), 
                    getWidth(), getHeight(), new Color(70, 0, 120)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        painelPrincipal.setOpaque(true);
        
        painelPrincipal.add(criarHeader(), BorderLayout.NORTH);
        painelPrincipal.add(criarAreaConteudo(), BorderLayout.CENTER);
        painelPrincipal.add(criarFooter(), BorderLayout.SOUTH);
        
        add(painelPrincipal);
    }
    
    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(40, 40, 80, 255));
        header.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        
        labelTitulo = new JLabel(ilhaAtual.getNome());
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        labelTitulo.setForeground(new Color(255, 215, 0));
        
        labelContador = new JLabel("1/" + perguntas.size());
        labelContador.setFont(new Font("Arial", Font.BOLD, 18));
        labelContador.setForeground(Color.WHITE);
        
        header.add(labelTitulo, BorderLayout.WEST);
        header.add(labelContador, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel criarAreaConteudo() {
        JPanel conteudo = new JPanel(new BorderLayout(20, 20));
        conteudo.setOpaque(false);
        conteudo.setBorder(BorderFactory.createEmptyBorder(30, 40, 20, 40));
        
        conteudo.add(criarBarraProgresso(), BorderLayout.NORTH);
        conteudo.add(criarAreaPergunta(), BorderLayout.CENTER);
        conteudo.add(criarAreaResposta(), BorderLayout.SOUTH);
        
        return conteudo;
    }
    
    private JPanel criarBarraProgresso() {
        painelProgresso = new JPanel(new GridLayout(1, perguntas.size(), 5, 0));
        painelProgresso.setOpaque(false);
        
        for (int i = 0; i < perguntas.size(); i++) {
            JPanel etapa = new JPanel();
            etapa.setBackground(i == 0 ? new Color(255, 215, 0) : new Color(100, 100, 150));
            etapa.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
            etapa.setPreferredSize(new Dimension(40, 8));
            painelProgresso.add(etapa);
        }
        
        JPanel container = new JPanel(new FlowLayout(FlowLayout.CENTER));
        container.setOpaque(false);
        container.add(painelProgresso);
        
        return container;
    }
    
    private JPanel criarAreaPergunta() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setOpaque(false);
        
        JLabel label = new JLabel("PERGUNTA ATUAL:");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(255, 215, 0));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JPanel containerPergunta = new JPanel(new BorderLayout());
        containerPergunta.setOpaque(false);
        containerPergunta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 215, 0), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        areaPergunta = new JTextArea();
        areaPergunta.setEditable(false);
        areaPergunta.setLineWrap(true);
        areaPergunta.setWrapStyleWord(true);
        areaPergunta.setFont(new Font("Arial", Font.PLAIN, 16));
        areaPergunta.setOpaque(false);
        areaPergunta.setForeground(Color.WHITE);
        areaPergunta.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        areaPergunta.setFocusable(false);
        areaPergunta.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane scroll = new JScrollPane(areaPergunta);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setPreferredSize(new Dimension(800, 150));
        
        containerPergunta.add(scroll, BorderLayout.CENTER);
        
        painel.add(label, BorderLayout.NORTH);
        painel.add(containerPergunta, BorderLayout.CENTER);
        
        return painel;
    }
    
    private JPanel criarAreaResposta() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setOpaque(false);
        
        JLabel label = new JLabel("SUA RESPOSTA:");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(255, 215, 0));
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 15, 0));
        
        JPanel containerResposta = new JPanel(new BorderLayout());
        containerResposta.setOpaque(false);
        containerResposta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 215, 0), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        areaResposta = new JTextArea(6, 50);
        areaResposta.setLineWrap(true);
        areaResposta.setWrapStyleWord(true);
        areaResposta.setFont(new Font("Arial", Font.PLAIN, 14));
        areaResposta.setBackground(new Color(255, 255, 255, 250));
        areaResposta.setForeground(new Color(40, 40, 40));
        areaResposta.setCaretColor(new Color(40, 40, 40));
        areaResposta.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        areaResposta.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isControlDown()) {
                    proximaPergunta();
                }
            }
        });
        
        JScrollPane scroll = new JScrollPane(areaResposta);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        
        containerResposta.add(scroll, BorderLayout.CENTER);
        
        JLabel dica = new JLabel("Dica: Use Ctrl+Enter para avançar mais rápido");
        dica.setFont(new Font("Arial", Font.ITALIC, 12));
        dica.setForeground(new Color(200, 200, 255));
        dica.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        
        painel.add(label, BorderLayout.NORTH);
        painel.add(containerResposta, BorderLayout.CENTER);
        painel.add(dica, BorderLayout.SOUTH);
        
        return painel;
    }
    
    private JPanel criarFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        btnVoltar = new JButton("Voltar ao Atlas");
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 14));
        btnVoltar.setBackground(new Color(100, 100, 150));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFocusPainted(false);
        btnVoltar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnVoltar.addActionListener(e -> voltarParaAtlas());
        
        btnProxima = new JButton("Próxima Pergunta");
        btnProxima.setFont(new Font("Arial", Font.BOLD, 14));
        btnProxima.setBackground(new Color(50, 205, 50));
        btnProxima.setForeground(Color.WHITE);
        btnProxima.setFocusPainted(false);
        btnProxima.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnProxima.addActionListener(e -> proximaPergunta());
        
        footer.add(btnVoltar);
        footer.add(btnProxima);
        
        return footer;
    }
    
    private void atualizarBarraProgresso() {
        Component[] componentes = painelProgresso.getComponents();
        for (int i = 0; i < componentes.length; i++) {
            JPanel etapa = (JPanel) componentes[i];
            etapa.setBackground(i <= perguntaAtualIndex ? 
                new Color(255, 215, 0) : new Color(100, 100, 150));
        }
    }
    
    private void mostrarPerguntaAtual() {
        if (perguntaAtualIndex < perguntas.size()) {
            Pergunta pergunta = perguntas.get(perguntaAtualIndex);
            
            areaPergunta.setText(pergunta.getTexto());
            labelContador.setText((perguntaAtualIndex + 1) + "/" + perguntas.size());
            
            areaResposta.setText("");
            areaResposta.setCaretPosition(0);
            areaResposta.requestFocusInWindow();
            
            areaPergunta.repaint();
            areaResposta.repaint();
            
            atualizarBarraProgresso();
            
            if (perguntaAtualIndex == perguntas.size() - 1) {
                btnProxima.setText("Finalizar Expedição");
                btnProxima.setBackground(new Color(40, 167, 69));
            } else {
                btnProxima.setText("Próxima Pergunta");
                btnProxima.setBackground(new Color(50, 205, 50));
            }
        }
    }
    
    private void proximaPergunta() {
        String resposta = areaResposta.getText().trim();
        
        if (resposta.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, escreva sua resposta antes de continuar.", 
                "Resposta Necessária", 
                JOptionPane.WARNING_MESSAGE);
            areaResposta.requestFocus();
            return;
        }
        
        Pergunta perguntaAtual = perguntas.get(perguntaAtualIndex);
        jogoController.processarResposta(perguntaAtual, resposta);
        perguntaAtualIndex++;
        
        if (perguntaAtualIndex < perguntas.size()) {
            mostrarPerguntaAtual();
        } else {
            finalizarExpedicao();
        }
    }
    
    private void finalizarExpedicao() {
        Jogador jogador = jogoController.getJogadorAtual();
        
        String mensagem = String.format(
            "Ilha Concluída!\n\n" +
            "Ilha: %s\n" +
            "Experiência: +100 pontos\n" +
            "Nível atual: %d\n" +
            "Progresso: %d/3 ilhas\n\n" +
            "Clique em Continuar para explorar a próxima ilha!",
            ilhaAtual.getNome(),
            jogador.getNivelAutoconhecimento(),
            jogador.getIlhasDescobertas()
        );
        
        int resposta = JOptionPane.showOptionDialog(this, 
            mensagem, 
            "Expedição Concluída", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new Object[]{"Continuar Jornada"},
            "Continuar Jornada");
        
        if (jogoController.isJogoCompleto()) {
            mostrarTelaFinal();
        } else {
            voltarParaAtlas();
        }
    }
    
    private void voltarParaAtlas() {
        new AtlasView(jogoController).setVisible(true);
        dispose();
    }
    
    private void mostrarTelaFinal() {
    JFrame telaFinal = new JFrame("Jornada Concluída!");
    telaFinal.setSize(600, 650);
    telaFinal.setLocationRelativeTo(null);
    telaFinal.setResizable(false);
    
    JPanel painelPrincipal = new JPanel(new BorderLayout());
    painelPrincipal.setBackground(new Color(25, 25, 80));
    painelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
    
    JPanel conteudo = new JPanel();
    conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
    conteudo.setBackground(new Color(25, 25, 80));
    
    // Título principal
    JLabel titulo = new JLabel("CONQUISTA ALCANCADA!", JLabel.CENTER);
    titulo.setFont(new Font("Arial", Font.BOLD, 26));
    titulo.setForeground(new Color(255, 215, 0));
    titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
    titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
    
    // Ícone comemorativo
    JLabel iconeCelebracao = new JLabel(" CONQUISTAS ", JLabel.CENTER);
    iconeCelebracao.setFont(new Font("Arial", Font.BOLD, 20));
    iconeCelebracao.setForeground(new Color(255, 255, 150));
    iconeCelebracao.setAlignmentX(Component.CENTER_ALIGNMENT);
    iconeCelebracao.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
    
    // Painel de estatísticas
    JPanel painelStats = new JPanel();
    painelStats.setLayout(new BoxLayout(painelStats, BoxLayout.Y_AXIS));
    painelStats.setBackground(new Color(45, 45, 110));
    painelStats.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(255, 215, 0), 2),
        BorderFactory.createEmptyBorder(20, 30, 20, 30)
    ));
    painelStats.setAlignmentX(Component.CENTER_ALIGNMENT);
    painelStats.setMaximumSize(new Dimension(450, 140));
    
    Jogador jogador = jogoController.getJogadorAtual();
    
    // Estatísticas
    JLabel statIlhas = new JLabel("► Arquipélago Mental: 3/3 Ilhas Concluídas");
    statIlhas.setFont(new Font("Arial", Font.BOLD, 15));
    statIlhas.setForeground(Color.WHITE);
    statIlhas.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    JLabel statPontos = new JLabel("► Sabedoria Interior: " + jogador.getNivelAutoconhecimento() + " pontos");
    statPontos.setFont(new Font("Arial", Font.BOLD, 15));
    statPontos.setForeground(Color.WHITE);
    statPontos.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    JLabel statExplorador = new JLabel("► Explorador: " + jogador.getNome());
    statExplorador.setFont(new Font("Arial", Font.BOLD, 15));
    statExplorador.setForeground(Color.WHITE);
    statExplorador.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    painelStats.add(statIlhas);
    painelStats.add(Box.createVerticalStrut(12));
    painelStats.add(statPontos);
    painelStats.add(Box.createVerticalStrut(12));
    painelStats.add(statExplorador);
    
    // Mensagem inspiradora
    JPanel painelMensagem = new JPanel(new BorderLayout());
    painelMensagem.setBackground(new Color(35, 35, 95));
    painelMensagem.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(255, 215, 0), 1),
        BorderFactory.createEmptyBorder(25, 30, 25, 30)
    ));
    painelMensagem.setAlignmentX(Component.CENTER_ALIGNMENT);
    painelMensagem.setMaximumSize(new Dimension(500, 220));
    
    JTextArea mensagem = new JTextArea();
    mensagem.setText(
        "SUA JORNADA DE AUTOCONHECIMENTO\n\n" +
        "Você navegou pelas ilhas da sua mente\ne descobriu tesouros interiores valiosos.\n\n" +
        "• Memórias que constroem sua história\n" +
        "• Criatividade que transforma realidades\n" +
        "• Emoções que guiam seu caminho\n\n" +
        "Esta conquista é apenas o começo.\nSeu arquipélago mental continua se expandindo\ncom cada nova experiência de vida."
    );
    mensagem.setEditable(false);
    mensagem.setLineWrap(true);
    mensagem.setWrapStyleWord(true);
    mensagem.setBackground(new Color(35, 35, 95));
    mensagem.setForeground(new Color(255, 255, 200));
    mensagem.setFont(new Font("Arial", Font.PLAIN, 14));
    mensagem.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    painelMensagem.add(mensagem, BorderLayout.CENTER);
    
    // Painel de botões
    JPanel painelBotoes = new JPanel();
    painelBotoes.setLayout(new BoxLayout(painelBotoes, BoxLayout.X_AXIS));
    painelBotoes.setBackground(new Color(25, 25, 80));
    painelBotoes.setAlignmentX(Component.CENTER_ALIGNMENT);
    painelBotoes.setBorder(BorderFactory.createEmptyBorder(30, 0, 15, 0));
    
    JButton btnSair = new JButton("SAIR DO JOGO");
    btnSair.setFont(new Font("Arial", Font.BOLD, 14));
    btnSair.setBackground(new Color(220, 20, 60));
    btnSair.setForeground(Color.WHITE);
    btnSair.setFocusPainted(false);
    btnSair.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.WHITE, 2),
        BorderFactory.createEmptyBorder(12, 25, 12, 25) // CORRIGIDO: createEmptyBorder
    ));
    btnSair.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
    btnSair.addActionListener(e -> System.exit(0));
    
    // Efeito hover no botão
    btnSair.addMouseListener(new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            btnSair.setBackground(new Color(255, 60, 60));
            btnSair.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 150), 2),
                BorderFactory.createEmptyBorder(12, 25, 12, 25) // CORRIGIDO: createEmptyBorder
            ));
        }
        public void mouseExited(MouseEvent e) {
            btnSair.setBackground(new Color(220, 20, 60));
            btnSair.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(12, 25, 12, 25) // CORRIGIDO: createEmptyBorder
            ));
        }
    });
    
    painelBotoes.add(Box.createHorizontalGlue());
    painelBotoes.add(btnSair);
    painelBotoes.add(Box.createHorizontalGlue());
    
    // Rodapé
    JLabel rodape = new JLabel("A jornada do autoconhecimento nunca termina...", JLabel.CENTER);
    rodape.setFont(new Font("Arial", Font.ITALIC, 13));
    rodape.setForeground(new Color(180, 180, 255));
    rodape.setAlignmentX(Component.CENTER_ALIGNMENT);
    rodape.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
    
    // Montagem final
    conteudo.add(titulo);
    conteudo.add(iconeCelebracao);
    conteudo.add(painelStats);
    conteudo.add(Box.createVerticalStrut(25));
    conteudo.add(painelMensagem);
    conteudo.add(Box.createVerticalStrut(30));
    conteudo.add(painelBotoes);
    conteudo.add(Box.createVerticalStrut(15));
    conteudo.add(rodape);
    
    painelPrincipal.add(conteudo, BorderLayout.CENTER);
    
    telaFinal.add(painelPrincipal);
    telaFinal.setVisible(true);
    dispose();
}
}