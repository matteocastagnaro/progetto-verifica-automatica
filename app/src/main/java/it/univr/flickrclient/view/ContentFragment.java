package it.univr.flickrclient.view;

import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 * Classe del Fragment iniziale. Vuoto perché utile nello stato iniziale dell'app
 */
public class ContentFragment extends Fragment {

    private View view;

    /**
     * Costruttore vuoto del fragment
     */
    public ContentFragment(){}

    /**
     * Metodo di creazione della view del fragment
     *
     * @param inflater Elemento necessario per l'inflate del layout nel container
     * @param container Necessario per l'inflating del layout nella view
     * @param savedInstanceState Stato dell'istanza creata. Varibile non utilizzata
     * @return View creata
     */
    @UiThread
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_fragment_layout, container, false);

        return view;
    }

}
