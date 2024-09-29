import javax.swing.SwingUtilities;
import java.time.Duration;
import java.time.Instant;

public class Laberinto {
    // Tamaño del laberinto
    static int N = 32;
    static int M = 32;
    static int[][] maze = new int[N][M];

    static LaberintoGUI mazeGUI = new LaberintoGUI(maze);
    static Duration maxDuration = Duration.ofMinutes(1);
    static Instant startTime;

    // Posición de la entrada y salida
    static int startX = 1, startY = 1;
    static int exitX = 8, exitY = 8;
    static int exitXFound = -1, exitYFound = -1;

    // Para almacenar la longitud de la mejor solución
    static int pathMin = Integer.MAX_VALUE;
    static int[][] bestPath;

    //region initial maze
    static int[][] initialMaze = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1},
            {1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1},
            {1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1},
            {1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1},
            {1, 1, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1},
            {1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1},
            {1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0, 1, 1, 0, 1, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 0, 1, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };
    //endregion

    public static void initMaze() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                maze[i][j] = initialMaze[i][j];
            }
        }
        maze[startX][startY] = 4; // Entrada
        maze[exitX][exitY] = 3; // Salida
    }

    public static void showMaze(int[][] maze) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (maze[i][j] == 1) {
                    System.out.print("█ "); // Muro
                } else if (maze[i][j] == 0) {
                    System.out.print(". "); // Camino libre
                } else if (maze[i][j] == 2) {
                    System.out.print("* "); // Camino recorrido
                } else if (maze[i][j] == 3) {
                    System.out.print("S "); // Salida
                }else if (maze[i][j] == 4) {
                    System.out.print("* "); // Salida
                }else if (maze[i][j] == 5) {
                    System.out.print("X "); // Salida
                }
            }
            System.out.println();
        }
    }

    public static int[] getPositionValue(int valueToSearch) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (maze[i][j] == valueToSearch) {
                    return new int[]{i, j}; // Devuelve la posición como un array [i, j]
                }
            }
        }
        return null; // Devuelve null si no se encuentra el valor
    }

    public static void clearMaze(int[][] maze) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (maze[i][j] == 2 || maze[i][j] == 5) {
                    maze[i][j] = 0;
                }
            }
        }
    }

    public static void reduceMaze(int[][] maze) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze.length; j++) {
                if (maze[i][j] == 2) {
                    maze[i][j] = 0;
                }
                if (maze[i][j] == 5) {
                    maze[i][j] = 1;
                }
            }
        }
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
        exitXFound = -1;
        exitYFound = -1;
        runMaze();
        clearMaze(bestPath);
        maze = bestPath;
        runMaze(); //with exit finded.
        reduceMaze(bestPath);
        maze = bestPath;
        runMaze();
        showSolution();
    }

    // Metodo recursivo de Flood Fill que acepta movimientos diagonales
    public static boolean floodFill(int x, int y, int length) {

        if (Duration.between(startTime, Instant.now()).compareTo(maxDuration) > 0) {
            System.out.println("Time limit exceeded. Stopping execution.");
            return true; // Return true to stop the current branch.
        }
        // Verify limits of maze.
        if (x < 0 || x >= N || y < 0 || y >= M ) {
            return false;
        }

        // Verify if is the exit.
        if (maze[x][y] == 3) {
            pathMin = length;
            bestPath = copyMaze(maze); // Guarda la mejor ruta
            startTime = Instant.now();
            exitXFound = x;
            exitYFound = y;
            return true;
        }

        // Verify that is not path or start.
        if (maze[x][y] != 0 && maze[x][y] != 4) {
            return false;
        }

        // Si no es la entrada, marca la posición actual como parte del camino
        int isPath = maze[x][y] == 4 ? 4 : 2;
        maze[x][y] = isPath;

        // Define the directions array to prioritize movement towards the exit
        boolean found = false;
        int[][] directions;
        if(exitXFound != -1){
             directions = getDirections(x, y);
        }else {
            directions = new int[][]{
                    {1, 0}, {-1, 0},
                    {0, 1}, {0, -1},
                    {-1, -1}, {1, 1},
                    {-1, 1},{1, -1},
            };
        }

        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            if (floodFill(newX, newY, length + 1)) {
                found = true;
            }
        }

        int isBackTracking = maze[x][y] == 4 ? 4 : 5;
        maze[x][y] = isBackTracking;
        return found;
    }

    private static int[][] getDirections(int x, int y) {
        int[][] directions;
        if(exitX == x){
            directions= new int[][] {
                    {0, exitYFound > y ? 1 : -1},
                    {exitXFound > x ? 1 : -1, 0},
                    {1, 0}, {-1, 0},  // Rest of the directions
                    {0, 1}, {0, -1},
                    {exitXFound > x ? 1 : -1, exitYFound > y ? 1 : -1}
            };
        }
        if(exitY == y){
            directions= new int[][] {
                    {exitXFound > x ? 1 : -1, 0},
                    {0, exitYFound > y ? 1 : -1},
                    {1, 0}, {-1, 0},
                    {0, 1}, {0, -1},
                    {exitXFound > x ? 1 : -1, exitYFound > y ? 1 : -1}
            };
        }
        else{
            directions= new int[][]{
                    {exitX > x ? 1 : -1, exitY > y ? 1 : -1},
                    {0, exitY > y ? 1 : -1},
                    {1, 0}, {-1, 0},  // Rest of the directions
                    {0, 1}, {0, -1},
                    {exitX > x ? 1 : -1, 0},
            };
        }
        return directions;
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
    }

    private static void showSolution() {
        if (bestPath != null) {
            System.out.println("Best solution founded, showing in window");
            mazeGUI.showMazeInWindow(bestPath, pathMin);
            System.out.println("Best Path Length: " + pathMin);
        } else {
            System.out.println("Not solution founded.");
        }
    }

    public static void main(String[] args) {
        // Initialize maze
        initMaze();
        // Open maze in editor.
        SwingUtilities.invokeLater(() -> new EditorLaberinto().crearVentanaEditor());
    }
}
