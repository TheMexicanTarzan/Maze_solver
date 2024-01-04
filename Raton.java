package laberinto;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Raton {

		public int contador = 0;
		private static int[] movX = {-1, -1, 0, 1, 1, 1, 0, -1};
	    private static int[] movY = {0, 1, 1, 1, 0, -1, -1, -1};
	    private static final int LIMITE_PASOS = 500000; // Establece el límite deseado
	    private boolean busquedaLarga = false;
	    static Stack<String> pasos = new Stack<>();
	    static Stack<String> pasos2 = new Stack<>();
	    static int startX = -1;
	    static int startY = -1;
	    static int endX = -1;
	    static int endY = -1;

	    public static char[][] laberinto;
	           
	    public void resuelve(String rutaArchivo) throws NumeroInvalidoException, DimensionesIncorrectasException {
	        int esquineado = cargarEsquineadoDesdeCSV(rutaArchivo);
	        
	        if (!buscarInicio() || !buscarSalida()) {
	            System.out.println("No startpoint or endpoint in the maze.");
	        } else if (!verificarLaberinto()) {
	            System.out.println("More than one entries or exits exist.");
	        } else {
	            if (esquineado == 4) {
	                BFS();
	            } else if (esquineado == 8) {
	                BFSOcho();
	            } else {
	                System.out.println("Invalid cornering value, must be 4 or 8.");
	            }
	        }
	    }

	    private boolean buscarInicio() {
	    //	 try {
		            for (int i = 0; i < laberinto.length; i++) {
		                for (int j = 0; j < laberinto[i].length; j++) {
		                    if (laberinto[i][j] == 'S') {
		                        endX = i;
		                        endY = j;
		                        return true;
		                    }
		                }
		            }
		            return false;
		  //      } catch (NullPointerException e) {
		   //         System.err.println("No se pudo obtener ninguna coordenada del laberinto, revise el formato del archivo");
		 //           return false; // Indicar que la búsqueda de inicio falló
		        }
	//    }
	    
	    private boolean verificarLaberinto() {
	       
	        int inicioCount = 0;
	        int salidaCount = 0;

	        for (int i = 0; i < laberinto.length; i++) {
	            for (int j = 0; j < laberinto[i].length; j++) {
	                if (laberinto[i][j] == 'S') {
	                    inicioCount++;
	                } else if (laberinto[i][j] == 'F') {
	                    salidaCount++;
	                }
	            }
	        }

	        return inicioCount == 1 && salidaCount == 1;
	    }
	   
	    private boolean buscarSalida() {
	    //	 try {
		            for (int i = 0; i < laberinto.length; i++) {
		                for (int j = 0; j < laberinto[i].length; j++) {
		                    if (laberinto[i][j] == 'F') {
		                        startX = i;
		                        startY = j;
		                        return true;
		                    }
		                }
		            }
		            return false;
		//        } catch (NullPointerException e) {
		 //           System.err.println("No se pudo obtener ninguna coordenada del laberinto, revise el formato del archivo");
		 //           return false; // Indicar que la búsqueda de inicio falló
		        }
	 //   }

	    private void BFS() {
	    for (int i = 0; i < laberinto.length; i++) {
	        for (int j = 0; j < laberinto[i].length; j++) {
	            if (laberinto[i][j] == 'F') {
	                startX = i;
	                startY = j;
	            }
	            if (laberinto[i][j] == 'S') {
	                endX = i;
	                endY = j;
	            }
	        }
	    }

	    Queue<Integer> colaX = new LinkedList<>();
	    Queue<Integer> colaY = new LinkedList<>();

	    colaX.add(startX);
	    colaY.add(startY);

	    int[] movX = {-1, 1, 0, 0};
	    int[] movY = {0, 0, -1, 1};

	    int[][] parentX = new int[laberinto.length][laberinto[0].length];
	    int[][] parentY = new int[laberinto.length][laberinto[0].length];

	    boolean[][] visitado = new boolean[laberinto.length][laberinto[0].length];

	    boolean encontrado = false;

	    while (!colaX.isEmpty() && !colaY.isEmpty()) {
	        int x = colaX.poll();
	        int y = colaY.poll();
	        visitado[x][y] = true;

	        if (contador >= LIMITE_PASOS) {
	            busquedaLarga = true;
	            break;
	        }

	       
	        if (x == endX && y == endY) {
	            encontrado = true;
	            break;
	        }

	        for (int i = 0; i < 4; i++) {
	            int newX = x + movX[i];
	            int newY = y + movY[i];

	            if (esValido(newX, newY) && !visitado[newX][newY]) {
	                colaX.add(newX);
	                colaY.add(newY);
	                parentX[newX][newY] = x;
	                parentY[newX][newY] = y;
	            }
	        }
	        contador++; 
	    }

	    if (busquedaLarga) {
	        System.out.println("Exit not found. Try with 8 corners.");
	        return;
	    }

	    if (encontrado) {
	        reconstruirCamino(parentX, parentY);
	    }
	}
	    
	    private void BFSOcho() {
	        for (int i = 0; i < laberinto.length; i++) {
	            for (int j = 0; j < laberinto[i].length; j++) {
	                if (laberinto[i][j] == 'F') {
	                    startX = i;
	                    startY = j;
	                }
	                if (laberinto[i][j] == 'S') {
	                    endX = i;
	                    endY = j;
	                }
	            }
	        }

	        Queue<Integer> colaX = new LinkedList<>();
	        Queue<Integer> colaY = new LinkedList<>();

	        colaX.add(startX);
	        colaY.add(startY);

	        int[][] distancia = new int[laberinto.length][laberinto[0].length];
	        for (int i = 0; i < laberinto.length; i++) {
	            Arrays.fill(distancia[i], Integer.MAX_VALUE);
	        }
	        distancia[startX][startY] = 0;

	        int[][] parentX = new int[laberinto.length][laberinto[0].length];
	        int[][] parentY = new int[laberinto.length][laberinto[0].length];

	        boolean encontrado = false;

	        while (!colaX.isEmpty() && !colaY.isEmpty()) {
	            int x = colaX.poll();
	            int y = colaY.poll();

	            if (x == endX && y == endY) {
	                encontrado = true;
	                break;
	            }

	            for (int i = 0; i < 8; i++) {
	                int newX = x + movX[i];
	                int newY = y + movY[i];

	                if (esValido(newX, newY) && distancia[newX][newY] == Integer.MAX_VALUE) {
	                    distancia[newX][newY] = distancia[x][y] + 1;
	                    colaX.add(newX);
	                    colaY.add(newY);
	                    parentX[newX][newY] = x;
	                    parentY[newX][newY] = y;
	                }
	            }
	            contador++;
	        }

	        if (busquedaLarga) {
	            System.out.println("Exit not found.");
	            return;
	        }

	        if (encontrado) {
	            reconstruirCamino(parentX, parentY);
	        } else {
	            System.out.println("Solution not found.");
	        }
	    }
	    
	    private boolean esValido(int x, int y) {
	        return x >= 0 && x < laberinto.length && y >= 0 && y < laberinto[0].length && (laberinto[x][y] == '0' || laberinto[x][y] == 'S');
	    }
	    
	    @Override
	    public String toString() {
	        String salida = "";
	        for (int x = 0; x < laberinto.length; x++) {
	            for (int y = 0; y < laberinto[x].length; y++) {
	                if (x == startX && y == startY) {
	                    salida += 'F' + " ";
	                }else if (laberinto[x][y] == '0') {
	                    salida += laberinto[x][y] + " ";
	                } else {
	                    char c = laberinto[x][y];
	                    switch (c) {
	                        case '1':
	                            salida += '1' + " "; // Abajo izquierda
	                            break;
	                        case '2':
	                            salida += '2' + " "; // Abajo
	                            break;
	                        case '3':
	                            salida += '3' + " "; // Abajo derecha
	                            break;
	                        case '4':
	                            salida += '4' + " "; // Izquierda
	                            break;
	                        case '6':
	                            salida += '6' + " "; // Derecha
	                            break;
	                        case '7':
	                            salida += '7' + " "; // Arriba izquierda
	                            break;
	                        case '8':
	                            salida += '8' + " "; // Arriba
	                            break;
	                        case '9':
	                            salida += '9' + " "; // Arriba derecha
	                            break;
	                        default:
	                            salida += c + " "; // Mantén otros caracteres
	                    }
	                }
	            }
	            salida += "\n";
	        }
	        return salida;
	    }
	 
	    private void reconstruirCamino(int[][] parentX, int[][] parentY) {
	        int x = endX;
	        int y = endY;

	        while (x != startX || y != startY) {
	            int prevX = parentX[x][y];
	            int prevY = parentY[x][y];

	            int direccion = 0;

	            if (prevX < x) {
	                if (prevY < y) {
	                    direccion = 7; // Arriba izquierda
	                } else if (prevY == y) {
	                    direccion = 8; // Arriba
	                } else if (prevY > y) {
	                    direccion = 9; // Arriba derecha
	                }
	            } else if (prevX == x) {
	                if (prevY < y) {
	                    direccion = 4; // Izquierda
	                } else if (prevY > y) {
	                    direccion = 6; // Derecha
	                }
	            } else if (prevX > x) {
	                if (prevY < y) {
	                    direccion = 1; // Abajo izquierda
	                } else if (prevY == y) {
	                    direccion = 2; // Abajo
	                } else if (prevY > y) {
	                    direccion = 3; // Abajo derecha
	                }
	            }

	            pasos.push(Integer.toString(direccion)); 

	            laberinto[x][y] = Character.forDigit(direccion, 10); 
	            x = prevX;
	            y = prevY;
	        }	
	    }
	    
	    public void cargarLaberintoDesdeArchivo(String rutaArchivo) throws DimensionesIncorrectasException {
	        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
	            String primeraLinea = reader.readLine();
	            if (primeraLinea == null) {
	                throw new DimensionesIncorrectasException("Information missing in the first line of the file.");
	            }

	            String[] dimensiones = primeraLinea.split(",");
	            if (dimensiones.length != 3) {
	                throw new DimensionesIncorrectasException("The first line of the file must contain three values separated by a comma (rows, columns, cornering value).");
	            }

	            int filas, columnas, esquinado;
	            try {
	                filas = Integer.parseInt(dimensiones[0]);
	                columnas = Integer.parseInt(dimensiones[1]);
	                esquinado = Integer.parseInt(dimensiones[2]);
	            } catch (NumberFormatException e) {
	                throw new DimensionesIncorrectasException("Rows, columns and cornering values must be valid integers.");
	            }

	            if (filas <= 0 || columnas <= 0) {
	                throw new DimensionesIncorrectasException("Dimensions must be positive integers.");
	            }

	            laberinto = new char[filas][columnas];

	            int fila = 0;
	            String linea;
	            while ((linea = reader.readLine()) != null && fila < filas) {
	                String[] valores = linea.split(",");
	                for (int columna = 0; columna < valores.length && columna < columnas; columna++) {
	                    if (valores[columna].length() != 1) {
	                        throw new DimensionesIncorrectasException("Error value in row " + (fila + 1) + ", column " + (columna + 1));
	                    }
	                    char c = valores[columna].charAt(0);
	                    laberinto[fila][columna] = c;

	                    // Verifica si es una posición de inicio ('S') o salida ('F')
	                    if (c == 'S') {
	                        startX = fila;
	                        startY = columna;
	                    } else if (c == 'F') {
	                        endX = fila;
	                        endY = columna;
	                    }
	                }
	                fila++;
	            }

	            if (fila < filas) {
	                throw new DimensionesIncorrectasException("Rows missing in file.");
	            }

	            if (startX == -1 || startY == -1) {
	                throw new DimensionesIncorrectasException("No startpoint ('S') in the maze.");
	            }

	            if (endX == -1 || endY == -1) {
	                throw new DimensionesIncorrectasException("No exit ('F') in the maze.");
	            }

	        } catch (IOException e) {
	            System.err.println("Error E/S: " + e.getMessage());
	            System.err.println("Error E/S loading the maze.");
	        }
	    }

	    public void guardarCoordenadasEnArchivo(List<String> coordenadas) {
	        try (PrintWriter writer = new PrintWriter("coordinates.txt")) {
	            String coordenadasCSV = String.join("\n", coordenadas);
	            writer.println(coordenadasCSV);
	            System.out.println("Coordinates saved in file 'coordinates.txt'");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    public int cargarEsquineadoDesdeCSV(String rutaArchivo) throws NumeroInvalidoException {
	        int esquineado = 0;

	        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
	            String primeraLinea = reader.readLine();
	            String[] valores = primeraLinea.split(",");
	            if (valores.length >= 3) {
	                try {
	                    esquineado = Integer.parseInt(valores[2]);
	                    if (esquineado != 4 && esquineado != 8) {
	                        throw new NumeroInvalidoException("Invalid cornering value, try 4 or 8.");
	                    }
	                } catch (NumberFormatException e) {
	                    throw new NumeroInvalidoException("Cornering value is not a valid integer.");
	                }
	            } else {
	                System.out.println("Error: No cornering value foind in CSV file.");
	            }
	        } catch (IOException e) {
	            System.err.println("Write a valid integer to solve the maze");
	        } catch (NumeroInvalidoException e) {
	            System.err.println(e.getMessage());
	        }

	        return esquineado;
	    }
	    
	    public static void main(String[] args) throws NumeroInvalidoException, IOException, DimensionesIncorrectasException, ArchivoNoEncontradoException {
	        Raton m = new Raton();
	        Scanner scanner = new Scanner(System.in);

	        String filePath;
	        boolean validFile = false;

	        do {
	            System.out.print("Enter maze file directory: ");
	            filePath = scanner.nextLine();

	            File file = new File(filePath);
	            if (file.exists() && file.isFile()) {
	                validFile = true;
	            } else {
	                System.out.println("File does not exist, try again.");
	            }
	        } while (!validFile);

	        try {
	            m.cargarLaberintoDesdeArchivo(filePath);
	        } catch (DimensionesIncorrectasException e) {
	            System.err.println("Error: " + e.getMessage());
	            validFile = false;
	        }

	        try { 
	        m.resuelve(filePath);
	        System.out.println(m);
	        	
	        }catch(NullPointerException e){
	        	System.err.println("Maze coordinates not found. Check file format");
	        	
	        }
	       

	        if (!pasos.empty()) {
	            System.out.println(pasos);
	            m.guardarCoordenadasEnArchivo(pasos);
	        } else {
	            System.out.println("No solution");
	        }
	        scanner.close();
	    }
}