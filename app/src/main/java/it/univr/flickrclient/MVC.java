package it.univr.flickrclient;

import it.univr.flickrclient.controller.Controller;
import it.univr.flickrclient.model.Model;

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
 * Classe MVC necessaria per la configurazione dell'architettura MVC
 */
public class MVC {

    public final Model model;
    public final Controller controller;

    /**
     * Costruttore della classe MVC
     *
     * @param model Modello dell'architettura MVC
     * @param controller Controller dell'architettura MVC
     */
    public MVC(Model model, Controller controller) {
        this.model = model;
        this.controller = controller;
    }
}
