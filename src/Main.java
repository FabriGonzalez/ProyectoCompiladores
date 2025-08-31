import exceptions.LexicalException;
import sourcemanager.SourceManager;
import sourcemanager.SourceManagerImpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

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

            Token token = null;
            boolean hasErrors = false;
            do{
                try{
                    token = lex.nextToken();
                    System.out.println(token);
                }catch (LexicalException e) {
                    System.out.println(e.getMessage());
                    hasErrors = true;
                    token = new Token("error", "error", -1);
                }
            }while(!Objects.equals(token.getId(), "EOF"));

            if(!hasErrors){
                System.out.println("\n[SinErrores]");
            }


        }catch (FileNotFoundException e) {
            System.out.println("Error: No se encontro el archivo");
        } catch (IOException e) {
            System.out.println("Error al leer el archivo");
        }

        try {
            sourceManager.close();
        } catch (IOException e) {
            System.out.println("Error al cerrar el sourceManager");
        }
    }
}