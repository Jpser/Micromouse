import javax.swing.*;

public class Laberinto {
    // Tamaño del laberinto
    static int N = 16;
    static int M = 16;
    static double thread = 0;
    static int[][] laberinto = new int[N][M];
    static LaberintoGUI laberintoGUI = new LaberintoGUI(laberinto);

    // Posición de la entrada y salida
    static int entradaX = 1, entradaY = 1;
    static int salidaX = 8, salidaY = 8;

    // Para almacenar la longitud de la mejor solución
    static int minCamino = Integer.MAX_VALUE;
    static int[][] mejorCamino;

    public static void inicializarLaberinto() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (i == j || i + j == N - 1 || (i == 0 && j >= 0 && j <= 8) || (i == N - 1 && j >= 18 && j <= 25)) {
                    laberinto[i][j] = 0; // Camino libre
                } else {
                    laberinto[i][j] = 1; // Muros
                }
            }
        }
        laberinto[entradaX][entradaY] = 4; // Entrada
        laberinto[salidaX][salidaY] = 3; // Salida
    }

    public static void mostrarLaberinto(int[][] lab) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (lab[i][j] == 1) {
                    System.out.print("█ "); // Muro
                } else if (lab[i][j] == 0) {
                    System.out.print("  "); // Camino libre
                } else if (lab[i][j] == 2) {
                    System.out.print(". "); // Camino recorrido
                } else if (lab[i][j] == 3) {
                    System.out.print("S "); // Salida
                }
            }
            System.out.println();
        }
    }

    public static int[] obtenerPosicionValor(int valorBuscado) {
        for (int i = 0; i < laberinto.length; i++) {
            for (int j = 0; j < laberinto[i].length; j++) {
                if (laberinto[i][j] == valorBuscado) {
                    return new int[]{i, j}; // Devuelve la posición como un array [i, j]
                }
            }
        }
        return null; // Devuelve null si no se encuentra el valor
    }

    public static void setLaberinto(int[][] nuevoLaberinto) {
        laberinto = nuevoLaberinto;
        // Buscar la posición del valor 4
        int[] posicion = obtenerPosicionValor(4);
        // Asignar a entradaX y entradaY
        entradaX = posicion[0];
        entradaY = posicion[1];
        posicion = obtenerPosicionValor(3);
        // Asignar a salidaX y salidaY
        salidaX = posicion[0];
        salidaY = posicion[1];
        // Cambiar el valor en esa posición a 0
        laberinto[entradaX][entradaY] = 0;

        System.out.println("Laberinto actualizado:");
        mostrarLaberinto(laberinto);
        jugar();
    }

    // Metodo recursivo de Flood Fill que acepta movimientos diagonales
    public static boolean floodFill(int x, int y, int longitud) {
        // Verifica si la posición actual es la salida
        if (x == salidaX && y == salidaY) {
            if (longitud < minCamino) {
                minCamino = longitud;
                mejorCamino = copiarLaberinto(laberinto); // Guarda la mejor ruta
            }
            return true;
        }

        // Verifica si está fuera de los límites del laberinto o es un muro o ya está visitado
        if (x < 0 || x >= N || y < 0 || y >= M || laberinto[x][y] != 0) {
            return false;
        }

        // Marca la posición actual como parte del camino
        laberinto[x][y] = 2;

        // Llama recursivamente en las ocho direcciones (incluyendo diagonales)
        boolean encontrado = false;

        // Define the directions array to prioritize movement towards the exit
        int[][] directions = {
                {salidaX > x ? 1 : -1, 0},  // Vertical towards the exit
                {0, salidaY > y ? 1 : -1},  // Horizontal towards the exit
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}, // Diagonals
                {0, 1}, {0, -1}, {1, 0}, {-1, 0}  // Rest of the directions
        };
        thread++;
        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            if (floodFill(newX, newY, longitud + 1)) {
                encontrado = true;

                // If a path has already been found, and it's shorter, stop further exploration
                if (longitud + 1 >= minCamino) {
                    break;
                }
            }
        }
        // Desmarca si no se encuentra un camino (backtracking)
        laberinto[x][y] = 0;
        //System.out.println("Threads: "+ thread);
        return encontrado;
    }

    public static int[][] copiarLaberinto(int[][] laberintoOriginal) {
        int[][] copia = new int[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                copia[i][j] = laberintoOriginal[i][j];
            }
        }
        return copia;
    }

    public static void jugar() {
        // Crea una copia del laberinto para el algoritmo floodFill
        copiarLaberinto(laberinto);
        minCamino = Integer.MAX_VALUE;
        mejorCamino = null;
        floodFill(entradaX, entradaY, 0);

        if (mejorCamino != null) {
            System.out.println("Mejor solución encontrada, mostrado en ventana");
            laberintoGUI.mostrarLaberintoEnVentana(mejorCamino);
            System.out.println("Longitud del mejor camino: " + minCamino);
        } else {
            System.out.println("No se encontró ninguna solución.");
        }
        thread = 0;
    }

    public static void main(String[] args) {
        // Inicializa el laberinto
        inicializarLaberinto();

        System.out.println("Laberinto inicial:");
        mostrarLaberinto(laberinto);
        // Abre el editor de laberinto
        SwingUtilities.invokeLater(() -> new EditorLaberinto().crearVentanaEditor());
    }
}
