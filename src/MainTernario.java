import Analyzers.LexicalAnalyzer;
import Analyzers.SyntacticAnalyzerTer;
import exceptions.LexicalException;
import exceptions.SyntacticException;
import sourcemanager.SourceManager;
import sourcemanager.SourceManagerImpl;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainTernario {
    public static void main(String[] args) {
        if(args.length != 1){
            System.out.println("Error debe pasar 1 parametro (el archivo fuente)");
            System.exit(1);
        }

        String filePath = args[0];

        SourceManager sourceManager = new SourceManagerImpl();

        try{
            sourceManager.open(filePath);
            LexicalAnalyzer lex = new LexicalAnalyzer(sourceManager);
            SyntacticAnalyzerTer syn = new SyntacticAnalyzerTer(lex);

            System.out.println("Compilacion exitosa \n");
            System.out.println("[SinErrores]");



        }catch (FileNotFoundException e) {
            System.out.println("Error: No se encontro el archivo");
        } catch (IOException e) {
            System.out.println("Error al leer el archivo");
        } catch (LexicalException | SyntacticException e) {
            System.out.println(e.getMessage());
        }

        try {
            sourceManager.close();
        } catch (IOException e) {
            System.out.println("Error al cerrar el sourceManager");
        }
    }
}