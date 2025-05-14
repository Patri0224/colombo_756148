package bookRecommender.entita;



public class itemComboBox {

    private String titoloLibro;
    private int id_libro;

    /**
     * classe intermedia utilizzata dai JComboBox per inserire le informazioni dei libri
     *
     * @param titoloLibro titolo del libro
     * @param id_libro id del libro
     */
    public itemComboBox(String titoloLibro, int id_libro) {
        this.titoloLibro = titoloLibro;
        this.id_libro = id_libro;
    }

    public String getTitoloLibro(){
        return titoloLibro;
    }

    public int getId_libro(){
        return id_libro;
    }

    @Override

    public String toString(){
        return titoloLibro;
    }
}
