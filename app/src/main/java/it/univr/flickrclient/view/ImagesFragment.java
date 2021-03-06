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
 * Fragment che mostra la lista delle immagini risultato
 */
public class ImagesFragment extends Fragment {

    private View view;

    private ListView listview;
    private SwipeRefreshLayout swipeLayout;

    private MVC mvc;

    /**
     * Costruttore vuoto del fragment
     */
    public ImagesFragment() {}

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

        if (getArguments().getString(LAST_IMAGES_CHOOSE) != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.menu_last_images));
        } else {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.menu_popular_images));
        }

        if (view == null) {
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

//            Listener per il click breve, porta al fragment che mostra la singola immagine
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    ResultsAdapter ra = (ResultsAdapter) listview.getAdapter();

                    SelectedImageFragment selectedImageFragment = new SelectedImageFragment();
                    Bundle args = new Bundle();

                    args.putString("id", ra.getItem(i).getId());
                    args.putString("title", ra.getItem(i).getTitle());
                    args.putString("url", ra.getItem(i).getUrl());

                    selectedImageFragment.setArguments(args);

                    mvc.controller.launchFragment(getActivity(), selectedImageFragment, true);
                }
            });

            launchTask();

            registerForContextMenu(listview);
        }

        return view;
    }

    /**
     * Metodo senza argomenti per il lancio del task creato
     */
    private void launchTask(){
        mvc.controller.launchTask(new SearchImagesAsyncTask(getActivity(), listview, getArguments().getString(LAST_IMAGES_CHOOSE) != null ? LAST_IMAGES_CHOOSE : POPULAR_IMAGES_CHOOSE, swipeLayout));
    }

    /**
     * Metodo per la creazione del menu contestuale della listview che appare con il click prolungato sull'elemento della listview
     *
     * @param menu Menu contestuale
     * @param v View di riferimento
     * @param menuInfo Informazioni del menu
     */
    @UiThread
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    /**
     * Metodo che viene lanciato quando viene cliccato (click prolungato) l'elemento item della listview e setta gli elementi del menu.
     * Se si preme sul primo elemento del menu si lancia il task per la condivisione dell'immagine, mentre se si seleziona il secondo elemento
     * del menu si lancia il fragment che contiene la listview di immagini relative allo stesso autore dell'immagine selezionata in precedenza
     *
     * @param item Elemento del menu
     * @return boolean
     */
    @UiThread
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        ResultsAdapter ra = (ResultsAdapter) listview.getAdapter();

        switch (item.getItemId()) {
            case R.id.context_menu_share:

                mvc.controller.launchTask(new ImageDownloadAsyncTask(getActivity(), this, ra.getItem(info.position).getUrl()));

                return true;
            case R.id.context_menu_sameauthor:

                SameAuthorImagesFragment frag = new SameAuthorImagesFragment();
//                Argomenti passati al fragment: nome dell'autore e id dell'autore
                Bundle args = new Bundle();
                args.putString("authorname", ra.getItem(info.position).getAuthorName());
                args.putString("author", ra.getItem(info.position).getAuthor());
                frag.setArguments(args);

                mvc.controller.launchFragment(getActivity(), frag, true);

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
