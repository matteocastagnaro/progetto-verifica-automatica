package it.univr.flickrclient.model;

/**
 * Progetto Flickr Client
 * Verifica Automatica di Sistemi
 * AA 2017/2018
 *
 * Created by:
 *  - Matteo Castagnaro - VR407832
 *  - Francesco Marretta - VR412875
 */

/**
 * Modello per l'architettura MVC
 */
public class Model {

    /**
     * Classe modello per le immagini
     */
    public static class Picture {

        public String id;
        public String title;
        public String url;
        public String description;
        private String author;
        private String authorname;

        /**
         * Costruttore della classe modello delle immagini. Informazioni settabili solo tramite il costruttore
         *
         * @param id ID dell'immagine
         * @param title Titolo dell'immagine
         * @param url URL dell'immagine
         * @param description Descrizione dell'immagine
         * @param author Autore dell'immagine
         * @param authorname Nome dell'autore dell'immagine
         */
        public Picture(String id, String title, String url, String description, String author, String authorname) {
            this.id = id;
            this.title = title;
            this.url = url;
            this.description = description;
            this.author = author;
            this.authorname = authorname;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }

        public String getDescription() {
            return description;
        }

        public String getAuthor() {
            return author;
        }

        public String getAuthorName() {
            return authorname;
        }

    }

    /**
     * Classe modello per i commenti delle immagini
     */
    public static class Message {

        private String author;
        private String date;
        private String body;

        /**
         * Costruttore della classe modello per i commenti delle immagini. Informazioni settabili solo tramite il costruttore
         *
         * @param author Autore del commento
         * @param date Data di pubblicazione del commento
         * @param body Corpo del commento
         */
        public Message(String author, String date, String body){
            this.author = author;
            this.date = date;
            this.body = body;
        }

        public String getAuthor() {
            return author;
        }

        public String getDate() {
            return date;
        }

        public String getBody() {
            return body;
        }
    }

}
