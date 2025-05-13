package com.example;

public class UtentiRegistrati {

        private String user_id;
        private String nome;
        private String cognome;
        private String cod_fisc;
        private String password;
        private String mail;
        private int id;

        // Costruttore
        public UtentiRegistrati(String user_id, String nome, String cognome,
                                String cod_fisc, String password, String mail, int id) {
            this.user_id = user_id;
            this.nome = nome;
            this.cognome = cognome;
            this.cod_fisc = cod_fisc;
            this.password = password;
            this.mail = mail;
            this.id = id;
        }

        // Metodi getter

        public String getUserId() {
            return user_id;
        }

        public String getNome() {
            return nome;
        }

        public String getCognome() {
            return cognome;
        }

        public String getCodFisc() {
            return cod_fisc;
        }

        public String getPassword() {
            return password;
        }

        public String getMail() {
            return mail;
        }

        public int getId(){
            return id;
        }

        // Metodi setter

        public void setUserId(String user_id) {
            this.user_id = user_id;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public void setCognome(String cognome) {
            this.cognome = cognome;
        }

        public void setCodFisc(String cod_fisc) {
            this.cod_fisc = cod_fisc;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setMail(String mail) {
            this.mail = mail;
        }

        public void setId(int id){
            this.id = id;
        }
    }


