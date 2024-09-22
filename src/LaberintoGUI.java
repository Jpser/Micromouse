import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;


public class LaberintoGUI extends JPanel {
    private int[][] maze;

    // Size of Cells in PX
    private static final int CELL_SIZE = 20;

    public LaberintoGUI(int[][] maze) {
        this.maze = maze;
        setPreferredSize(new Dimension(maze[0].length * CELL_SIZE, maze.length * CELL_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                Color color = switch (maze[i][j]) {
                    case 1 -> Color.BLACK; // Wall
                    case 2 -> Color.YELLOW; // Path
                    case 3 -> Color.RED; // Exits
                    case 4 -> Color.GREEN; // Start
                    default -> Color.CYAN; // Clean
                };
                g2d.setColor(color);
                g2d.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);

                // Draw cell borders
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.drawRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    public void showMazeInWindow(int[][] maze, int length) {
        JFrame frame = new JFrame("Maze "+ length);
        LaberintoGUI panel = new LaberintoGUI(maze);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null); // Center window
        frame.setVisible(true);
    }
}
