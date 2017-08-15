/*
 * ~~~ erstmal fertig ~~~
 * 
 * 
 */
package Terminkalender;

/**
 *
 * @author Tim Meyer
 */
public enum Monat {
    Januar(1),
    Februar(2), 
    Maerz(3),
    April(4),
    Mai(5),
    Juni(6),
    Juli(7),
    August(8),
    September(9),
    Oktober(10),
    November(11),
    Dezember(12);
    
    private final int wert;
    
    private Monat(int wert){
        this.wert = wert;
    }    
    
    public int getWert(){
        return wert;
    }    
}
