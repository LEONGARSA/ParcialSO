import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Simulator {

    // Definición de la memoria, el acumulador y los registros
    private static int[] memory = new int[100]; // Array de memoria
    private static int accumulator = 0; // Registro acumulador
    private static int instructionCounter = 0; // Registro contador de instrucciones
    private static int addressRegister = 0; // Registro de dirección de memoria
    private static int memoryDataRegister = 0; // Registro de datos de memoria

    // Método principal que inicia la lectura del archivo
    public static void main(String[] args) {
        // Create JTextArea
        JTextArea textArea = new JTextArea(50, 120);
        textArea.setEditable(false);
        PrintStream printStream = new PrintStream(new JTextAreaOutputStream(textArea));

        // Create JScrollPane
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Create JFrame
        JFrame frame = new JFrame("Console output");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane);
        frame.pack();
        frame.setVisible(true);

        // Redirect standard output to text area
        System.setOut(printStream);

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            String inputFileName = fileChooser.getSelectedFile().getPath();
            readFile(inputFileName);
        } else {
            System.out.println("No se seleccionó ningún archivo.");
        }

    }
    // Método que lee el archivo y ejecuta las instrucciones
    private static void readFile(String inputFileName) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(inputFileName));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] instruction = line.split(" ");
                switch (instruction[0]) {
                    case "SET":
                        executeSetInstruction(instruction); // Ejecuta la instrucción SET
                        break;
                    case "LDR":
                        executeLoadInstruction(instruction); // Ejecuta la instrucción LDR
                        break;
                    case "ADD":
                        executeAddInstruction(instruction); // Ejecuta la instrucción ADD
                        break;
                    case "INC":
                        executeIncrementInstruction(instruction); // Ejecuta la instrucción INC
                        break;
                    case "DEC":
                        executeDecrementInstruction(instruction); // Ejecuta la instrucción DEC
                        break;
                    case "STR":
                        executeStoreInstruction(instruction); // Ejecuta la instrucción STR
                        break;
                    case "SHW":
                        executeShowInstruction(instruction); // Ejecuta la instrucción SHW
                        break;
                    case "PAUSE":
                        executePauseInstruction(instruction); // Ejecuta la instrucción PAUSE
                        break;
                    case "END":
                        executeEndInstruction(instruction); // Ejecuta la instrucción END
                        break;
                    default:
                        System.out.println("Invalid instruction: " + instruction[0]); // Maneja instrucciones inválidas
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Maneja excepciones de entrada/salida
        } finally {
            if (reader != null) {
                try {
                    reader.close(); // Cierra el lector
                } catch (IOException e) {
                    e.printStackTrace(); // Maneja excepciones de entrada/salida
                }
            }
        }
    }

    // Método que ejecuta la instrucción SET
    private static void executeSetInstruction(String[] instruction) {
        int address = Integer.parseInt(instruction[1].substring(1)); // Obtiene la dirección de la instrucción
        int value = Integer.parseInt(instruction[2]); // Obtiene el valor de la instrucción
        memory[address] = value; // Asigna el valor a la dirección de memoria
    }

    // Método que ejecuta la instrucción LDR
    private static void executeLoadInstruction(String[] instruction) {
        int address = Integer.parseInt(instruction[1].substring(1)); // Obtiene la dirección de la instrucción
        accumulator = memory[address]; // Carga el valor de la dirección de memoria en el acumulador
    }

    // Método que ejecuta la instrucción ADD
    private static void executeAddInstruction(String[] instruction) {
        int address = Integer.parseInt(instruction[1].substring(1)); // Obtiene la dirección de la instrucción
        accumulator += memory[address]; // Suma el valor de la dirección de memoria al acumulador
    }

    // Método que ejecuta la instrucción INC
    private static void executeIncrementInstruction(String[] instruction) {
        int address = Integer.parseInt(instruction[1].substring(1)); // Obtiene la dirección de la instrucción
        memory[address]++; // Incrementa el valor en la dirección de memoria
    }

    // Método que ejecuta la instrucción DEC
    private static void executeDecrementInstruction(String[] instruction) {
        int address = Integer.parseInt(instruction[1].substring(1)); // Obtiene la dirección de la instrucción
        memory[address]--; // Decrementa el valor en la dirección de memoria
    }

    // Método que ejecuta la instrucción STR
    private static void executeStoreInstruction(String[] instruction) {
        int address = Integer.parseInt(instruction[1].substring(1)); // Obtiene la dirección de la instrucción
        memory[address] = accumulator; // Almacena el valor del acumulador en la dirección de memoria
    }

    // Método que ejecuta la instrucción SHW
    private static void executeShowInstruction(String[] instruction) {
        int address = Integer.parseInt(instruction[1].substring(1)); // Obtiene la dirección de la instrucción
        System.out.println("Memory[" + address + "]: " + memory[address]); // Muestra el valor en la dirección de memoria
    }

    // Método que ejecuta la instrucción PAUSE
    private static void executePauseInstruction(String[] instruction) {
        // No se necesita hacer nada para la instrucción PAUSE
    }

    // Método que ejecuta la instrucción END
    private static void executeEndInstruction(String[] instruction) {
        // No se necesita hacer nada para la instrucción END
    }

    static class JTextAreaOutputStream extends OutputStream {
        private final JTextArea textArea;

        public JTextAreaOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }


        public void write(int b) throws IOException {
            // redirects data to the text area
            textArea.append(String.valueOf((char)b));
            // scrolls the text area to the end of data
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
    }
}