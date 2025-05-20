package db;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

public class DbBuilder {
    private final String[] ddlTabelleArr = {"create table libritemp(\n" +
            "\tlibro_id int generated always as identity,\n" +
            "\ttitolo varchar(400),\n" +
            "\tanno_pubblicazione smallint,\n" +
            "\tmese_pubblicazione varchar(9),\n" +
            "\tautori varchar(400),\n" +
            "\tdescrizione varchar (25000),\n" +
            "\tcategorie varchar(400),\n" +
            "\teditori varchar(400),\n" +
            "\tprezzo decimal(6,2)\n" +
            ")", "create table utenti_registrati_principale(\n" +
            "\tutente_id int generated always as identity primary key,\n" +
            "\tmail varchar(100) not null unique,\n" +
            "\tpasswd varchar(200) not null\n" +
            ")", "create table utenti_registrati_dettagli(\n" +
            "\tutente_id int primary key references utenti_registrati_principale(utente_id) on delete cascade on update cascade,\n" +
            "\tnome varchar(100) not null,\n" +
            "\tcognome varchar(100) not null,\n" +
            "\tcodice_fiscale char(16) not null unique\n" +
            ")", "create table libri_principale(\n" +
            "\tlibro_id int generated always as identity primary key,\n" +
            "\ttitolo varchar(400),\n" +
            "\tanno_pubblicazione smallint,\n" +
            "\tmese_pubblicazione varchar(9),\n" +
            "\tautori varchar(400)\n" +
            ")", "create table libri_dettagli(\n" +
            "\tlibro_id int primary key references libri_principale(libro_id) on delete cascade on update cascade,\n" +
            "\tdescrizione varchar (25000),\n" +
            "\tcategorie varchar(400),\n" +
            "\teditori varchar(400),\n" +
            "\tprezzo decimal(6,2)\n" +
            ")", "create table librerie(\n" +
            "\tlibreria_id int generated always as identity primary key,\n" +
            "\tutente_id int not null references utenti_registrati_principale(utente_id) on delete cascade on update cascade,\n" +
            "\tnome_libreria varchar(100) not null,\n" +
            "\t\n" +
            "\tunique(utente_id, nome_libreria)\n" +
            ")", "create table contenuto_libreria(\n" +
            "\tlibreria_id int references librerie(libreria_id) on delete cascade on update cascade,\n" +
            "\tlibro_id int references libri_principale(libro_id) on delete cascade on update cascade,\n" +
            "\t\n" +
            "\tprimary key(libreria_id, libro_id)\n" +
            ")", "create table consigli(\n" +
            "\tutente_id int references utenti_registrati_principale(utente_id) on delete cascade on update cascade,\n" +
            "\tlibro_id_riguardante int references libri_principale(libro_id) on delete cascade on update cascade,\n" +
            "\t\n" +
            "\tprimary key(utente_id, libro_id_riguardante)\n" +
            ")", "create table contenuto_consiglio(\n" +
            "\tutente_id int,\n" +
            "\tlibro_id_riguardante int,\n" +
            "\tlibro_id_consigliato int references libri_principale(libro_id) on delete cascade on update cascade,\n" +
            "\n" +
            "\tprimary key(utente_id, libro_id_riguardante, libro_id_consigliato),\n" +
            "\tforeign key(utente_id, libro_id_riguardante) references consigli(utente_id, libro_id_riguardante) on delete cascade on update cascade,\n" +
            "\t\n" +
            "\tcheck(libro_id_riguardante <> libro_id_consigliato)\n" +
            ")", "create table recensioni(\n" +
            "\tutente_id int references utenti_registrati_principale(utente_id) on delete cascade on update cascade,\n" +
            "\tlibro_id int references libri_principale(libro_id) on delete cascade on update cascade,\n" +
            "\t\n" +
            "\tnote_contenuto varchar(256) default(null),\n" +
            "\tnote_stile varchar(256) default(null),\n" +
            "\tnote_gradevolezza varchar(256) default(null),\n" +
            "\tnote_originalita varchar(256) default(null),\n" +
            "\tnote_edizione varchar(256) default(null),\n" +
            "\tnote_generale varchar(256) default(null),\n" +
            "\tcontenuto smallint not null check(contenuto between 1 and 5),\n" +
            "\tstile smallint not null check(stile between 1 and 5),\n" +
            "\tgradevolezza smallint not null check(gradevolezza between 1 and 5),\n" +
            "\toriginalita smallint not null check(originalita between 1 and 5),\n" +
            "\tedizione smallint not null check(edizione between 1 and 5),\n" +
            "\t\n" +
            "\tprimary key(utente_id, libro_id)\n" +
            ")"};
    private final String[] puliziaTabelle = {"update libritemp \n" +
            "set autori = NULL\n" +
            "where length(autori) = 2","update libritemp\n" +
            "set autori = REGEXP_REPLACE(autori, '^By ', '')\n" +
            "where autori LIKE 'By %'","UPDATE libritemp\n" +
            "SET categorie = LTRIM(categorie)"};
    private final String [] splittingLibri = {"insert into libri_principale (libro_id, titolo, anno_pubblicazione, mese_pubblicazione, autori)\n" +
            "overriding system value\n" +
            "select libro_id, titolo, anno_pubblicazione, mese_pubblicazione, autori\n" +
            "from libritemp","insert into libri_dettagli (libro_id, descrizione, categorie, editori, prezzo)\n" +
            "select Libro_id, Descrizione, Categorie, Editori, Prezzo\n" +
            "from libritemp"};
    private final String eliminaLibritemp = "drop table libritemp";

