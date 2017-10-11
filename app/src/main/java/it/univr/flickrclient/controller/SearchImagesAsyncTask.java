package it.univr.flickrclient.controller;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import it.univr.flickrclient.FlickrClientApplication;
import it.univr.flickrclient.MVC;
import it.univr.flickrclient.model.Model;
import it.univr.flickrclient.view.ResultsAdapter;

import static it.univr.flickrclient.controller.Controller.LAST_IMAGES_CHOOSE;
import static it.univr.flickrclient.controller.Controller.POPULAR_IMAGES_CHOOSE;
import static it.univr.flickrclient.controller.Controller.SEARCH_IMAGES_CHOOSE;

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
 * Task generale per la ricerca delle immagini
 */
public class SearchImagesAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private final Activity act;

    private JSONObject obj;

    private String query;

    private MVC mvc;

    private ListView listView;
    private SwipeRefreshLayout swipeLayout;

    private ArrayList<Model.Picture> pictures;

    private String searchImagesURL;
    private String lastImagesURL;
    private String popularImagesURL;
    private String sameAuthorURL;

    private String choose;

    /**
     * Costruttore del task asincrono. Ricerca con parola chiave personalizzata. Lancia UnsupportedEncodingException se non riesce a convertire la stringa
     * in UTF-8
     *
     * @param act Activity di riferimento
     * @param searchString Stringa di ricerca
     * @param listView Listview da popolare
     * @param swipeLayout SwipeRefreshLayout necessario per bloccare il widget di caricamento
     */
    public SearchImagesAsyncTask(Activity act, String searchString, ListView listView, SwipeRefreshLayout swipeLayout) throws UnsupportedEncodingException {

        this.act = act;
        this.listView = listView;
        this.swipeLayout = swipeLayout;
        pictures = new ArrayList<>();

        mvc = ((FlickrClientApplication) act.getApplication()).getMVC();

        searchImagesURL = String.format("https://api.flickr.com/services/rest?method=flickr.photos.search&api_key=%s&text=%s&extras=url_z,description,tags&per_page=50&format=json&nojsoncallback=1",
                mvc.controller.FLICKR_KEY,
                URLEncoder.encode(searchString, "UTF-8"));

        choose = SEARCH_IMAGES_CHOOSE;

    }

    /**
     * Costruttore del task asincrono. Ricerche standard (last, popular, sameauthor)
     *
     * @param act Activity di riferimento
     * @param listView Listview da popolare
     * @param choose Stringa che identifica la scelta
     * @param swipeLayout SwipeRefreshLayout necessario per bloccare il widget di caricamento
     */
    public SearchImagesAsyncTask(Activity act, ListView listView, String choose, SwipeRefreshLayout swipeLayout) {

        this.act = act;
        this.listView = listView;
        this.choose = choose;
        this.swipeLayout = swipeLayout;
        pictures = new ArrayList<>();

        mvc = ((FlickrClientApplication) act.getApplication()).getMVC();

        switch (choose) {
            case LAST_IMAGES_CHOOSE:
                lastImagesURL = String.format("https://api.flickr.com/services/rest?method=flickr.photos.getRecent&api_key=%s&extras=owner_name,url_sq,url_l,description,tags&per_page=50&format=json&nojsoncallback=1",
                        mvc.controller.FLICKR_KEY);
                break;
            case POPULAR_IMAGES_CHOOSE:
                popularImagesURL = String.format("https://api.flickr.com/services/rest?method=flickr.interestingness.getList&api_key=%s&extras=owner_name,url_sq,url_l,description,tags&per_page=50&format=json&nojsoncallback=1",
                        mvc.controller.FLICKR_KEY);
                break;
            default:
                sameAuthorURL = String.format("https://api.flickr.com/services/rest?method=flickr.photos.search&api_key=%s&user_id=%s&extras=owner_name,url_sq,url_l,description,tags&per_page=50&format=json&nojsoncallback=1",
                        mvc.controller.FLICKR_KEY,
                        choose);

                Log.d("SAMEAUTHOR", sameAuthorURL);
        }

    }

    /**
     * Metodo eseguito dall'AsyncTask prima del lancio della thread in background
     */
    @UiThread
    @Override
    protected void onPreExecute() {
        swipeLayout.setRefreshing(true);
    }


    /**
     * Thread in background creata dalla thread principale.
     * Scarica le informazioni relative alla lista di immagini da scaricare
     *
     * @param voids
     * @return Boolean
     */
    @Override
    @WorkerThread
    protected Boolean doInBackground(Void... voids) {

        // Switch per impostare e usare la query desiderata
        switch (choose) {
            case SEARCH_IMAGES_CHOOSE:
                query = searchImagesURL;
                break;
            case LAST_IMAGES_CHOOSE:
                query = lastImagesURL;
                break;
            case POPULAR_IMAGES_CHOOSE:
                query = popularImagesURL;
                break;
            default:
                query = sameAuthorURL;
        }

        Log.d("QUERY", query);

        try {

            URLConnection urlConnection = new URL(query).openConnection();
            InputStream in = urlConnection.getInputStream();
            obj = new JSONObject(IOUtils.toString(in, "UTF-8"));

            JSONArray array = obj.getJSONObject("photos").getJSONArray("photo");

            // In base alla query il risultato delle api varia (i parametri esistenti), per questo esiste lo switch
            switch (choose) {
                case SEARCH_IMAGES_CHOOSE:
                    for (int i = 0; i < array.length(); i++) {
                        if (array.getJSONObject(i).has("url_z")) {
                            pictures.add(new Model.Picture(
                                    array.getJSONObject(i).getString("id"),
                                    array.getJSONObject(i).getString("title").isEmpty() ? "No title" : array.getJSONObject(i).getString("title"),
                                    array.getJSONObject(i).getString("url_z"),
                                    array.getJSONObject(i).getJSONObject("description").getString("_content").isEmpty() ? "No description" : array.getJSONObject(i).getJSONObject("description").getString("_content"),
                                    array.getJSONObject(i).getString("owner"),
                                    array.getJSONObject(i).has("ownername") ? array.getJSONObject(i).getString("ownername") : "iD Author: " + array.getJSONObject(i).getString("owner")));
                        }
                    }
                    break;
                default:
                    for (int i = 0; i < array.length(); i++) {
                        if (array.getJSONObject(i).has("url_l")) {
                            pictures.add(new Model.Picture(
                                    array.getJSONObject(i).getString("id"),
                                    array.getJSONObject(i).getString("title").isEmpty() ? "No title" : array.getJSONObject(i).getString("title"),
                                    array.getJSONObject(i).getString("url_l"),
                                    array.getJSONObject(i).getJSONObject("description").getString("_content").isEmpty() ? "No description" : array.getJSONObject(i).getJSONObject("description").getString("_content"),
                                    array.getJSONObject(i).getString("owner"),
                                    array.getJSONObject(i).has("ownername") ? array.getJSONObject(i).getString("ownername") : "iD Author: " + array.getJSONObject(i).getString("owner")));
                        } else {
                            pictures.add(new Model.Picture(
                                    array.getJSONObject(i).getString("id"),
                                    array.getJSONObject(i).getString("title").isEmpty() ? "No title" : array.getJSONObject(i).getString("title"),
                                    array.getJSONObject(i).getString("url_sq"),
                                    array.getJSONObject(i).getJSONObject("description").getString("_content").isEmpty() ? "No description" : array.getJSONObject(i).getJSONObject("description").getString("_content"),
                                    array.getJSONObject(i).getString("owner"),
                                    array.getJSONObject(i).has("ownername") ? array.getJSONObject(i).getString("ownername") : "iD Author: " + array.getJSONObject(i).getString("owner")));
                        }
                    }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Metodo che viene lanciato dalla thread principale una volta che é terminata l'esecuzione della thread in background.
     * Setta l'adapter alla listview con gli elementi scaricati dal thread in background, crea il Toast per informare che il caricamento é stato
     * completato ed elimina il widget di caricamento
     *
     * @param success
     */
    @UiThread
    @Override
    protected void onPostExecute(final Boolean success) {

        // Aggiornamento della listview
        listView.setAdapter(new ResultsAdapter(act, pictures));

        Toast.makeText(act.getApplicationContext(), "Caricamento completato", Toast.LENGTH_SHORT).show();

        swipeLayout.setRefreshing(false);

    }
}
