/*
 * ~~~ erstmal fertig ~~~
 * 
 * 
 */
package Terminkalender;

import java.io.Serializable;

/**
 *
 * @author Tim Meyer
 */
public class Zeit implements Serializable{
    private int stunde;
    private int minute;
    
    Zeit(int stunde, int minute) throws ZeitException{
        if(stunde > 23 || stunde < 0){
            throw new ZeitException("Stunde zwischen 0 und 23 wählen");
        }
        this.stunde = stunde;
      
        if(minute > 59 || minute < 0){
            throw new ZeitException("Minute zwischen 0 und 59 wählen");
        }
        this.minute = minute;
    }

    //Setter:
    private void setStunde(int stunde) throws ZeitException{
        if(stunde > 23 || stunde < 0){
            throw new ZeitException("Stunde zwischen 0 und 23 wählen");
        }
        this.stunde = stunde;
    }
    private void setMinute(int minute) throws ZeitException{
        if(minute > 59 || minute < 0){
            throw new ZeitException("Minute zwischen 0 und 59 wählen");
        }
        this.minute = minute;
    }
    
    //Getter:
    private int getStunde(){
        return stunde;
    }
    private int getMinute(){
        return minute;
    }
    
    
    /**
     * Exception-Klasse für Klasse Zeit
     */
    public static class ZeitException extends Exception implements Serializable{
        
        private final String message;
        
        public ZeitException(String message) {
            this.message = message;
        }
        
        @Override
        public String getMessage(){
            return message;
        }
    }
}
