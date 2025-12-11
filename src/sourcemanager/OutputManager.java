package sourcemanager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class OutputManager {
    private static BufferedWriter writer;
    private static OutputManager instance;

    private OutputManager(String fileName) {
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
        } catch (IOException e) {
            System.out.println("Error al crear el archivo: " + e.getMessage());
        }
    }

    public static OutputManager getInstance(String fileName){
        if(instance == null){
            instance = new OutputManager(fileName);
        }
        return instance;
    }

    public static OutputManager getInstance(){
        return instance;
    }

    public static void removeInstance(){
        instance = null;
    }

    public static void gen(String text) {
        try {
            writer.write(text);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        }
    }
}
