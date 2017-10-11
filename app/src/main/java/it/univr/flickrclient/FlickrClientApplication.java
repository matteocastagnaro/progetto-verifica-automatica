package it.univr.flickrclient;

import android.app.Application;
import android.support.annotation.UiThread;

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
 * Classe che estende Application che definisce l'applicazione
 */
public class FlickrClientApplication extends Application {

    private MVC mvc;

    /**
     * Metodo chiamato durante la creazione dell'applicazione. Crea l'oggetto mvc
     */
    @UiThread
    @Override
    public void onCreate() {
        super.onCreate();

        mvc = new MVC(new Model(), new Controller());
    }

    /**
     * Metodo che ritorna l'oggetto mvc
     *
     * @return MVC
     */
    public MVC getMVC() {
        return mvc;
    }
}
