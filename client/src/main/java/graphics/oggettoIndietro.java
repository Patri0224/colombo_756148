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
