package bookRecommender.entita;

import java.io.Serializable;
import java.util.Arrays;


public class Libri implements Serializable {
    private static int LastId = 0;
    private int Id;
    private String Titolo = "";
    private String[] Autori;
    private String Descrizione = "";
    private String[] Categorie;
    private String Editore = "";
    private float PrezzoPartenza = 0;
    private short MesePubblicazione = 0;
    private int AnnoPubblicazione = 0;
    private static float CambioInEuro = 0.92F;


    public Libri() {

        Titolo = "";
        Autori = new String[1];
        Descrizione = "";
        Categorie = new String[1];
        Editore = "";
        PrezzoPartenza = 0;
        MesePubblicazione = 0;
        AnnoPubblicazione = 0;
    }
    public Libri(String titolo, String[] autori, String descrizione, String[] categorie, String editore, float prezzoPartenza, short mesePubblicazione, int annoPubblicazione) {
        LastId++;
        Id = LastId;
        Titolo = titolo;
        Autori = autori;
        Descrizione = descrizione;
        Categorie = categorie;
        Editore = editore;
        PrezzoPartenza = prezzoPartenza;
        MesePubblicazione = mesePubblicazione;
        AnnoPubblicazione = annoPubblicazione;
    }


    /**
     * restituisce il numero del mese partendo dalla stringa
     *
     * @param mese: in inglese con lettara iniziale maiuscola
     * @return numero del mese partendo da 1 e 0 se invalido
     */
    public static short meseStringToShort(String mese) {
        Short num;
        switch (mese) {
            case "January":
                return num = 1;
            case "February":
                return num = 2;
            case "Mmarch":
                return num = 3;
            case "April":
                return num = 4;
            case "May":
                return num = 5;
            case "June":
                return num = 6;
            case "July":
                return num = 7;
            case "August":
                return num = 8;
            case "September":
                return num = 9;
            case "October":
                return num = 10;
            case "November":
                return num = 11;
            case "December":
                return num = 12;
            default:
                return num = 0;
        }
    }

    /**
     * restituisce la stringa del mese partendo dal numero
     *
     * @param mese: in numero con lettara iniziale maiuscola
     * @return nome del mese in inglese con lettara iniziale maiuscola
     */
    public static String meseShortTostring(short mese) {
        switch (mese) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "";
        }
    }

    @Override
    public String toString() {
        return "Libro{" +
                "Id=" + Id +
                ", Titolo='" + Titolo + '\'' +
                ", Autori=" + Arrays.toString(Autori) +
                ", Descrizione='" + Descrizione + '\'' +
                ", Categorie=" + Arrays.toString(Categorie) +
                ", Editore='" + Editore + '\'' +
                ", PrezzoPartenza=" + PrezzoPartenza +
                ", MesePubblicazione=" + Libri.meseShortTostring(MesePubblicazione) +
                ", AnnoPubblicazione=" + AnnoPubblicazione +
                '}';
    }

    //inizio getter
    public int getId() {
        return Id;
    }

    public String getTitolo() {
        return Titolo;
    }

    public String[] getAutoriArray() {
        return Autori;
    }

    public String getAutoriString() {
        return Arrays.toString(Autori);
    }

    public String getDescrizione() {
        return Descrizione;
    }

    public String[] getCategorieArray() {
        return Categorie;
    }

    public String getCategorieString() {
        return Arrays.toString(Categorie);
    }

    public String getEditore() {
        return Editore;
    }

    public float getPrezzoPartenzaDollari() {
        return PrezzoPartenza;
    }

    public float getPrezzoPartenzaEuro() {
        return PrezzoPartenza * CambioInEuro;
    }

    public String getMesePubblicazione() {
        return meseShortTostring(this.MesePubblicazione);
    }

    public int getAnnoPubblicazione() {
        return AnnoPubblicazione;
    }


    //inizio setter
    public void setTitolo(String titolo) {
        Titolo = titolo;
    }

    public void setAutori(String[] autori) {
        Autori = autori;
    }

    public void setDescrizione(String descrizione) {
        Descrizione = descrizione;
    }

    public void setCategorie(String[] categorie) {
        Categorie = categorie;
    }

    public void setEditore(String editore) {
        Editore = editore;
    }

    public void setPrezzoPartenza(float prezzoPartenza) {
        PrezzoPartenza = prezzoPartenza;
    }

    public void setMesePubblicazioneShort(short mesePubblicazione) {
        MesePubblicazione = mesePubblicazione;
    }

    public void setMesePubblicazioneString(String mesePubblicazione) {
        MesePubblicazione = meseStringToShort(mesePubblicazione);
    }

    public void setAnnoPubblicazione(int annoPubblicazione) {
        AnnoPubblicazione = annoPubblicazione;
    }
}