    private Connection connBookRecommenderDB;
    private Connection connTemplate0;
    private static DbBuilder instance;

    private DbBuilder() throws SQLException, IOException {
        while(true){
            try{
                connTemplate0 = Db.getConnection(0);
                System.out.println("Presa connessione con template");
                break;
            }
            catch(SQLException sqlException){
                System.err.println("Connessione fallita... riprovo tra 5sec");
                if(Objects.equals(sqlException.getSQLState(), "08001")){
                    System.err.println("Causa probabile: numero di porta sbagliato (db non in ascolto su quella porta");
                }
                System.err.println(sqlException.getMessage());
                System.err.println("Codice: " + sqlException.getSQLState());
            }
            try{
                Thread.sleep(5000);
            }
            catch(InterruptedException interruptedException){
            }
        }
        try{
            createDatabase();
        }
        catch(CreazioneAvvenutaEx creazioneAvvenutaEx){
            System.out.println(creazioneAvvenutaEx.getMessage());
            while(true){
                try{
                    connBookRecommenderDB = Db.getConnection(1);
                    System.out.println("Presa connessione con bookrecommenderdb");
                    break;
                }
                catch(SQLException sqlException){
                    System.err.println(sqlException.getMessage());
                    System.err.println("Codice: " + sqlException.getSQLState());
                    System.err.println("Connessione fallita (errore interno)... riprovo tra 5sec");
                }
                try{
                    Thread.sleep(5000);
                }
                catch(InterruptedException interruptedException){
                }
            }
            try(Statement statementBookRecommenderDB = connBookRecommenderDB.createStatement()){
                for(String s:ddlTabelleArr){
                    statementBookRecommenderDB.executeUpdate(s);
                }
                try(InputStream in = getClass().getResourceAsStream("/bd.csv")){
                    try(BufferedReader bufIn = new BufferedReader(new InputStreamReader(in))){
                        CopyManager copyManager = new CopyManager((BaseConnection) connBookRecommenderDB);
                        copyManager.copyIn("COPY libritemp(titolo, autori, descrizione, categorie, editori, prezzo, mese_pubblicazione, anno_pubblicazione)\n" +
                                "FROM STDIN\n" +
                                "WITH (FORMAT CSV, HEADER, DELIMITER ',')", bufIn);
                    }
                }
                for(String s:puliziaTabelle){
                    statementBookRecommenderDB.executeUpdate(s);
                }
                for(String s:splittingLibri){
                    statementBookRecommenderDB.executeUpdate(s);
                }
                statementBookRecommenderDB.executeUpdate(eliminaLibritemp);
            }
        }
        catch (SQLException sqlException){
            if(Objects.equals(sqlException.getSQLState(), "42P04")){
                System.out.println("DB già esistente. Programma pronto all'uso");
            }
            else{
                System.out.println(sqlException.getMessage());
                System.out.println("Codice errore SQL: " + sqlException.getSQLState());
            }
        }
        //connBookRecommenderDB.close();
    }
    public static DbBuilder getDbInstance() throws SQLException, IOException {
        if(instance == null) {
            instance = new DbBuilder();
        }
        return(instance);
    }
    private void createDatabase() throws SQLException, CreazioneAvvenutaEx{
        try(Statement statTemplate0 = connTemplate0.createStatement()){
            System.out.println("Creazione database");
            statTemplate0.executeUpdate("CREATE DATABASE bookrecommenderdb");
            System.out.println("Database creato");
            connTemplate0.close();
            throw new CreazioneAvvenutaEx("Costruzione e riempimento tabelle in corso");
        }
    }

}
