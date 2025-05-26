/*
 * Autori del progetto:
 * Nome: Colombo     Cognome: Patrizio     Matricola: 756148     Sede: CO
 * Nome: Felitti     Cognome: Fabio        Matricola: 758058     Sede: CO
 * Nome: Franchi     Cognome: Matteo       Matricola: 757075     Sede: CO
 * Nome: Iacono      Cognome: Alessandro   Matricola: 758451     Sede: CO
 */
package db;
/**
 * Lanciata quando la creazione del {@code bookrecommenderdb} avviene correttamente
 */

class CreazioneAvvenutaEx extends Exception{
    public CreazioneAvvenutaEx(String messaggio){
        super(messaggio);
    }
}
