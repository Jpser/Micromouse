import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class EditorLaberinto {
    // Tamaño del laberinto
    static final int N = 16;
    static final int M = 16;
    static int[][] laberinto = new int[N][M];
    private JButton[][] botones;
    private boolean dibujando = false; // Estado de dibujo
    private boolean colocandoMuros = false; // Estado de colocación de muros
    private boolean colocandoEntrada = false; // Estado de colocación de entrada
    private boolean colocandoSalida = false; // Estado de colocación de salida
    private boolean entradaColocada = false; // Estado de si se ha colocado la entrada

    public void crearVentanaEditor() {
        JFrame frame = new JFrame("Editor de Laberinto");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(N, M));

        botones = new JButton[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                botones[i][j] = new JButton();
                botones[i][j].setPreferredSize(new Dimension(20, 20));
                laberinto = Laberinto.laberinto;
                if(Laberinto.laberinto[i][j] == 0) {
                    botones[i][j].setBackground(Color.WHITE);
                } else if (Laberinto.laberinto[i][j] == 1) {
                    botones[i][j].setBackground(Color.BLACK);
                } else if (Laberinto.laberinto[i][j] == 3) {
                    botones[i][j].setBackground(Color.RED);
                }else if (Laberinto.laberinto[i][j] == 4) {
                    botones[i][j].setBackground(Color.GREEN);
                }
                botones[i][j].setBorder(BorderFactory.createLineBorder(Color.GRAY));

                // Añadir MouseListener para manejar clics
                botones[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        dibujando = true; // Se está dibujando
                        JButton boton = (JButton) e.getSource();
                        updateCell(boton);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        dibujando = false; // Se deja de dibujar
                    }
                });

                // Añadir MouseMotionListener para manejar el arrastre
                botones[i][j].addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        if (dibujando) {
                            JButton boton = (JButton) e.getSource();
                            updateCell(boton);
                        }
                    }
                });

                // Añadir MouseEnteredListener para manejar el paso del ratón sobre la celda
                botones[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (dibujando) {
                            JButton boton = (JButton) e.getSource();
                            updateCell(boton);
                        }
                    }
                });

                panel.add(botones[i][j]);
            }
        }

        JPanel controlPanel = new JPanel();
        JButton resetButton = new JButton("Restablecer");
        JButton murosButton = new JButton("Colocar Muros");
        JButton entradaButton = new JButton("Colocar Entrada");
        JButton salidaButton = new JButton("Colocar Salidas");
        JButton copiarButton = new JButton("Generar Laberinto");

        // ActionListener para restablecer el laberinto
        resetButton.addActionListener(e -> {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    laberinto[i][j] = 0; // Camino libre
                    botones[i][j].setBackground(Color.WHITE);
                }
            }
            entradaColocada = false; // Reiniciar estado de entrada colocada
        });

        // ActionListener para activar/desactivar colocación de muros
        murosButton.addActionListener(e -> colocarMuros(!colocandoMuros));

        // ActionListener para activar/desactivar colocación de entrada
        entradaButton.addActionListener(e -> {
            if (entradaColocada) {
                JOptionPane.showMessageDialog(frame, "La entrada ya está colocada.");
            } else {
                colocarEntrada(!colocandoEntrada);
            }
        });

        // ActionListener para activar/desactivar colocación de salida
        salidaButton.addActionListener(e -> colocarSalida(!colocandoSalida));

        // ActionListener para copiar la matriz del laberinto
        copiarButton.addActionListener(e -> copiarLaberinto());

        controlPanel.add(resetButton);
        controlPanel.add(murosButton);
        controlPanel.add(entradaButton);
        controlPanel.add(salidaButton);
        controlPanel.add(copiarButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void updateCell(JButton boton) {
        int row = getButtonRow(boton);
        int col = getButtonCol(boton);

        if (colocandoMuros) {
            boton.setBackground(Color.BLACK);
            laberinto[row][col] = 1; // Muro
        } else if (colocandoEntrada) {
            if (!entradaColocada) {
                boton.setBackground(Color.GREEN);
                laberinto[row][col] = 4; // Entrada
                entradaColocada = true; // Marcar que la entrada ha sido colocada
                colocandoEntrada = false; // Desactivar modo de colocación de entrada
            } else {
                JOptionPane.showMessageDialog(null, "Ya se ha colocado una entrada.");
            }
        } else if (colocandoSalida) {
            boton.setBackground(Color.RED);
            laberinto[row][col] = 3; // Salida
        } else {
            boton.setBackground(Color.WHITE);
            laberinto[row][col] = 0; // Camino libre
        }
    }

    private int getButtonRow(JButton boton) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (botones[i][j] == boton) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int getButtonCol(JButton boton) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (botones[i][j] == boton) {
                    return j;
                }
            }
        }
        return -1;
    }

    private void colocarMuros(boolean estado) {
        colocandoMuros = estado;
        if (estado) {
            System.out.println("Modo de colocación de muros activado.");
        } else {
            System.out.println("Modo de colocación de muros desactivado.");
        }
    }

    private void colocarEntrada(boolean estado) {
        colocandoEntrada = estado;
        if (estado) {
            System.out.println("Modo de colocación de entrada activado.");
        } else {
            System.out.println("Modo de colocación de entrada desactivado.");
        }
    }

    private void colocarSalida(boolean estado) {
        colocandoSalida = estado;
        if (estado) {
            System.out.println("Modo de colocación de salidas activado.");
        } else {
            System.out.println("Modo de colocación de salidas desactivado.");
        }
    }

    private void copiarLaberinto() {
        // Copia la matriz del laberinto
        int[][] laberintoCopia = new int[N][M];
        for (int i = 0; i < N; i++) {
            System.arraycopy(laberinto[i], 0, laberintoCopia[i], 0, M);
        }

        // Puedes guardar esta copia en una variable estática o global accesible desde otras clases
        Laberinto.setLaberinto(laberintoCopia);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EditorLaberinto().crearVentanaEditor());
    }
}
