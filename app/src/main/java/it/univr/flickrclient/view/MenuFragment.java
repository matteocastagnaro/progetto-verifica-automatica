package it.univr.flickrclient.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import it.univr.flickrclient.FlickrClientApplication;
import it.univr.flickrclient.MVC;
import it.univr.flickrclient.R;

import static it.univr.flickrclient.controller.Controller.LAST_IMAGES_CHOOSE;
import static it.univr.flickrclient.controller.Controller.POPULAR_IMAGES_CHOOSE;

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
 * Fragment che compare soltanto nella versione per tablet. É il menu laterale che nella versione per smartphone é presente nel drawer
 */
public class MenuFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener{

    private MVC mvc;

    /**
     * Costruttore vuoto
     */
    public MenuFragment() {}

    /**
     * Metodo di creazione della view del fragment.
     *
     * @param inflater Layout inflater
     * @param container Container per l'inflating della view
     * @param savedInstanceState Stato dell'istanza creata
     * @return View creata
     */
    @UiThread
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.menu_fragment_layout, container, false);

        mvc = ((FlickrClientApplication) getActivity().getApplication()).getMVC();

        NavigationView navigationView = (NavigationView) view.findViewById(R.id.nav_view);
        MenuItem searchimagesitem = navigationView.getMenu().findItem(R.id.nav_search_images);
        searchimagesitem.setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        return view;
    }

    /**
     * Metodo per la gestione del menu.
     * Gli elementi cliccabili disponibili nel menu sono:
     * - Cerca immagini;
     * - Ultime immagini;
     * - Immagini popolari;
     * - Informazioni;
     *
     * @param item Elemento del menu selezionato
     * @return true
     */
    @UiThread
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

//      Creazione del Bundle args per passare gli elementi al fragment
        Bundle args = new Bundle();

        if (id == R.id.nav_search_images) {

            mvc.controller.launchFragment(getActivity(), new SearchImagesFragment(), false);

        } else if (id == R.id.nav_last_images) {

            ImagesFragment frag = new ImagesFragment();
            args.putString(LAST_IMAGES_CHOOSE, LAST_IMAGES_CHOOSE);
            frag.setArguments(args);
            mvc.controller.launchFragment(getActivity(), frag, false);

        } else if (id == R.id.nav_popular_images) {

            ImagesFragment frag = new ImagesFragment();
            args.putString(POPULAR_IMAGES_CHOOSE, POPULAR_IMAGES_CHOOSE);
            frag.setArguments(args);
            mvc.controller.launchFragment(getActivity(), frag, false);

        } else if (id == R.id.nav_dev_infos) {

            mvc.controller.infoDialog(getActivity());

        }

        return true;
    }
}
