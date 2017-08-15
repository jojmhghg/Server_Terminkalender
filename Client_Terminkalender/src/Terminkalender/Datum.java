/*
 * TODO: Exception-Texte verbessern, evtl Methode um Monat von int zu String umwandeln
 *
 *
 */
package Terminkalender;

/**
 *
 * @author Tim Meyer
 */
public class Datum {
    private int tag;
    private int monat;
    private int jahr;
    
    public Datum(int tag, Monat monat, int jahr) throws DatumException{

        if(!existiertTagInMonat(tag, monat.getWert(), jahr)){
            throw new DatumException("Monat " + monat + " hat keinen Tag " + tag);
        }
        this.tag = tag;
        this.monat = monat.getWert();
        this.jahr = jahr;
    }

    //Setter:
    public void setTag(int tag) throws DatumException{
        if(!existiertTagInMonat(tag, monat, jahr)){
            throw new DatumException("Monat " + monat + " hat keinen Tag " + tag);
        }
        this.tag = tag;
    }
    public void setMonat(Monat monat){
        this.monat = monat.getWert();
    }
    public void setJahr(int jahr){
        this.jahr = jahr;
    }
    
    //Getter:
    public int getTag(){
        return tag;
    }    
    public int getMonat(){
        return monat;
    }    
    public int getJahr(){
        return jahr;
    }
    /**
     * gibt die Kalenderwoche des Datums zurück
     * 
     * @return 
     */
    public int getKalenderwoche(){
        int kalenderwoche = 0;
        
        int wochentag = getWochentag();
        int x = getTagDesJahres() + 4 - wochentag;
        while(x > 0){
            x -= 7;
            kalenderwoche++;
        }

        
        return kalenderwoche;
    }
    /**
     * Gibt zurück der wievielte Tag des Jahres an dem Datum ist
     * 
     * @return 
     */
    public int getTagDesJahres(){
        int tagDesJahres;
        
        tagDesJahres = (monat - 1) * 31;
        if((monat - 1) % 2 == 0 && monat > 1){
            tagDesJahres -= (monat - 1) / 2;
        }
        else{
            tagDesJahres -= (monat - 2) / 2;
        }
        if(istSchaltjahr(jahr) && (monat - 1) >= 2){
            tagDesJahres -= 1;
        }
        if(!istSchaltjahr(jahr) && (monat - 1) >= 2){
            tagDesJahres -= 2;
        }
        tagDesJahres += tag;
        
        return tagDesJahres;
    }
    /**
     * gibt Wochentag als Zahl (1-7) des Datums zurück
     * 
     * @return 
     */
    public int getWochentag(){
        int wochentag;
        
        int f = (14 - monat) / 12;
        int y = jahr - f;
        int m = monat + 12 * f - 2;
        wochentag = (tag + y + 31 * m / 12 + y / 4 - y / 100 + y / 400) % 7;
        
        return wochentag;
    }
    
    
    /**
     * testet ob übergebener Tag im Monat 'monat' und Jahr 'jahr' existiert
     * 
     * @param tag
     * @param monat
     * @param jahr
     * @return 
     */
    private boolean existiertTagInMonat(int tag, int monat, int jahr){
        switch(monat % 2){
            case 1:
                if(tag > 31 || tag < 1){
                    return false;
                }
                break;
                
            case 0:
                /* Februar mehr als 29 Tage? */
                if(monat == 2 && tag > 29){
                    return false;
                }
                /* Februar mehr als 28 Tage, wenn kein Schaltjahr? */
                if(monat == 2 && !istSchaltjahr(jahr) && tag > 28){
                    return false;
                }
                if(tag > 30 || tag < 1){
                    return false;
                }
                break;

            default:

                break; 
        }
        
        return true;
    }
    
    /**
     * test ob das als Argument übergebene Jahr ein Schaltjahr ist
     * 
     * @param jahr
     * @return 
     */
    private boolean istSchaltjahr(int jahr){
        boolean result = false;
        
        if(jahr % 4 == 0 && jahr % 100 != 0){
            result = true;
        }
        if(jahr % 400 == 0){
            result = true;
        }
        
        return result;
    }
    
    /**
     * Exception-Klasse für Klasse Datum
     */
    public static class DatumException extends Exception {

        private final String message;
        
        public DatumException(String message) {
            this.message = message;
        }
        
        @Override
        public String getMessage(){
            return message;
        }
    }
}
