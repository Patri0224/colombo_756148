package bookRecommender.entita;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LibriFileInutile {
    String csvFile = "data/BooksDataClean.csv";

    public LibriFileInutile(String nomeFile) {
        if (nomeFile != "")
            csvFile = nomeFile;
    }

    public bookrecommender.classi.Libri[] readFile() {
        List<bookrecommender.classi.Libri> libriList = new ArrayList<>();
        int i = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                // Skip the header
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                //test
                i++;
                if (i % 1000 == 0) System.out.println(i);
                String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                int id = Integer.parseInt(values[0]);
                String titolo = values[1];
                if(titolo.startsWith("\"")){
                    titolo=titolo.substring(1,titolo.length()-1);
                }
                String[] autori = values[2].replace("By ", "").replace("\"", "").replace(" and ", "").split(", ");
                String descrizione;
                if (values[3].length()<2)
                    descrizione = values[3];
                else
                    descrizione = values[3].substring(1, values[3].length() - 1);
                String[] categorie = values[4].replace("\"", "").split(" , ");
                String editore = values[5];
                float prezzoPartenza = Float.parseFloat(values[6]);
                short mesePubblicazione = bookrecommender.classi.Libri.meseStringToShort(values[7]);
                int annoPubblicazione = Integer.parseInt(values[8]);

                bookrecommender.classi.Libri libro = new bookrecommender.classi.Libri(titolo, autori, descrizione, categorie, editore, prezzoPartenza, mesePubblicazione, annoPubblicazione);
                libriList.add(libro);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("ciao");
        bookrecommender.classi.Libri[] array = new bookrecommender.classi.Libri[libriList.size()];
        array = libriList.toArray(array);
        return array;
    }

    public int writeFile(bookrecommender.classi.Libri[] libri) throws IOException {
        FileWriter writer = new FileWriter("data/BooksData.csv");
        int num = 0;
        for (bookrecommender.classi.Libri libro : libri) {
            num++;
            if (num % 1000 == 0) System.out.println(num);
            String str = "";
            str += libro.getId() + ",";
            str += libro.getTitolo() + ",";
            for (String autore : libro.getAutoriArray())
                str += autore + ", ";
            str = str.substring(0, str.length() - 2);
            str += ",";
            str += libro.getDescrizione() + ",";
            for (String categ : libro.getCategorieArray())
                str += categ + " , ";
            str = str.substring(0, str.length() - 3);
            str += ",";
            str += libro.getEditore() + ",";
            str += libro.getPrezzoPartenzaDollari() + ",";
            str += libro.getMesePubblicazione() + ",";
            str += libro.getAnnoPubblicazione() + ",";

            writer.write(str + "\n");
        }
        return 0;
    }

}
