import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class TelaInicial extends JFrame {
    private JogoController jogoController;
    private JTextField campoNome;
    private JButton btnIniciar;
    private Point initialClick;
    
    public TelaInicial() {
        this.jogoController = new JogoController();
        configurarJanela();
        inicializarComponentes();
        configurarArrastar();
    }
    
    private void configurarJanela() {
        setTitle("Divertidamente - Jornada Interior");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 1000, 700, 50, 50));
    }
    
    private void inicializarComponentes() {
        // Painel principal com gradiente animado
        JPanel painelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // Gradiente mágico azul e roxo
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(25, 25, 112), 
                    getWidth(), getHeight(), new Color(75, 0, 130)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Estrelas cintilantes
                g2d.setColor(Color.WHITE);
                for (int i = 0; i < 100; i++) {
                    int x = (int)(Math.random() * getWidth());
                    int y = (int)(Math.random() * getHeight());
                    int size = 1 + (int)(Math.random() * 3);
                    g2d.fillOval(x, y, size, size);
                }
                
                // Ilhas flutuantes no fundo
                drawIlhas(g2d);
            }
            
            private void drawIlhas(Graphics2D g2d) {
                // Ilha da Memória (marrom)
                g2d.setColor(new Color(139, 69, 19, 100));
                g2d.fillOval(100, 500, 120, 60);
                
                // Ilha da Criatividade (azul)
                g2d.setColor(new Color(30, 144, 255, 100));
                g2d.fillOval(300, 480, 140, 70);
                
                // Ilha das Emoções (vermelho)
                g2d.setColor(new Color(220, 20, 60, 100));
                g2d.fillOval(600, 520, 130, 65);
            }
        };
        painelPrincipal.setLayout(new BorderLayout());
        
        // Botão fechar personalizado
        JButton btnFechar = criarBotaoFechar();
        
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelTopo.setOpaque(false);
        painelTopo.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));
        painelTopo.add(btnFechar);
        
        // Conteúdo central
        JPanel painelConteudo = new JPanel();
        painelConteudo.setOpaque(false);
        painelConteudo.setLayout(new BoxLayout(painelConteudo, BoxLayout.Y_AXIS));
        painelConteudo.setBorder(BorderFactory.createEmptyBorder(80, 0, 80, 0));
        
        // Título principal com efeito
        JLabel titulo = new JLabel("DIVERTIDAMENTE");
        titulo.setFont(new Font("Arial", Font.BOLD, 72));
        titulo.setForeground(new Color(255, 215, 0));
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Efeito de brilho no título
        titulo.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                titulo.setForeground(new Color(255, 255, 150));
            }
            public void mouseExited(MouseEvent e) {
                titulo.setForeground(new Color(255, 215, 0));
            }
        });
        
        // Subtítulo
        JLabel subtitulo = new JLabel("Uma Jornada de Autoconhecimento");
        subtitulo.setFont(new Font("Arial", Font.ITALIC, 24));
        subtitulo.setForeground(new Color(200, 200, 255));
        subtitulo.setAlignmentX(CENTER_ALIGNMENT);
        subtitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 80, 0));
        
        // Container do formulário - sem fundo branco
        JPanel containerForm = new JPanel();
        containerForm.setOpaque(false);
        containerForm.setLayout(new BoxLayout(containerForm, BoxLayout.Y_AXIS));
        containerForm.setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100));
        
        // Label do nome
        JLabel labelNome = new JLabel("Qual é o seu nome, explorador?");
        labelNome.setFont(new Font("Arial", Font.BOLD, 22));
        labelNome.setForeground(Color.WHITE);
        labelNome.setAlignmentX(CENTER_ALIGNMENT);
        labelNome.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Campo de nome
        campoNome = new JTextField();
        campoNome.setFont(new Font("Arial", Font.PLAIN, 20));
        campoNome.setMaximumSize(new Dimension(400, 60));
        campoNome.setAlignmentX(CENTER_ALIGNMENT);
        campoNome.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 215, 0), 3),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        campoNome.setBackground(new Color(255, 255, 255, 230));
        campoNome.setForeground(new Color(50, 50, 50));
        campoNome.setCaretColor(new Color(255, 215, 0));
        
        // Placeholder personalizado
        campoNome.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campoNome.getText().equals("Digite seu nome aqui...")) {
                    campoNome.setText("");
                    campoNome.setForeground(new Color(50, 50, 50));
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (campoNome.getText().isEmpty()) {
                    campoNome.setText("Digite seu nome aqui...");
                    campoNome.setForeground(new Color(150, 150, 150));
                }
            }
        });
        
        // Texto placeholder inicial
        campoNome.setText("Digite seu nome aqui...");
        campoNome.setForeground(new Color(150, 150, 150));
        
        // Botão iniciar - TEXTO REDUZIDO E FONTE MENOR
        btnIniciar = new JButton("INICIAR JORNADA");
        btnIniciar.setFont(new Font("Arial", Font.BOLD, 20)); // Fonte reduzida
        btnIniciar.setBackground(new Color(50, 205, 50));
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.setFocusPainted(false);
        btnIniciar.setMaximumSize(new Dimension(380, 65)); // Largura aumentada
        btnIniciar.setAlignmentX(CENTER_ALIGNMENT);
        btnIniciar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 215, 0), 3),
            BorderFactory.createEmptyBorder(15, 40, 15, 40)
        ));
        btnIniciar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efeitos hover no botão
        btnIniciar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnIniciar.setBackground(new Color(60, 220, 60));
                btnIniciar.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 255, 150), 3),
                    BorderFactory.createEmptyBorder(15, 40, 15, 40)
                ));
            }
            public void mouseExited(MouseEvent e) {
                btnIniciar.setBackground(new Color(50, 205, 50));
                btnIniciar.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 215, 0), 3),
                    BorderFactory.createEmptyBorder(15, 40, 15, 40)
                ));
            }
        });
        
        // Texto de rodapé
        JLabel rodape = new JLabel("Descubra os segredos da sua mente");
        rodape.setFont(new Font("Arial", Font.ITALIC, 16));
        rodape.setForeground(new Color(200, 200, 255));
        rodape.setAlignmentX(CENTER_ALIGNMENT);
        rodape.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        
        // Montagem do container do formulário
        containerForm.add(labelNome);
        containerForm.add(Box.createVerticalStrut(20));
        containerForm.add(campoNome);
        containerForm.add(Box.createVerticalStrut(40));
        containerForm.add(btnIniciar);
        
        // Montagem do conteúdo principal
        painelConteudo.add(titulo);
        painelConteudo.add(subtitulo);
        painelConteudo.add(containerForm);
        painelConteudo.add(rodape);
        
        painelPrincipal.add(painelTopo, BorderLayout.NORTH);
        painelPrincipal.add(painelConteudo, BorderLayout.CENTER);
        
        add(painelPrincipal);
        
        // Listeners
        btnIniciar.addActionListener(e -> iniciarJogo());
        campoNome.addActionListener(e -> iniciarJogo());
        btnFechar.addActionListener(e -> System.exit(0));
        
        // Focar no campo de nome ao abrir
        SwingUtilities.invokeLater(() -> {
            campoNome.requestFocusInWindow();
        });
    }
    
    private JButton criarBotaoFechar() {
        JButton btnFechar = new JButton("X"); // Sem emoji
        btnFechar.setFont(new Font("Arial", Font.BOLD, 16));
        btnFechar.setBackground(new Color(255, 59, 48));
        btnFechar.setForeground(Color.WHITE);
        btnFechar.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        btnFechar.setFocusPainted(false);
        btnFechar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efeito hover no botão fechar
        btnFechar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnFechar.setBackground(new Color(255, 100, 100));
            }
            public void mouseExited(MouseEvent e) {
                btnFechar.setBackground(new Color(255, 59, 48));
            }
        });
        
        return btnFechar;
    }
    
    private void configurarArrastar() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int thisX = getLocation().x;
                int thisY = getLocation().y;
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;
                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                setLocation(X, Y);
            }
        });
    }
    
    private void iniciarJogo() {
        String nome = campoNome.getText().trim();
        
        if (nome.equals("Digite seu nome aqui...") || nome.isEmpty()) {
            mostrarMensagemErro("Por favor, digite seu nome para começar esta aventura!");
            campoNome.requestFocus();
            return;
        }
        
        if (nome.length() < 2) {
            mostrarMensagemErro("Seu nome precisa ter pelo menos 2 caracteres!");
            campoNome.requestFocus();
            return;
        }
        
        try {
            if (jogoController.criarJogador(nome)) {
                btnIniciar.setEnabled(false);
                btnIniciar.setText("INICIANDO...");
                
                Timer timer = new Timer(1000, e -> {
                    try {
                        new AtlasView(jogoController).setVisible(true);
                        dispose();
                    } catch (Exception ex) {
                        System.out.println("Erro ao abrir Atlas: " + ex.getMessage());
                        ex.printStackTrace();
                        mostrarMensagemErro("Erro ao iniciar jogo. Tente novamente.");
                    }
                });
                timer.setRepeats(false);
                timer.start();
                
            } else {
                mostrarMensagemErro("Ops! Algo deu errado ao criar seu personagem. Tente novamente.");
            }
        } catch (Exception e) {
            System.out.println("Erro crítico: " + e.getMessage());
            e.printStackTrace();
            mostrarMensagemErro("Erro inesperado. Reinicie o jogo.");
        }
    }
    
    private void mostrarMensagemErro(String mensagem) {
        JOptionPane.showMessageDialog(this, 
            "<html><div style='text-align: center; font-size: 14pt;'>" + mensagem + "</div></html>", 
            "Divertidamente", JOptionPane.WARNING_MESSAGE);
    }
}