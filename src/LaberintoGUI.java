import javax.swing.*;
import java.awt.*;

public class LaberintoGUI extends JPanel {
    private int[][] laberinto;

    // Tamaño de cada celda en píxeles
    private static final int CELDA_SIZE = 20;

    public LaberintoGUI(int[][] laberinto) {
        this.laberinto = laberinto;
        setPreferredSize(new Dimension(laberinto[0].length * CELDA_SIZE, laberinto.length * CELDA_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (int i = 0; i < laberinto.length; i++) {
            for (int j = 0; j < laberinto[i].length; j++) {
                Color color;
                switch (laberinto[i][j]) {
                    case 1:
                        color = Color.BLACK; // Muro
                        break;
                    case 2:
                        color = Color.YELLOW; // Camino
                        break;
                    case 3:
                        color = Color.RED; // Salida
                        break;
                    default:
                        color = Color.CYAN; // Libre
                        break;
                }
                g2d.setColor(color);
                g2d.fillRect(j * CELDA_SIZE, i * CELDA_SIZE, CELDA_SIZE, CELDA_SIZE);

                // Dibujar contorno (borde) alrededor de la celda
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.drawRect(j * CELDA_SIZE, i * CELDA_SIZE, CELDA_SIZE, CELDA_SIZE);
            }
        }
    }

    public static void mostrarLaberintoEnVentana(int[][] laberinto) {
        JFrame frame = new JFrame("Laberinto");
        LaberintoGUI panel = new LaberintoGUI(laberinto);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null); // Centra la ventana
        frame.setVisible(true);
    }
}
