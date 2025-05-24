package graphics;

import bookRecommender.LibrerieGestore;
import bookRecommender.UtenteGestore;
import bookRecommender.eccezioni.Eccezione;
import bookRecommender.entita.Librerie;

import javax.swing.*;
import java.awt.*;

public class menu extends JPanel {
    UtenteGestore utente;
    JScrollPane scroll;

    public menu(String titoloPagina, String paginaPrecendete, JScrollPane librerie, BookRecommender gui) {
        scroll = librerie;
        setLayout(new BorderLayout());
        JLabel titolo = new JLabel(titoloPagina, JLabel.EAST);
        add(titolo, BorderLayout.WEST);
        //controllo login
        utente = UtenteGestore.GetInstance();
        JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.RIGHT));
        if (utente.UtenteLoggato()) {
            JButton mostraLibreria = new JButton("Mostra libreria");
            mostraLibreria.addActionListener(e -> mostraLibrerie());
            panel1.add(mostraLibreria);
            JButton Logout = new JButton("Logout");
            Logout.addActionListener(e -> gui.logout());
            panel1.add(Logout);
        } else {
            JButton Login = new JButton("Logout");
            Login.addActionListener(e -> gui.showLogin(titoloPagina));
        }
        add(panel1, BorderLayout.EAST);
    }

    public void mostraLibrerie() {
        LibrerieGestore libG = LibrerieGestore.GetInstance();
        Eccezione ecc = libG.caricaLibrerie();
        if (ecc.getErrorCode() > 0) {
            return;
        }
        for (Librerie libreria : libG.GetLibrerie()){
            JButton b = new JButton(libreria.getNome());
            b.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            b.addActionListener(e -> gui.showLibreria());
        }
    }


    ;
}
