import Analyzers.LexicalAnalyzer;
import Analyzers.SymbolTable;
import Analyzers.SyntacticAnalyzer;
import exceptions.LexicalException;
import exceptions.SemanticException;
import exceptions.SyntacticException;
import sourcemanager.SourceManager;
import sourcemanager.SourceManagerImpl;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
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
            SymbolTable ts = new SymbolTable();
            SyntacticAnalyzer syn = new SyntacticAnalyzer(lex, ts);
            ts.checkDeclarations();
            ts.consolidateAllClasses();

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
        } catch (IOException e) {
            System.out.println("Error al cerrar el sourceManager");
        }
    }
}