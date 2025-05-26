/*
 * Autori del progetto:
 * Nome: Colombo     Cognome: Patrizio     Matricola: 756148     Sede: CO
 * Nome: Felitti     Cognome: Fabio        Matricola: 758058     Sede: CO
 * Nome: Franchi     Cognome: Matteo       Matricola: 757075     Sede: CO
 * Nome: Iacono      Cognome: Alessandro   Matricola: 758451     Sede: CO
 */
package db;

import java.util.Scanner;

/**
 * Si occupa di interfacciarsi con l'utente per ottenere le credenziali di connessione alla propria installazione di postgres
 */

class GetDbCredentials {
    private int host;
    private String nome;
    private String password;
    private Scanner scanner = new Scanner(System.in);

    /**
     * Chiede all'utente l'host
      */
    protected void retrieveHost() {
        System.out.println("Inserisci porta (xxxx) localhost della tua istanza di PostgreSQL (del tipo localhost:xxxxx, compresa tra 1 e 65535)");
        String port = scanner.nextLine().trim();
        while(port.isEmpty() || !port.matches("\\d+")){
            System.out.println("Porta non valida");
            port = scanner.nextLine().trim();
        }
        while (Integer.parseInt(port) < 1 || Integer.parseInt(port) > 65535) {
            System.out.println("Numero porta impossibile !");
            port = scanner.nextLine().trim();
        }
        host = Integer.parseInt(port);
    }

    /**
     * Chiede all'utente il nome
     */
    protected void retrieveNome() {
        System.out.println("Inserisci username per accesso a DB");
        nome = scanner.nextLine().trim();
        while(nome.isEmpty()){
            System.out.println("Nome non valido");
            nome = scanner.nextLine().trim();
        }
    }

    /**
     * Chiede all'utente la password
     */
    protected void retrievePassword() {
        System.out.println("Inserisci password per accesso a DB");
        password = scanner.nextLine().trim();
        while(password.isEmpty()){
            System.out.println("Password non valida");
            password = scanner.nextLine().trim();
        }
    }

    protected int getHost() {
        return host;
    }

    protected String getNome() {
        return nome;
    }

    protected String getPassword() {
        return password;
    }

    protected void defaultCredentials() {
        host = 5432;
        nome = "postgres";
        password = "Patrizio.24";
    }
}