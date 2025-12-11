import Analyzers.LexicalAnalyzer;
import Analyzers.SymbolTable;
import Analyzers.SyntacticAnalyzer;
import exceptions.LexicalException;
import exceptions.SemanticException;
import exceptions.SyntacticException;
import sourcemanager.OutputManager;
import sourcemanager.SourceManager;
import sourcemanager.SourceManagerImpl;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if(args.length < 1 || args.length > 2){
            System.out.println("Error debe pasar por lo menos 1 parametro (el archivo fuente)");
            System.exit(1);
        }
        String outFilePath;
        if(args.length == 2){
             outFilePath = args[1];
        } else {
            outFilePath = "salida.txt";
            System.out.println("No se paso el archivo de salida. Se usara: " + outFilePath);
        }

        OutputManager outputManager = OutputManager.getInstance(outFilePath);
        String filePath = args[0];
        SourceManager sourceManager = new SourceManagerImpl();

        try{
            sourceManager.open(filePath);
            LexicalAnalyzer lex = new LexicalAnalyzer(sourceManager);
            SymbolTable ts = SymbolTable.getInstance();
            SyntacticAnalyzer syn = new SyntacticAnalyzer(lex, ts);
            ts.checkDeclarations();
            ts.consolidateAllClasses();
            ts.check();
            ts.generate();

            System.out.println("Compilacion exitosa \n");
            System.out.println("[SinErrores]");



        }catch (FileNotFoundException e) {
            System.out.println("Error: No se encontro el archivo");
        } catch (IOException e) {
            System.out.println("Error al leer el archivo");
        } catch (LexicalException | SyntacticException | SemanticException e) {
            System.out.println(e.getMessage());
        }

        try {
            sourceManager.close();
            outputManager.close();
        } catch (IOException e) {
            System.out.println("Error al cerrar el sourceManager");
        } finally {
            OutputManager.removeInstance();
            SymbolTable.removeInstance();
        }
    }
}