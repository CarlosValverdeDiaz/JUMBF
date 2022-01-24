package Common;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
/**
 *
 * @author carlos
 */
public class FileToHex {
    


    private static final String NEW_LINE = System.lineSeparator();
    private static final String UNKNOWN_CHARACTER = ".";
    

    public String convertFileToHex(Path path) throws IOException {        
        
        if (Files.notExists(path)) {
            throw new IllegalArgumentException("File not found! " + path);
        }
        
        StringBuilder result = new StringBuilder();
        StringBuilder hex = new StringBuilder();
        StringBuilder input = new StringBuilder();

        int countLine = 0;
        int value;
        int markerFinder[] = new int[2];
        int marker;

        ValuesSorter vs = new ValuesSorter();
        
        vs.initMap();
        
        // path to inputstream....
        try (InputStream inputStream = Files.newInputStream(path)) {
            while ((value = inputStream.read()) != -1) { 
                
                markerFinder[1] = markerFinder[0];
                markerFinder[0] = value;
                marker = markerFinder[1]*256 + markerFinder[0];
                
                hex.append(String.format("%02X ", value));

                if(marker >= 0xFFC0){
                    hex.append("\n");
                    countLine = 0;
                    hex.append("marker: " + vs.sortByValue(marker) + "\n");
                    
                }
             
                if(marker == Values.END_OF_IMAGE){
                        return result.toString();
                }
                //If the character is unable to convert, just prints a dot "."
                
                
                // After 15 bytes, reset everything for formatting purpose
                if (countLine == 14) {
                    result.append(String.format("%-60s\n", hex));
                    //result.append(String.format("%-60s", hex));
                    hex.setLength(0);
                    input.setLength(0);
                    countLine = 0;
                } else {
                    countLine++;
                }
            }

            // if the count>0, meaning there is remaining content
            if (countLine > 0) {
                result.append(String.format("%-60s\n", hex));
                result.append(String.format("%-60s", hex));
            }

        }
        return result.toString();
    }
    
    public String convertBoxToHex(byte[] data) throws IOException, Exception {        
        
        StringBuilder result = new StringBuilder();
        StringBuilder hex = new StringBuilder();
        StringBuilder input = new StringBuilder();

        int countLine = 0;
        int value;
        int markerFinder[] = new int[2];
        int marker;

        ValuesSorter vs = new ValuesSorter();
        
        vs.initMap();
        
        for (int i = 0; i < data.length; i++) {
            value = Byte.toUnsignedInt(data[i]);
            
            markerFinder[1] = markerFinder[0];
            markerFinder[0] = value;
            marker = markerFinder[1]*256 + markerFinder[0];

            hex.append(String.format("%02X ", value));

            if(marker >= 0xFFC0){
                hex.append("\n");
                countLine = 0;
                hex.append("marker: " + vs.sortByValue(marker) + "\n");

            }

            if(marker == Values.END_OF_IMAGE){
                    return result.toString();
            }
            //If the character is unable to convert, just prints a dot "."


            // After 15 bytes, reset everything for formatting purpose
            if (countLine == 14) {
                result.append(String.format("%-60s\n", hex));
                //result.append(String.format("%-60s", hex));
                hex.setLength(0);
                input.setLength(0);
                countLine = 0;
            } else {
                countLine++;
            }
        }

        // if the count>0, meaning there is remaining content
        if (countLine > 0) {
            result.append(String.format("%-60s\n", hex));
            result.append(String.format("%-60s", hex));
        }
        return result.toString();
    }
}

