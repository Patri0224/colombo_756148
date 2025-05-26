/*
 * Autori del progetto:
 * Nome: Colombo     Cognome: Patrizio     Matricola: 756148     Sede: CO
 * Nome: Felitti     Cognome: Fabio        Matricola: 758058     Sede: CO
 * Nome: Franchi     Cognome: Matteo       Matricola: 757075     Sede: CO
 * Nome: Iacono      Cognome: Alessandro   Matricola: 758451     Sede: CO
 */
package graphics;

public class oggettoIndietro {
    private String pagina;
    private String opzione;

    public oggettoIndietro(String pagina, String opzione) {
        this.pagina = pagina;
        this.opzione = opzione;
    }
    public String getPagina() {
        return pagina;
    }
    public String getOpzione() {
        return opzione;
    }
    @Override
    public String toString() {
        return "oggettoIndietro{" +
                "pagina='" + pagina + '\'' +
                ", opzione='" + opzione + '\'' +
                '}';
    }
}
