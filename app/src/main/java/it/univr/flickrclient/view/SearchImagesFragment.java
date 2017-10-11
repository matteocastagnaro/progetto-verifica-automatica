package it.univr.flickrclient.view;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.UnsupportedEncodingException;

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

// Fragment di ricerca delle immagini, presenta un'area di testo in alto + bottone Cerca
/**
 * Fragment di ricerca delle immagini, presenta un'area di testo in alto e il bottone "Cerca"
 */
public class SearchImagesFragment extends Fragment {

    private ListView listview;
    private View view;

    private SwipeRefreshLayout swipeLayout;
    private String searchString;

    private MVC mvc;

    /**
     * Costruttore vuoto del fragment
     */
    public SearchImagesFragment() {}

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

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(searchString == null ? getActivity().getResources().getString(R.string.menu_images_search) : searchString);

        if (view == null) {

            view = inflater.inflate(R.layout.search_images_fragment_layout, container, false);

            listview = (ListView) view.findViewById(R.id.list_view_results);
            final EditText et_search = (EditText) view.findViewById(R.id.et_search);

            swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

            swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    try {
                        mvc.controller.launchTask(new SearchImagesAsyncTask(getActivity(), searchString, listview, swipeLayout));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });

            swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    ResultsAdapter ra = (ResultsAdapter) listview.getAdapter();

                    SelectedImageFragment frag = new SelectedImageFragment();
                    Bundle args = new Bundle();
                    args.putString("id", ra.getItem(i).getId());
                    args.putString("title", ra.getItem(i).getTitle());
                    args.putString("url", ra.getItem(i).getUrl());
                    frag.setArguments(args);

                    mvc.controller.launchFragment(getActivity(), frag, true);
                }
            });

            ImageButton btn_search = (ImageButton) view.findViewById(R.id.btn_search);
            btn_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    searchString = et_search.getText().toString();

                    if(searchString.matches("")) {
                        // l'EditText é vuoto
                        et_search.setError("Non puó essere vuoto");
                    } else {
                        try {
                            mvc.controller.launchTask(new SearchImagesAsyncTask(getActivity(), searchString, listview, swipeLayout));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(et_search.getText());
                        et_search.setText("");

                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            });

            registerForContextMenu(listview);
        }

        return view;
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
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
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
                Bundle args = new Bundle();
                args.putString("author", ra.getItem(info.position).getAuthor());
                args.putString("authorname", ra.getItem(info.position).getAuthorName());
                frag.setArguments(args);

                mvc.controller.launchFragment(getActivity(), frag, true);

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
