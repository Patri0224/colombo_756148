package db;

import java.util.Scanner;

class GetDbCredentials {
    private int host;
    private String nome;
    private String password;
    private Scanner scanner = new Scanner(System.in);

    protected void retrieveHost() {
        System.out.println("Inserisci porta (xxxx) localhost della tua istanza di PostgreSQL (del tipo localhost:xxxxx, compresa tra 1 e 65535)");
        String port = scanner.nextLine().trim();
        while (Integer.parseInt(port) < 1 || Integer.parseInt(port) > 65535) {
            System.out.println("Numero porta impossibile !");
            port = scanner.nextLine().trim();
        }
        host = Integer.parseInt(port);
    }

    protected void retrieveNome() {
        System.out.println("Inserisci username per accesso a DB");
        nome = scanner.nextLine().trim();
    }

    protected void retrievePassword() {
        System.out.println("Inserisci password per accesso a DB");
        password = scanner.nextLine().trim();
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