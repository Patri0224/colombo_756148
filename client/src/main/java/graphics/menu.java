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
    BookRecommender gui;

    public menu(String titoloPagina, JScrollPane librerie) {
        scroll = librerie;
        this.gui = BookRecommender.GetInstance();
        setLayout(new BorderLayout());
        // Impostazione del titolo della pagina
        JPanel panel = new JPanel(new FlowLayout( FlowLayout.LEFT));
        panel.add(ComandoIndietro.getBottoneHome());
        panel.add(ComandoIndietro.getBottoneIndietro());
        panel.add(new JLabel(titoloPagina));
        add(panel, BorderLayout.WEST);
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
            Login.addActionListener(e -> gui.showLogin());
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
            b.addActionListener(e -> gui.showLibreria(libreria.getNome()));
        }
    }


    ;
}
