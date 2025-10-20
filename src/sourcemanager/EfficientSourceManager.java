/*package sourcemanager;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class EfficientSourceManager implements SourceManager{
    private BufferedReader reader;
    private int lineNumber;
    private int columnNumber;
    private int currentChar;

    public EfficientSourceManager(){
        currentChar = -1;
        lineNumber = 1;
        columnNumber = 0;
    }

    @Override
    public void open(String filePath) throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

        reader = new BufferedReader(inputStreamReader);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    @Override
    public char getNextChar() throws IOException{
        currentChar = reader.read();

        if(currentChar == -1){
            return END_OF_FILE;
        }

        char c = (char) currentChar;

        if(c == '\n'){
            lineNumber++;
            columnNumber = 0;
        } else {
            columnNumber++;
        }

        return c;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

}
*/