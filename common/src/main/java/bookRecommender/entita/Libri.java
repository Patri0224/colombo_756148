package bookRecommender.entita;

import java.io.Serializable;


public class Libri implements Serializable {
    private int Id;
    private String Titolo = "";
    private String Autori;
    private String Descrizione = "";
    private String Categorie;
    private String Editore = "";
    private float PrezzoPartenza = 0;
    private String MesePubblicazione = "";
    private int AnnoPubblicazione = 0;
    private static float CambioInEuro = 0.92F;


    public Libri() {
        Id = -1;
        Titolo = "";
        Autori = "";
        Descrizione = "";
        Categorie = "";
        Editore = "";
        PrezzoPartenza = 0;
        MesePubblicazione = "";
        AnnoPubblicazione = 0;
    }

    public Libri(int id, String titolo, String autori, String descrizione, String categorie, String editore, float prezzoPartenza, String mesePubblicazione, int annoPubblicazione) {

        Id = id;
        Titolo = titolo;
        Autori = autori;
        Descrizione = descrizione;
        Categorie = categorie;
        Editore = editore;
        PrezzoPartenza = prezzoPartenza;
        MesePubblicazione = mesePubblicazione;
        AnnoPubblicazione = annoPubblicazione;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "Id=" + Id +
                ", Titolo='" + Titolo + '\'' +
                ", Autori=" + Autori +
                ", Descrizione='" + Descrizione + '\'' +
                ", Categorie=" + Categorie +
                ", Editore='" + Editore + '\'' +
                ", PrezzoPartenza=" + PrezzoPartenza +
                ", MesePubblicazione=" + MesePubblicazione +
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

    public String getAutoriArray() {
        return Autori;
    }

    public String getDescrizione() {
        return Descrizione;
    }

    public String getCategorieArray() {
        return Categorie;
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
        return MesePubblicazione;
    }

    public int getAnnoPubblicazione() {
        return AnnoPubblicazione;
    }


    //inizio setter

    public void setId(int id) {
        Id = id;
    }

    public void setTitolo(String titolo) {
        Titolo = titolo;
    }

    public void setAutori(String autori) {
        Autori = autori;
    }

    public void setDescrizione(String descrizione) {
        Descrizione = descrizione;
    }

    public void setCategorie(String categorie) {
        Categorie = categorie;
    }

    public void setEditore(String editore) {
        Editore = editore;
    }

    public void setPrezzoPartenza(Float prezzoPartenza) {
        PrezzoPartenza = prezzoPartenza;
    }

    public void setMesePubblicazione(String mesePubblicazione) {
        MesePubblicazione = mesePubblicazione;
    }

    public void setAnnoPubblicazione(int annoPubblicazione) {
        AnnoPubblicazione = annoPubblicazione;
    }
}
