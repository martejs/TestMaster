
package luceneindexer.files;

import java.io.InputStreamReader;
import java.io.File;
import java.io.FileInputStream;

//Class for opening the files for indexing
public class FileOpener {
    
    private String fileToIndex = "";

    private FileOpener() {
    }

    public FileOpener(String fileToIndex) {
        this.fileToIndex = fileToIndex;
    }
    
    public InputStreamReader getFileForReading(){
        
        InputStreamReader iStreamReader = null;
        
        try {
            iStreamReader = new InputStreamReader(new FileInputStream( new File(fileToIndex)), "UTF-8");
        } catch (Exception e){
            System.out.println(" Something went wrong trying to set up the file to read: " +  e.getClass() + " :: " + e.getMessage());
        }
        
        return iStreamReader;
        
        
    }
    
    
    
    
}
