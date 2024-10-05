import javax.swing.SwingUtilities;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Laberinto {
    // Tamaño del laberinto
    static int N = 32;
    static int M = 32;
    static int[][] maze = new int[N][M];

    static LaberintoGUI mazeGUI = new LaberintoGUI(maze);

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
            if (M >= 0) System.arraycopy(initialMaze[i], 0, maze[i], 0, M);
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
                } else if (maze[i][j] == 4) {
                    System.out.print("* "); // Salida
                } else if (maze[i][j] == 5) {
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

    public static void optimizedPath(int[][] optimizedMaze) {
        List<int[]> pathPositions = new ArrayList<>();

        // Traverse through the 2D array to find positions with value 2
        for (int i = 0; i < optimizedMaze.length; i++) {          // Iterate through rows
            for (int j = 0; j < optimizedMaze[i].length; j++) {    // Iterate through columns
                if (optimizedMaze[i][j] == 2) {                    // Check if the value is 2
                    pathPositions.add(new int[]{i, j});            // Add position to the list
                }
            }
        }

        int lenght = 1;
        for (int[] position : pathPositions) {
            floodFill(position[0], position[1], lenght);
            lenght++;
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
        int[][] auxPath = copyMaze(bestPath);
        showSolution(auxPath); //first solution
        clearMaze(bestPath);
        maze = bestPath;
        runMaze(); //with exit founded.
        int[][] auxPath2 = bestPath != null ? copyMaze(bestPath) : maze;
        showSolution(auxPath2);//improved solution
        int[][] mazeToOptimize = bestPath != null ? copyMaze(bestPath) : maze;
        clearMaze(bestPath);
        maze = bestPath;
        copyMaze(maze);
        pathMin = Integer.MAX_VALUE;
        bestPath = null;
        optimizedPath(mazeToOptimize);
        int[][] auxPath3 = bestPath != null ? copyMaze(bestPath) : maze;
        showSolution(auxPath3); //new best solution.
    }

    // Metodo recursivo de Flood Fill que acepta movimientos diagonales
    public static boolean floodFill(int x, int y, int length) {
        // Verify limits of maze.
        if (x < 0 || x >= N || y < 0 || y >= M) {
            return false;
        }

        // Verify if is the exit.
        if (maze[x][y] == 3) {
            pathMin = length;
            bestPath = copyMaze(maze); // Guarda la mejor ruta
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
        if (exitXFound != -1 && exitYFound != -1) {
            directions = getDirections(x, y);
        } else {
            directions = new int[][]{
                    {1, 0}, {-1, 0},
                    {0, 1}, {0, -1},
                    {-1, -1}, {1, 1},
                    {-1, 1}, {1, -1},
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
        // Prioritize the movement direction towards the exit based on current and exit positions

        int deltaX = Integer.compare(exitXFound, x); // -1 if exit is to the left, 1 if to the right, 0 if aligned
        int deltaY = Integer.compare(exitYFound, y); // -1 if exit is above, 1 if below, 0 if aligned

        if (x == exitXFound && y == exitYFound) {
            return new int[0][0]; // Return an empty array to indicate no further movement needed
        }
        // Create the direction priorities based on calculated deltas
        return new int[][]{
                {deltaX, deltaY},         // Move diagonally towards the exit
                {deltaX, 0},              // Move horizontally towards the exit
                {0, deltaY},              // Move vertically towards the exit
                {deltaX, deltaY == 0 ? 1 : deltaY},  // Adjust diagonals for cases when deltaY is zero
                {deltaX == 0 ? 1 : deltaX, deltaY},  // Adjust diagonals for cases when deltaX is zero
                {deltaX, -deltaY},        // Opposite diagonal
                {-deltaX, deltaY},        // Opposite horizontal or vertical
                {deltaX == 0 ? 1 : deltaX, 0}, // Straight horizontal
                {0, deltaY == 0 ? 1 : deltaY},  // Straight vertical
                {1, 0}, {0, 1}
        };
    }

    public static int[][] copyMaze(int[][] originalMaze) {
        int[][] copy = new int[N][M];
        for (int i = 0; i < N; i++) {
            if (M >= 0) System.arraycopy(originalMaze[i], 0, copy[i], 0, M);
        }
        return copy;
    }

    public static void runMaze() {
        // Crea una copia del laberinto para el algoritmo floodFill
        copyMaze(maze);
        pathMin = Integer.MAX_VALUE;
        bestPath = null;
        floodFill(startX, startY, 0);
    }

    private static void showSolution(int[][] bestPath) {
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
