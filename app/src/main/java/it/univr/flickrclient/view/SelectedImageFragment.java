package it.univr.flickrclient.view;

import android.app.Fragment;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import it.univr.flickrclient.FlickrClientApplication;
import it.univr.flickrclient.MVC;
import it.univr.flickrclient.R;
import it.univr.flickrclient.controller.GetImageInfosAsyncTask;

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
 * Fragment che mostra l'immagine selezionata e i relativi commenti
 */
public class SelectedImageFragment extends Fragment {

    private String image_id;
    private String image_url;
    private String title_str;

    private ImageView image;

    private View view;

    private MVC mvc;

    /**
     * Costruttore vuoto del fragment
     */
    public SelectedImageFragment() {}

    /**
     * Metodo di creazione della view del fragment
     *
     * @param inflater Layout inflater
     * @param container Container per l'inflating della view
     * @param savedInstanceState Stato dell'istanza creata
     * @return View creata
     */
    @UiThread
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mvc = ((FlickrClientApplication) getActivity().getApplication()).getMVC();

        if(view == null) {
            view = inflater.inflate(R.layout.selected_image_fragment, container, false);

            Bundle args = getArguments();

            if (args != null) {
                image_id = args.getString("id");
                image_url = args.getString("url");
                title_str = args.getString("title");
            }

            final ListView listview = (ListView) view.findViewById(R.id.selected_image_comments);
            TextView empty = (TextView) view.findViewById(R.id.empty);

            listview.setEmptyView(empty);

            image = (ImageView) view.findViewById(R.id.selected_image_src);
            final TextView title = (TextView) view.findViewById(R.id.selected_image_title);

            Glide.with(getActivity()).load(image_url).into(image);

            title.setText(title_str);

            mvc.controller.launchTask(new GetImageInfosAsyncTask(getActivity(), image_id, listview));
        }

        return view;
    }

    /**
     * Metodo per la creazione del fragment. Necessario per notificare al fragment la presenza di un menu contestuale
     *
     * @param savedInstanceState Stato dell'istanza creata
     */
    @UiThread
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    /**
     * Metodo per la crezione del menu nella toolbar. É presente nel menu solo il tasto "condividi"
     *
     * @param menu Menu contestuale
     * @param inflater Menu inflater
     */
    @UiThread
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.share_menu, menu);
    }

    /**
     * Metodo per la configurazione del menu contestuale. Premendo il tasto "condividi" si lancia l'intent relativo alla condivisione dell'immagine
     *
     * @param item Elemento del menu selezionato
     * @return boolean
     */
    @UiThread
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_share:

                mvc.controller.launchIntent(this, mvc.controller.shareImage(getActivity().getBaseContext(), ((BitmapDrawable) image.getDrawable()).getBitmap()));

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
