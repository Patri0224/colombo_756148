package db;

public class CreazioneAvvenutaEx extends Exception{
    public CreazioneAvvenutaEx(){
        super("Creazione DB avvenuta. Costruzione e riempimento tabelle in corso");
    }
}
