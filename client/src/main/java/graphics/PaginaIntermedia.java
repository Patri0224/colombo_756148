/*
 * Autori del progetto:
 * Nome: Colombo     Cognome: Patrizio     Matricola: 756148     Sede: CO
 * Nome: Felitti     Cognome: Fabio        Matricola: 758058     Sede: CO
 * Nome: Franchi     Cognome: Matteo       Matricola: 757075     Sede: CO
 * Nome: Iacono      Cognome: Alessandro   Matricola: 758451     Sede: CO
 */
package graphics;

import javax.swing.*;

public class PaginaIntermedia extends JPanel {
    public PaginaIntermedia() {
        reload();
    }
    public void reload() {
        removeAll();
        Config.setPanel1(this);
        add(new JLabel("Pagina Intermedia, questa pagina non dovrebbe essere visibile"));
        revalidate();
        repaint();
    }
}
