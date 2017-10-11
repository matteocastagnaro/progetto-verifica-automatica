package it.univr.flickrclient.view;

import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import it.univr.flickrclient.FlickrClientApplication;
import it.univr.flickrclient.R;

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
 * Activity principale e unica dell'applicazione. Estende AppCompatActivity
 */
public class MainActivity extends AppCompatActivity {

    private Drawer drawer;

    /**
     * Metodo di creazione della view dell'activity
     *
     * @param savedInstanceState Stato dell'istanza creata
     */
    @UiThread
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Il drawer è inizializzato solo negli smartphone
        if (!getResources().getBoolean(R.bool.isTablet)) {
            drawer = new Drawer(this, toolbar);
        }

        // Lancia il primo fragment (SearchImages) all'avvio dell'app
        ((FlickrClientApplication) getApplication()).getMVC().controller.launchFragment(this, new SearchImagesFragment(), false);

    }

    /**
     * Metodo necessario per la gestione del tasto "back". Viene gestito lo stack della creazione dei fragment: se ci sono fragment nello stack,
     * con il tasto indietro si va ad eliminare il fragment dallo stack in modo da tornare allo stato precedente
     */
    @UiThread
    @Override
    public void onBackPressed() {

        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            // Se lo stack dei fragment è > 0 si è all'interno di un altro fragment di una "macrosezione"
            if(getSupportFragmentManager().getBackStackEntryCount() > 0)
                getSupportFragmentManager().popBackStack();
            super.onBackPressed();
        }
    }

}
