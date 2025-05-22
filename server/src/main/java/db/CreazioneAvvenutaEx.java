package db;
/**
 * Lanciata quando la creazione del {@code bookrecommenderdb} avviene correttamente
 */

class CreazioneAvvenutaEx extends Exception{
    public CreazioneAvvenutaEx(String messaggio){
        super(messaggio);
    }
}
