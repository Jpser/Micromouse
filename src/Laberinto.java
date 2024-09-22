import javax.swing.SwingUtilities;
import java.time.Duration;
import java.time.Instant;

public class Laberinto {
    // Tamaño del laberinto
    static int N = 32;
    static int M = 32;
    static int[][] maze = new int[N][M];
    static LaberintoGUI mazeGUI = new LaberintoGUI(maze);
    static Duration maxDuration = Duration.ofMinutes(5);
    static Instant startTime;

    // Posición de la entrada y salida
    static int startX = 1, startY = 1;
    static int exitX = 8, exitY = 8;

    // Para almacenar la longitud de la mejor solución
    static int pathMin = Integer.MAX_VALUE;
    static int[][] bestPath;

    public static void initMaze() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (i == j || i + j == N - 1 || (i == 0 && j >= 0 && j <= 8) || (i == N - 1 && j >= 18 && j <= 25)) {
                    maze[i][j] = 0; // Camino libre
                } else {
                    maze[i][j] = 1; // Muros
                }
            }
        }
        maze[startX][startY] = 4; // Entrada
        maze[exitX][exitY] = 3; // Salida
    }

    public static void showMaze(int[][] lab) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (lab[i][j] == 1) {
                    System.out.print("█ "); // Muro
                } else if (lab[i][j] == 0) {
                    System.out.print(". "); // Camino libre
                } else if (lab[i][j] == 2) {
                    System.out.print("* "); // Camino recorrido
                } else if (lab[i][j] == 3) {
                    System.out.print("S "); // Salida
                }else if (lab[i][j] == 4) {
                    System.out.print("* "); // Salida
                }
            }
            System.out.println();
        }
    }

    public static int[] getPositionValue(int valueToSearch) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == valueToSearch) {
                    return new int[]{i, j}; // Devuelve la posición como un array [i, j]
                }
            }
        }
        return null; // Devuelve null si no se encuentra el valor
    }

    public static void setMaze(int[][] newMaze) {
        maze = newMaze;
        // Buscar la posición del valor 4
        int[] position = getPositionValue(4);
        // Asignar a entradaX y entradaY
        startX = position[0];
        startY = position[1];
        position = getPositionValue(3);
        // Asignar a salidaX y salidaY
        exitX = position[0];
        exitY = position[1];

        System.out.println("Maze to solve:");
        showMaze(maze);
        runMaze();
    }

    // Metodo recursivo de Flood Fill que acepta movimientos diagonales
    public static boolean floodFill(int x, int y, int longitud) {

        if (Duration.between(startTime, Instant.now()).compareTo(maxDuration) > 0) {
            System.out.println("Time limit exceeded. Stopping execution.");
            return true; // Return true to stop the current branch.
        }

        //To prune longest paths.
        if (longitud > pathMin - 1) {
            return true;
        }

        // Verifica si la posición actual es la salida
        if (x == exitX && y == exitY) {
            pathMin = longitud;
            bestPath = copyMaze(maze); // Guarda la mejor ruta
            mazeGUI.showMazeInWindow(bestPath, pathMin);
            return true;
        }

        // Verifica si está fuera de los límites del laberinto o es un muro o ya está visitado
        if (x < 0 || x >= N || y < 0 || y >= M || (maze[x][y] != 0  && maze[x][y] != 4)) {
            return false;
        }

        // Si no es la entrada, marca la posición actual como parte del camino
        int isPath = maze[x][y] == 4 ? 4 : 2;
        maze[x][y] = isPath;

        // Llama recursivamente en las ocho direcciones (incluyendo diagonales)
        boolean finded = false;

        // Define the directions array to prioritize movement towards the exit
        int[][] directions = {
                {0, exitY > y ? 1 : -1},  // Horizontal towards the exit
                {exitX > x ? 1 : -1, 0},  // Vertical towards the exit
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}, // Diagonals
                {0, 1}, {0, -1}, {1, 0}, {-1, 0}  // Rest of the directions
        };
        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            if (floodFill(newX, newY, longitud + 1)) {
                finded = true;

                // If a path has already been found, and it's shorter, stop further exploration
                if (longitud + 1 >= pathMin) {
                    break;
                }
            }
        }
        // Desmarca si no se encuentra un camino (backtracking)
        maze[x][y] = 0;
        return finded;
    }

    public static int[][] copyMaze(int[][] originalMaze) {
        int[][] copy = new int[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                copy[i][j] = originalMaze[i][j];
            }
        }
        return copy;
    }

    public static void runMaze() {
        // Crea una copia del laberinto para el algoritmo floodFill
        startTime = Instant.now(); // Set start time before floodFill begins
        copyMaze(maze);
        pathMin = Integer.MAX_VALUE;
        bestPath = null;
        floodFill(startX, startY, 0);

        if (bestPath != null) {
            System.out.println("Best solution founded, showing in window");
            mazeGUI.showMazeInWindow(bestPath, pathMin);
            System.out.println("Best Path Length: " + pathMin);
        } else {
            System.out.println("Not solution founded.");
        }
    }

    public static void main(String[] args) {
        // Inicializa el laberinto
        initMaze();
        // Abre el editor de laberinto
        SwingUtilities.invokeLater(() -> new EditorLaberinto().crearVentanaEditor());
    }
}
