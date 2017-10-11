package it.univr.flickrclient.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import it.univr.flickrclient.FlickrClientApplication;
import it.univr.flickrclient.MVC;
import it.univr.flickrclient.R;
import it.univr.flickrclient.controller.ImageDownloadAsyncTask;
import it.univr.flickrclient.controller.SearchImagesAsyncTask;

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
 * Fragment che mostra la lista delle immagini relative ad uno stesso autore
 */
public class SameAuthorImagesFragment extends Fragment {

    private ListView listview;
    private View view;

    private String image_url;
    private MVC mvc;

    private SwipeRefreshLayout swipeLayout;

    /**
     * Costruttore vuoto del fragment
     */
    public SameAuthorImagesFragment(){}

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mvc = ((FlickrClientApplication) getActivity().getApplication()).getMVC();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getArguments().getString("authorname"));

        if(view == null) {
            view = inflater.inflate(R.layout.lastandpopular_images_fragment_layout, container, false);

            listview = (ListView) view.findViewById(R.id.list_view_results);
            swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

            swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    launchTask();
                }
            });

            swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);

            // Listener per il click breve, porta al fragment che mostra la singola immagine
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    ResultsAdapter ra = (ResultsAdapter) listview.getAdapter();

                    image_url = ra.getItem(i).getUrl();

                    SelectedImageFragment frag = new SelectedImageFragment();
                    Bundle args = new Bundle();
                    args.putString("id", ra.getItem(i).getId());
                    args.putString("title", ra.getItem(i).getTitle());
                    args.putString("url", ra.getItem(i).getUrl());
                    frag.setArguments(args);

                    mvc.controller.launchFragment(getActivity(), frag, true);

                }
            });

            registerForContextMenu(listview);

            launchTask();
        }

        return view;
    }

    /**
     * Metodo senza argomenti per il lancio del task creato
     */
    private void launchTask(){
        mvc.controller.launchTask(new SearchImagesAsyncTask(getActivity(), listview, getArguments().getString("author"), swipeLayout));
    }

    /**
     * Metodo per la creazione del menu contestuale della listview che appare con il click prolungato sull'elemento della listview.
     * A differenza dei menu nelle altre listview, qui non é presente la voce "Stesso autore" perché le immagini sono giá di uno stesso autore
     *
     * @param menu Menu contestuale
     * @param v View di riferimento
     * @param menuInfo Informazioni del menu
     */
    @UiThread
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.context_menu, menu);

        menu.getItem(1).setVisible(false);
    }

    /**
     * Metodo che viene lanciato quando viene cliccato (click prolungato) l'elemento item della listview e setta gli elementi del menu.
     * Se si preme sull'elemento del menu si lancia il task per la condivisione dell'immagine
     *
     * @param item Elemento del menu
     * @return boolean
     */
    @UiThread
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        ResultsAdapter ra = (ResultsAdapter) listview.getAdapter();
        image_url = ra.getItem(info.position).getUrl();

        switch (item.getItemId()) {
            case R.id.context_menu_share:

                mvc.controller.launchTask(new ImageDownloadAsyncTask(getActivity(), this, image_url));

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
