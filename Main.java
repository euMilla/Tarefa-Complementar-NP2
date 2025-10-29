import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("🎮 INICIANDO DIVERTIDAMENTE...");
        System.out.println("==========================================");
        
        DatabaseConnection.testarConexao();
        
        SwingUtilities.invokeLater(() -> {
            try {
                new TelaInicial().setVisible(true);
                System.out.println("✅ Jogo iniciado!");
            } catch (Exception e) {
                System.out.println("❌ Erro ao iniciar jogo: " + e.getMessage());
                JOptionPane.showMessageDialog(null, 
                    "Erro ao iniciar o jogo:\n" + e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}