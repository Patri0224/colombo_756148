package bookRecommender.entita;

import java.io.Serializable;

public class Score implements Serializable {
    private short[] punteggi = new short[6];
    private String[] note = new String[6];

    public Score(short[] punteggi, String[] note) {
        this.punteggi = punteggi;
        this.note = note;
    }

    public Score() {
    }

    public short[] getPunteggi() {
        return punteggi;
    }

    public void setPunteggi(short[] punteggi) {
        this.punteggi = punteggi;
    }

    public String[] getNote() {
        return note;
    }

    public void setNote(String[] note) {
        this.note = note;
    }

    public void setP(int indice, short punteggio) {
        punteggi[indice] = punteggio;
    }
    public void setN(int indice, String nota) {
        note[indice] = nota;
    }
    public short getP(int indice) {
        return punteggi[indice];
    }
    public String getN(int indice) {
        return note[indice];
    }
}
